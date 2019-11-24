package com.gmmp.easylearn.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.TodosCursosActivity
import com.gmmp.easylearn.adapter.CanaisAdapter
import com.gmmp.easylearn.adapter.HorizontalCursosAdapter
import com.gmmp.easylearn.adapter.VerticalAdapter
import com.gmmp.easylearn.helper.comprado
import com.gmmp.easylearn.helper.listarPor
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_destaques.*
import kotlinx.android.synthetic.main.fragment_destaques.view.*


/**
 * A simple [Fragment] subclass.
 */
class DestaquesFragment : Fragment() {

    private var listRecomendados = arrayListOf<Curso>()
    private var listPrincipais = arrayListOf<Curso>()
    private var listEmAlta = arrayListOf<Usuario>()
    // private var listCursosInscritos = arrayListOf<Curso>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_destaques, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view: View) {


        view.textVerTudo.setOnClickListener {
            startActivity(Intent(activity, TodosCursosActivity::class.java))
        }

        val recyclerViewEmAlta = view.findViewById<RecyclerView>(R.id.recyclerViewEmAlta)
        val recyclerViewPrincipais = view.findViewById<RecyclerView>(R.id.recyclerViewPrincipais)
        val recyclerViewRecomendados = view.findViewById<RecyclerView>(R.id.recyclerViewRecomendados)

        //Inicializa Canais Recomendados
        val adapterRecomendados = HorizontalCursosAdapter(context!!, listRecomendados)
        recyclerViewRecomendados.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRecomendados.adapter = adapterRecomendados
        encherRecomendados(adapterRecomendados)

        //Inicializa Canais Principais
        val adapter = VerticalAdapter(context!!, listPrincipais)
        recyclerViewPrincipais.layoutManager = LinearLayoutManager(context!!)
        recyclerViewPrincipais.adapter = adapter
        encherPrincipais(adapter)

        //Inicializa Aulas em Alta
        val adapterEmAlta = CanaisAdapter(context!!, listEmAlta)
        recyclerViewEmAlta.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewEmAlta.adapter = adapterEmAlta
        encherEmAlta(adapterEmAlta, view)
    }

    fun encherEmAlta(adapter: CanaisAdapter, view: View) {
        val usuarios = FirebaseDatabase.getInstance().reference.child("usuarios")
        usuarios.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listEmAlta.clear()
                for (d in dataSnapshot.children) {
                    var u = d.getValue(Usuario::class.java)

                    if (u != null) {
                        listEmAlta.add(u)
                    }

                }
                view.progress_em_alta.visibility = View.GONE
                view.recyclerViewEmAlta?.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun encherRecomendados(adapter: HorizontalCursosAdapter) {
        // Firebase
        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val cursos = FirebaseDatabase.getInstance().reference.child("cursos")
        val matriculados = usuariosReferencia().child(auth).child("matriculados")

        // Quando clicar em ver todos ele vai listar todos
        listarPor = "todos"


        cursos.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listRecomendados.clear()
                        // listCursosInscritos.clear()

                        for (d in dataSnapshot.children) {
                            val c = d.getValue(Curso::class.java)

                            if (c != null) {
                                if (c.idCanal != auth) {
                                    listRecomendados.add(c)
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
                                            listRecomendados.remove(c)
                                            // listCursosInscritos.add(c)
                                        }
                                    }

                                    listRecomendados.reverse()
                                }

                                adapter.notifyDataSetChanged()
                                progress_recomendados.visibility = View.GONE
                                recyclerViewRecomendados.visibility = View.VISIBLE
                            }


                            override fun onCancelled(ds: DatabaseError) {

                            }
                        })


                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
    }

    fun encherPrincipais(adapter: VerticalAdapter) {
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
                                progress_circular.visibility = View.GONE

                                recyclerViewPrincipais.visibility = View.VISIBLE
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
