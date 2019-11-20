package com.gmmp.easylearn.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.TodosCursosActivity
import com.gmmp.easylearn.adapter.HorizontalAdapter
import com.gmmp.easylearn.adapter.VerticalAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.comprado
import com.gmmp.easylearn.helper.listarPor
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Curso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_destaques.view.*


/**
 * A simple [Fragment] subclass.
 */
class DestaquesFragment : Fragment() {

    private var listEmAlta: ArrayList<Aula>? = null
    private var listRecomendados: ArrayList<Aula>? = null
    private var listPrincipais = arrayListOf<Curso>()
    // private var listCursosInscritos = arrayListOf<Curso>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_destaques, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view: View) {
        encherEmAlta()
        encherRecomendados()

        view.textVerTudo.setOnClickListener {
            startActivity(Intent(activity, TodosCursosActivity::class.java))
        }

        val recyclerViewEmAlta = view.findViewById<RecyclerView>(R.id.recyclerViewEmAlta)
        val recyclerViewPrincipais = view.findViewById<RecyclerView>(R.id.recyclerViewPrincipais)
        val recyclerViewRecomendados = view.findViewById<RecyclerView>(R.id.recyclerViewRecomendados)

        //Inicializa Aulas em Alta
        recyclerViewEmAlta.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapterEmAlta = HorizontalAdapter(activity!!, listEmAlta!!)
        recyclerViewEmAlta.adapter = adapterEmAlta

        //Inicializa Canais Recomendados
        recyclerViewRecomendados.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapterRecomendados = HorizontalAdapter(activity!!, listRecomendados!!)
        recyclerViewRecomendados.adapter = adapterRecomendados

        //Inicializa Canais Principais
        val adapter = VerticalAdapter(context!!, listPrincipais)
        recyclerViewPrincipais.layoutManager = LinearLayoutManager(context!!)
        recyclerViewPrincipais.adapter = adapter

        encherPrincipais(adapter)


    }

    fun encherEmAlta() {
        listEmAlta = ArrayList()

        val aula1 = Aula("1", "Unidades de Medidas", "Leo Gomes", "https://www.jovensestudantes.com.br/wp-content/uploads/2018/07/gratis-unidades-de-medidas-e-ord.jpg", "25:35")
        val aula2 = Aula("5", "Porcentagem", "Ferretto", "https://images.passeidireto.com/thumbnails/video/65117545/thumb.jpg.xlarge", "11:46")
        val aula3 = Aula("3", "Teorema de Tales", "Alex", "https://www.jovensestudantes.com.br/wp-content/uploads/2018/07/gratis-teorema-de-tales-matemati.jpg", "25:35")
        val aula4 = Aula("2", "Multiplicação e Divisão", "Ferretto", "https://img.youtube.com/vi/0UGJRHq2PS4/maxresdefault.jpg", "15:32")
        val aula5 = Aula("4", "Adição e Subtração", "Ferretto", "https://images.passeidireto.com/thumbnails/video/65117508/thumb.jpg.xlarge", "23:22")

        listEmAlta?.add(aula2)
        listEmAlta?.add(aula4)
        listEmAlta?.add(aula5)
        listEmAlta?.add(aula3)
        listEmAlta?.add(aula1)
    }

    fun encherRecomendados() {
        listRecomendados = ArrayList()

        val aula1 = Aula("1", "Descomplica", "Preparatório para Vestibulares", "https://www.infoenem.com.br/wp-content/uploads/2018/04/social-share-descomplica-1280x720.jpg", "")
        val aula2 = Aula("2", "Stoodi", "Preparatório para Vestibulares", "https://cadernodoenem.com.br/wp-content/uploads/2016/09/stoodi-1024x576.png", "")
        val aula3 = Aula("3", "Pro ENEM", "Preparatório para Vestibulares", "https://www.concurseirosdamadrugada.com.br/wp-content/uploads/2018/09/logo-proenem-vale-a-pena.png", "")
        val aula4 = Aula("4", "AulaLivre.net", "Preparatório para Vestibulares", "https://sambatech.com/blog/wp-content/uploads/2015/01/banner-2-case-aula-livre1.png", "23:22")

        listRecomendados?.add(aula1)
        listRecomendados?.add(aula2)
        listRecomendados?.add(aula3)
        listRecomendados?.add(aula4)
    }

    fun encherPrincipais(adapter: VerticalAdapter) {

        val viewDialog = ViewDialog(activity)
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        // Firebase
        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val cursos = FirebaseDatabase.getInstance().reference.child("cursos")
        val matriculados = usuariosReferencia().child(auth).child("matriculados")

        // Quando clicar em ver todos ele vai listar todos
        listarPor = "todos"


        cursos.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listPrincipais.clear()
                        // listCursosInscritos.clear()

                        for (d in dataSnapshot.children) {
                            val c = d.getValue(Curso::class.java)

                            if (c != null) {
                                if (c.idCanal != auth) {
                                    listPrincipais.add(c)
                                    comprado = false
                                }
                            }

                        }

                        matriculados.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (ds in dataSnapshot.children) {
                                        val c = ds.getValue(Curso::class.java)
                                        if (c != null) {
                                            listPrincipais.remove(c)
                                            // listCursosInscritos.add(c)
                                        }
                                    }
                                }

                                adapter.notifyDataSetChanged()
                                viewDialog.hideDialog()

                            }


                            override fun onCancelled(ds: DatabaseError) {

                            }
                        })


                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }
                })

    }
}
