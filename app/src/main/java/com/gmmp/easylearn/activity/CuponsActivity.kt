package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.CompartilharBottomSheet
import com.gmmp.easylearn.helper.*
import com.gmmp.easylearn.model.Cupom
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

    private lateinit var listCupons : ArrayList<Cupom>
    private lateinit var listCodigos : ArrayList<String>

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
        listCodigos = ArrayList()
        listCupons = ArrayList()

        btn_confirmar.setOnClickListener {
            val codigoCupom = txt_cupomDesconto.text.toString().toUpperCase()
            var isValido = false

            if (codigoCupom.equals(usuarioId.toUpperCase())) {
                toast("Você não pode usar seu próprio cupom!")
            } else {
                usuariosReferencia().addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(ds: DataSnapshot) {
                        if(ds.exists()){
                            var remetenteId = ""
                          
                            for(datasnapshot in ds.children){
                                val c = datasnapshot.key!!
                                listCodigos.add(c)
                            }

                            for(i in 0 until listCodigos.size){
                                if(codigoCupom.equals(listCodigos.get(i).toUpperCase())){
                                    isValido = true
                                    remetenteId = listCodigos.get(i)
                                }
                            }
                            if(isValido){
                                salvarCupom(remetenteId, usuarioId)
                            }else{
                                toast("Código inválido")
                            }

                        }
                    }

                })

            }
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

        easyCash.setOnClickListener {
            startActivity(Intent(this@CuponsActivity, MeuEasyCashActivity::class.java))
        }
    }

    //eePdXeJ40bNefYqMJr6ngUClcSF2

    fun salvarCupom(codigoCupom : String, usuarioId : String){
        cuponsReferencia(codigoCupom).child(usuarioId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(ds: DataSnapshot) {
                if(ds.exists()){
                    val c = ds.getValue(Cupom::class.java)

                    if(c!!.usado){
                        toast("Você já usou esse cupom de desconto")
                    }else{
                        toast("Você já cadastrou esse cupom de desconto")
                    }
                }else{
                    //Cupom para quem compartilhou o código e pra quem usou
                    val cupomDestinatario = Cupom(codigoCupom.toUpperCase(), usuarioId, 15, false)
                    cuponsReferencia(codigoCupom).child(usuarioId).setValue(cupomDestinatario)
                    meusCuponsReferencia(usuarioId).child(codigoCupom.toUpperCase()).setValue(cupomDestinatario)

                    val cupomRemetente = Cupom(usuarioId.toUpperCase(), codigoCupom, 15, false)
                    cuponsReferencia(usuarioId).child(codigoCupom).setValue(cupomRemetente)
                    meusCuponsReferencia(codigoCupom).child(usuarioId.toUpperCase()).setValue(cupomRemetente)

                    toast("Cupom cadastrado com sucesso!")
                }
            }

        })
    }
}


