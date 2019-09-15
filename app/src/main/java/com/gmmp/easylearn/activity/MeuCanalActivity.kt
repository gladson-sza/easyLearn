package com.gmmp.easylearn.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CursosDisponibilizadosAdapter
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Usuario
import com.gmmp.easylearn.model.ViewDialog
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

        inicializar()
    }

    fun inicializar() {

        val textNomeCanal = textNomeCanal
        val textViewDescricao = textDescricao

        val viewDialog = ViewDialog(this)
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        // Carrega as Informações do Usuário no Seu Perfil
        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)
        val cursos = usuario.child("canal").child("cursos")

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                textNomeCanal.text = u?.nome
                textViewDescricao.text = u?.descricao

                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // Botão de Novo Curso
        val buttonNovoCurso = buttonNovoCurso
        buttonNovoCurso.setOnClickListener {
            startActivity(Intent(this, NovoCursoActivity::class.java))
        }

        // Configura o RecyclerView de CursosDisponibilizados
        val adapter = CursosDisponibilizadosAdapter(listCursos, this)
        var recyclerView = recyclerViewCursosDisponibilizados
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Carrega os Dados
        cursos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listCursos.clear()
                for (ds in dataSnapshot.children) {
                    listCursos.add(ds.getValue(Curso::class.java)!!)
                }

                val textCursosDisponibilizados = textNCursosDisponibilizados
                textCursosDisponibilizados.text = listCursos.size.toString()

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

}