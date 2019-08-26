package com.gmmp.easylearn.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.DestaquesFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(){

    private var btnIniciarSessao: Button? = null
    private var txtCadastro: TextView? = null

    private var editEmail: EditText? = null
    private var editSenha: EditText? = null

    private var firebaseAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inicializar()
    }

    fun inicializar() {

        // Inicializa os EditTexts
        editEmail = findViewById(R.id.textEmail)
        editSenha = findViewById(R.id.textSenha)

        supportActionBar?.hide()
        //Inicializa o botão
        btnIniciarSessao = findViewById(R.id.buttonEntrar)
        btnIniciarSessao!!.setOnClickListener {

            val email = editEmail!!.text.toString()
            val senha = editSenha!!.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth!!.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(applicationContext, NavegacaoActivity::class.java))
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }

            
            
        }

        txtCadastro = findViewById(R.id.textCadastro)
        txtCadastro!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, CadastroActivity::class.java))
        })
    }
}