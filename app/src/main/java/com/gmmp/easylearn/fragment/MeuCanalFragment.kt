package com.gmmp.easylearn.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.NovoCursoActivity
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_meu_canal.view.*


/**
 * A simple [Fragment] subclass.
 */
class MeuCanalFragment : Fragment() {


    private var listCursos = arrayListOf<Curso>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_meu_canal, container, false)

        inicializar(v)
        return v
    }

    fun inicializar(v: View) {

        val viewDialog = ViewDialog(activity)
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        // Carrega as Informações do Usuário no Seu Perfil
        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)
        val cursos = FirebaseDatabase.getInstance().reference.child("cursos")

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {

                    v.textNomeCanal.text = u.nome
                    v.textDescricao.text = u.descricao

                    if (u.urlPerfil.isNotEmpty()) {
                        Glide.with(activity!!)
                                .load(u.urlPerfil)
                                .centerCrop()
                                .into(v.imageProfile)
                    }

                    if (u.urlWallpaper.isNotEmpty()) {
                        Glide.with(activity!!)
                                .load(u.urlWallpaper)
                                .centerCrop()
                                .into(v.imageThumb)
                    }
                }


                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // Botão de novo curso
        v.buttonNovoCurso.setOnClickListener {
            startActivity(Intent(activity, NovoCursoActivity::class.java))
        }

        // Configura o RecyclerView de CursosDisponibilizados
        val adapter = CursosAdapter(activity!!, listCursos)
        var recyclerView = v.recyclerViewCursosDisponibilizados
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        cursos.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listCursos.clear()
                        for (d in dataSnapshot.children) {
                            val c = d.getValue(Curso::class.java)

                            if (c?.idCanal.equals("${auth.uid}")) {
                                listCursos.add(c!!)
                            }
                        }

                        v.textNCursosDisponibilizados.text = listCursos.size.toString()
                        adapter.notifyDataSetChanged()
                        viewDialog.hideDialog()

                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })

    }
}
