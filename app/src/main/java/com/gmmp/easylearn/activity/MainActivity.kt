package com.gmmp.easylearn.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.gmmp.easylearn.R

class MainActivity : AppCompatActivity() {

    private var buttonComecar: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
    }

    fun inicializar() {
        //Esconde a Actionbar
        supportActionBar!!.hide()

        //Inicializa o botão
        buttonComecar = findViewById(R.id.buttonComecar)
        buttonComecar!!.setOnClickListener {
            //Abre tela de cadastro
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }
}
