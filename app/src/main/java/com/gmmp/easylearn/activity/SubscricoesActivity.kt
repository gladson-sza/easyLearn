package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.helper.comprado
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Curso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_subscricoes.*
import kotlinx.android.synthetic.main.fragment_meus_cursos.*

class SubscricoesActivity : AppCompatActivity() {

    private var cursosAdapter: CursosAdapter? = null
    private var listMeusCursos = arrayListOf<Curso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscricoes)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar?.setHomeButtonEnabled(true)      //Ativar o botão

        supportActionBar?.title = "Minhas subscrições"

        iniciar()

        val linearManager = LinearLayoutManager(this)

        cursosAdapter = this?.let { CursosAdapter(it, listMeusCursos!!) }
        recyclerViewSubs!!.layoutManager = linearManager
        recyclerViewSubs.isNestedScrollingEnabled = false
        recyclerViewSubs!!.adapter = cursosAdapter
    }

    fun iniciar(){

        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        usuariosReferencia().child(auth).child("matriculados").addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listMeusCursos.clear()

                        if (dataSnapshot.exists()) {
                            for (ds in dataSnapshot.children) {
                                val c = ds.getValue(Curso::class.java)
                                if (c != null) {
                                    listMeusCursos.add(c)
                                    comprado = true
                                }
                            }
                        }

                        cursosAdapter?.notifyDataSetChanged()
                        verificaCursos()
                    }

                    override fun onCancelled(de: DatabaseError) {

                    }
                }
        )

    }

    fun verificaCursos(){
        if(listMeusCursos.size == 0){
            Log.i("MEUCURSOS", listMeusCursos.size.toString())
            textView.visibility = View.VISIBLE
        }else{
            textView.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Botão adicional na ToolBar
        when (item.itemId) {
            //Método para matar a activity e não deixa-lá indexada na pilhagem | PAULO
            //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            android.R.id.home -> finish()
        }

        return true
    }
}
