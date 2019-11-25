package com.gmmp.easylearn.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.cursosReferencia
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_canal_alheio.*

class CanalAlheioActivity : AppCompatActivity() {

    private val listCursos = arrayListOf<Curso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canal_alheio)

        supportActionBar?.hide()

        if (NavegacaoActivity.usuarioGlobal != Usuario()) inicializar()
        else finish()

    }

    private fun inicializar() {

        // Perfil Básico
        textNomeCanal.text = NavegacaoActivity.usuarioGlobal.nome
        textDescricao.text = NavegacaoActivity.usuarioGlobal.descricao

        Glide.with(this)
                .load(NavegacaoActivity.usuarioGlobal.urlPerfil)
                .centerCrop()
                .into(imageProfile)

        Glide.with(this)
                .load(NavegacaoActivity.usuarioGlobal.urlWallpaper)
                .centerCrop()
                .into(imageThumb)

        // Carregando dados
        val dialog = ViewDialog(this)
        dialog.showDialog("Aguarde", "Obtendo dados de nossos servidores")

        // Cursos disponibilizados
        val adapter = CursosAdapter(this, listCursos)
        recyclerViewCursosDisponibilizados.layoutManager = LinearLayoutManager(this)
        recyclerViewCursosDisponibilizados.adapter = adapter

        cursosReferencia().addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                listCursos.clear()

                for (ds in dataSnapshot.children) {
                    val c = ds.getValue(Curso::class.java)

                    if (c?.idCanal == NavegacaoActivity.usuarioGlobal.id) {
                        listCursos.add(c)
                    }
                }

                textNCursosDisponibilizados.text = listCursos.size.toString()
                adapter.notifyDataSetChanged()
                dialog.hideDialog()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}
