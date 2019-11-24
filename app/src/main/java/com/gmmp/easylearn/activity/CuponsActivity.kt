package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.CompartilharBottomSheet
import kotlinx.android.synthetic.main.activity_cupons.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import com.gmmp.easylearn.helper.cupomDesconto
import com.gmmp.easylearn.helper.nomeUsuario
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_compartilhar.*


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


