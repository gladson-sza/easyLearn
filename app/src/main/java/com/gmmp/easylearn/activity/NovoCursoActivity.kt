package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Disciplina
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_meu_canal.*
import kotlinx.android.synthetic.main.activity_novo_curso.*

class NovoCursoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_curso)

        inicializar()
    }

    fun inicializar() {

        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth?.uid!!)
        val canal = usuario.child("canal").child("cursos")

        buttonSalvar.setOnClickListener {

            if (textNome.text.isNotEmpty() && textDescricao.text.isNotEmpty()) {

                canal.child(textNome.text.toString()).child("id").setValue("---")
                canal.child(textNome.text.toString()).child("nome").setValue(textNome.text.toString())
                canal.child(textNome.text.toString()).child("descricao").setValue(textDescricao.text.toString())
                canal.child(textNome.text.toString()).child("thumbUrl").setValue("---")
                canal.child(textNome.text.toString()).child("disciplina").setValue("---")

                Toast.makeText(applicationContext, "Curso Adicionado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
