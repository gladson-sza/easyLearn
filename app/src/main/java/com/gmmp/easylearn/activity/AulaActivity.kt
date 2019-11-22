package com.gmmp.easylearn.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.gmmp.easylearn.R
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.cursosReferencia
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.helper.videoGlobal
import com.gmmp.easylearn.model.Cartao
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_aula.*
import org.jetbrains.anko.backgroundColor

class AulaActivity : AppCompatActivity() {

    var usuarioLogado = Usuario()
    val auth = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aula)

        supportActionBar?.hide()

        txt_nomeAula.text = videoGlobal.nome
        txt_nomeCurso.text = cursoGlobal.nome

        iniciar()
    }

    fun iniciar(){

        verificacaoDeCurtida()

        btn_gostei.setOnClickListener {
            gostei()
            //Log.i("asd", "aasd")
        }

        btn_naoGostei.setOnClickListener {
            naoGostei()
        }
    }

    fun verificacaoDeCurtida(){

        val auth = FirebaseAuth.getInstance().currentUser
        val gosteiReferencia = cursosReferencia().child(cursoGlobal.id).child("reacao").child("gostei").child(auth!!.uid)
        val naoGosteiReferencia = cursosReferencia().child(cursoGlobal.id).child("reacao").child("naoGostei").child(auth!!.uid)

        // verifica o usuário logado
        usuariosReferencia().child(auth!!.uid) .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usuarioLogado = dataSnapshot.getValue(Usuario::class.java)!!
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        // verificação de reação
        gosteiReferencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {
                    img_like.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        naoGosteiReferencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {
                    img_dislike.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun gostei(){
        // troca de cor
        img_like.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))
        img_dislike.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorInativo)))

        // remove o dislike
        cursosReferencia().child(cursoGlobal.id).child("reacao").child("naoGostei").child(auth!!.uid).removeValue()

        // salva como gostei
        cursosReferencia().child(cursoGlobal.id).child("reacao").child("gostei").child(auth!!.uid).setValue(usuarioLogado)
    }

    fun naoGostei(){
        // troca de cor
        img_dislike.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))
        img_like.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorInativo)))

        // remove do like
        cursosReferencia().child(cursoGlobal.id).child("reacao").child("gostei").child(auth!!.uid).removeValue()
        // salva o dislike
        cursosReferencia().child(cursoGlobal.id).child("reacao").child("naoGostei").child(auth!!.uid).setValue(usuarioLogado)
    }
}
