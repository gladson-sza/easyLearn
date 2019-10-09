package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.adapter.HorizontalAdapter
import com.gmmp.easylearn.dialog.ViewDialog
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


/**
 * A simple [Fragment] subclass.
 */
class MeusCursosFragment : Fragment() {

    private var listVistoPorUltimo: ArrayList<Aula>? = null
    private var cursosAdapter: CursosAdapter? = null
    private var listMeusCursos = arrayListOf<Curso>()
    private var listMatriculados = arrayListOf<String>()

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

        val recyclerViewMeusCursos = view.findViewById<RecyclerView>(R.id.recyclerViewMeusCursos)
        val linearManager = LinearLayoutManager(activity)

        //Inicializa os cursos
        cursosAdapter = activity?.let { CursosAdapter(it, listMeusCursos!!) }
        recyclerViewMeusCursos!!.layoutManager = linearManager
        recyclerViewMeusCursos.isNestedScrollingEnabled = false
        recyclerViewMeusCursos!!.adapter = cursosAdapter


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


//        val viewDialog = ViewDialog(activity)
//        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")
//
//        // Firebase
//        val auth = FirebaseAuth.getInstance().currentUser
//        val cursos = FirebaseDatabase.getInstance().reference.child("cursos")
//
//        // Quando clicar em ver todos ele vai listar todos
//        listarPor = "todos"

//        usuariosReferencia().child(auth?.uid.toString()).child("matriculados").addValueEventListener(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                        listMatriculados.clear()
//                        for (d in dataSnapshot.children) {
//                            val m = d.getValue(String::class.java)
//                            listMatriculados.add(m!!)
//                        }
//
//                        listMeusCursos.clear()
//                        for (m in listMatriculados) {
//                            cursosReferencia().child(m).child("inscritos").addValueEventListener(
//                                    object : ValueEventListener {
//                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                            for (d in dataSnapshot.children) {
//                                                val i = d.getValue(String::class.java)
//
//                                                if (i.equals()) {
//                                                    listMeusCursos.add(i)
//                                                }
//                                            }
//                                            cursosAdapter?.notifyDataSetChanged()
//                                            viewDialog.hideDialog()
//                                        }
//
//                                        override fun onCancelled(p0: DatabaseError) {
//                                        }
//                                    })
//                        }
//                    }
//
//                    override fun onCancelled(p0: DatabaseError) {
//                    }
//                })


//        listMeusCursos = ArrayList<Curso>()
//
//        val curso1 = Curso("1", "", "Emmerson Santa Rita", "Neste cursos voc...", "https://miro.medium.com/max/1200/1*RIANcAESOEI6IbMbxvE5Aw.jpeg", "Linguagem de Programção");
//        val curso2 = Curso("2", "", "Lucas Silva", "Aprenda a resolv...", "https://fundacaomatiasmachline.org.br/wp-content/uploads/2017/05/Dia-do-F%C3%ADsico.jpg", "Teste");
//        val curso3 = Curso("2", "", "Marcos Lima", "Aprenda a resolv...", "https://pbs.twimg.com/media/Ddj-mckXcAI8OL2.jpg", "Teste");
//        val curso4 = Curso("2", "", "Dalva lima de Souza", "Aprenda a resolv...", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSF71i_14oPiOR1llgjOpTgKjNRK2nFPLLSdDScAFjhcAqQbGcB", "Teste");
//
//        listMeusCursos!!.add(curso1)
//        listMeusCursos!!.add(curso2)
//        listMeusCursos!!.add(curso3)
//        listMeusCursos!!.add(curso4)


    }

}
