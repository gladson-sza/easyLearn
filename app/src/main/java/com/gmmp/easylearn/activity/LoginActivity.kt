package com.gmmp.easylearn.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.DestaquesFragment

class LoginActivity : AppCompatActivity(){

    private var btnIniciarSessao: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inicializar()
    }

    fun inicializar() {
        //Inicializa o botão
        btnIniciarSessao = findViewById(R.id.buttonEntrar)
        btnIniciarSessao!!.setOnClickListener {
            //Abre tela de destaque
            startActivity(Intent(applicationContext, DestaquesFragment::class.java))
        }
    }
}