package com.gmmp.easylearn.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.SubscricoesActivity
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.adapter.HorizontalAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.comprado
import com.gmmp.easylearn.helper.cursosReferencia
import com.gmmp.easylearn.helper.listarPor
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Curso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_curso.*
import kotlinx.android.synthetic.main.fragment_meus_cursos.*
import kotlinx.android.synthetic.main.fragment_meus_cursos.view.*
import org.jetbrains.anko.margin


/**
 * A simple [Fragment] subclass.
 */
class MeusCursosFragment : Fragment() {

    private var listVistoPorUltimo: ArrayList<Aula>? = null
    private var cursosAdapter: CursosAdapter? = null
    private var listMeusCursos = arrayListOf<Curso>()
    private lateinit var textView : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meus_cursos, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view: View) {
        encherVistoPorUltimo()
        encherMeusCursos()

        val recyclerViewVistoPorUltimo = view.findViewById<RecyclerView>(R.id.recyclerViewVistoPorUltimo)

        //Inicializa Aulas vista por ultimo
        recyclerViewVistoPorUltimo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapterVistoPorUltimo = HorizontalAdapter(activity!!, listVistoPorUltimo!!)
        recyclerViewVistoPorUltimo.adapter = adapterVistoPorUltimo

        textView = view.findViewById(R.id.semCursos)
        val recyclerViewMeusCursos = view.findViewById<RecyclerView>(R.id.recyclerViewMeusCursos)
        val linearManager = LinearLayoutManager(activity)

        //Inicializa os cursos
        cursosAdapter = activity?.let { CursosAdapter(it, listMeusCursos!!) }
        recyclerViewMeusCursos!!.layoutManager = linearManager
        recyclerViewMeusCursos.isNestedScrollingEnabled = false
        recyclerViewMeusCursos!!.adapter = cursosAdapter

        view.textVerSubs.setOnClickListener {
            startActivity(Intent(context, SubscricoesActivity::class.java))
        }

    }

    fun encherVistoPorUltimo() {
        listVistoPorUltimo = ArrayList<Aula>()

        val aula1 = Aula("1", "Kotlin: Introdução", "Emmerson Santa Rita", "https://blog.intuz.com/wp-content/uploads/2017/12/Kotlin_Banner_V2--1-.jpg", "25:35")
        val aula2 = Aula("5", "C: Ponteiros", "Thiago", "https://i.ytimg.com/vi/9yUZygnRsm8/maxresdefault.jpg", "11:46")
        val aula3 = Aula("3", "Revisão Enem 2019", "Alex", "https://www.sinergia.edu.br/wp-content/uploads/2016/09/Banner-Aul%C3%A3o-Sinergia-FACEBOOK-17-setembro-1200x480.jpg", "25:35")

        listVistoPorUltimo!!.add(aula1)
        listVistoPorUltimo!!.add(aula3)
        listVistoPorUltimo!!.add(aula2)


    }

    fun encherMeusCursos() {

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

}
