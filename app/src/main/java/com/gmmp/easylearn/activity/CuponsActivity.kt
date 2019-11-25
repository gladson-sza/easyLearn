package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.CompartilharBottomSheet
import com.gmmp.easylearn.helper.cupomDesconto
import com.gmmp.easylearn.helper.nomeUsuario
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cupons.*
import org.jetbrains.anko.toast


class CuponsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cupons)

        iniciar()
    }

    @SuppressLint("DefaultLocale")
    private fun iniciar() {

        supportActionBar?.hide()

        val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
        cupomDesconto = usuarioId.toUpperCase()


        btn_confirmar.setOnClickListener {

            val desconto = txt_cupomDesconto.text.toString()

            toast(desconto)
        }

        usuariosReferencia().child(usuarioId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()) {
                    val u = ds.getValue(Usuario::class.java)
                    nomeUsuario = u!!.nome
                }
            }

        })
        val compartilhar = CompartilharBottomSheet()
        compartilhar.show(supportFragmentManager, "bottomsheet")
    }

}


