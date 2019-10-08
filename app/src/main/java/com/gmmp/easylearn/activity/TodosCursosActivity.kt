package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.listarPor
import com.gmmp.easylearn.model.Curso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_todos_cursos.*

class TodosCursosActivity : AppCompatActivity() {

    private var listCursos = arrayListOf<Curso>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todos_cursos)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar?.setHomeButtonEnabled(true)      //Ativar o botão

        if(listarPor.equals("todos")) {
            supportActionBar?.title = "Todos os cursos"
            text_disciplina.text = "Todos os cursos"
        }else{
            text_disciplina.text = listarPor
            supportActionBar?.setTitle(listarPor)
        }

        iniciar()
    }

    fun iniciar(){

        val adapter = CursosAdapter(this, listCursos)
        recyclerViewTodosCursos.layoutManager = LinearLayoutManager(this)
        recyclerViewTodosCursos.adapter = adapter

        val viewDialog = ViewDialog(this)
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        // Firebase
        val auth = FirebaseAuth.getInstance().currentUser
        val cursos = FirebaseDatabase.getInstance().reference.child("cursos")

        cursos.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listCursos.clear()
                        for (d in dataSnapshot.children) {
                            val c = d.getValue(Curso::class.java)

                            if (!(c?.idCanal.equals("${auth!!.uid}")) && c?.disciplina.equals(listarPor)) {
                                listCursos.add(c!!)
                            }else{
                                if(listarPor.equals("todos"))
                                    listCursos.add(c!!)
                            }
                        }
                        adapter.notifyDataSetChanged()
                        adapter.notifyDataSetChanged()
                        viewDialog.hideDialog()
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
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
