package com.gmmp.easylearn.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmmp.easylearn.R
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.videoGlobal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_aula.*

class AulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aula)

        supportActionBar?.hide()

        txt_nomeAula.text = videoGlobal.nome
        txt_nomeCurso.text = cursoGlobal.nome
    }
}
