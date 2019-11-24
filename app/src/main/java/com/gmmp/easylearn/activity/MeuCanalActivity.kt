package com.gmmp.easylearn.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CursosAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_meu_canal.*

class MeuCanalActivity : AppCompatActivity() {

    private var listCursos = arrayListOf<Curso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meu_canal)

        supportActionBar?.title = "Meu Canal"
         supportActionBar?.hide()

        inicializar()
    }

    fun inicializar() {



        val viewDialog = ViewDialog(this)
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
                        Glide.with(applicationContext)
                                .load(u.urlPerfil)
                                .centerCrop()
                                .into(imageProfile)
                    }

                    if (u.urlWallpaper.isNotEmpty()) {
                        Glide.with(applicationContext)
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
        }
        )

        // Botão de Novo Curso
        val buttonNovoCurso = buttonNovoCurso
        buttonNovoCurso.setOnClickListener {
            startActivity(Intent(this, NovoCursoActivity::class.java))
        }

        // Configura o RecyclerView de CursosDisponibilizados
        val adapter = CursosAdapter(this, listCursos)
        var recyclerView = recyclerViewCursosDisponibilizados
        recyclerView.layoutManager = LinearLayoutManager(this)
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

                        textNCursosDisponibilizados.text = listCursos.size.toString()
                        adapter.notifyDataSetChanged()
                        viewDialog.hideDialog()

                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })

    }

}
