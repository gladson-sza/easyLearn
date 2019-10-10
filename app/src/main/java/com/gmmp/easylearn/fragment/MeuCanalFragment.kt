package com.gmmp.easylearn.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.bumptech.glide.Glide

import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.NovoCursoActivity
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.adapter.MensagensAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Mensagen
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_meu_canal.*


/**
 * A simple [Fragment] subclass.
 */
class MeuCanalFragment : Fragment() {


    private var listCursos = arrayListOf<Curso>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_meu_canal, container, false)


        inicializar()
        return view
    }

    fun inicializar() {



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

                    textNomeCanal.text = u.nome
                    textDescricao.text = u.descricao

                    if (u.urlPerfil.isNotEmpty()) {
                        Glide.with(context)
                                .load(u.urlPerfil)
                                .centerCrop()
                                .into(imageProfile)
                    }

                    if (u.urlWallpaper.isNotEmpty()) {
                        Glide.with(context)
                                .load(u.urlWallpaper)
                                .centerCrop()
                                .into(imageThumb)
                    }
                }


                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // Botão de Novo Curso
        val buttonNovoCurso = buttonNovoCurso
        buttonNovoCurso.setOnClickListener {
            startActivity(Intent(activity, NovoCursoActivity::class.java))
        }

        // Configura o RecyclerView de CursosDisponibilizados




    }

}
