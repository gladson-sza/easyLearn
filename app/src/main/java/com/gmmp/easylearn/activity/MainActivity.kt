package com.gmmp.easylearn.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import com.gmmp.easylearn.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var buttonComecar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
    }

    fun inicializar() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            //Esconde a Actionbar
            supportActionBar!!.hide()

            //Inicializa o botão
            buttonComecar = findViewById(R.id.buttonComecar)
            buttonComecar.setOnClickListener {
                //Abre tela de cadastro
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }else{
            val intent = Intent(this@MainActivity, NavegacaoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
