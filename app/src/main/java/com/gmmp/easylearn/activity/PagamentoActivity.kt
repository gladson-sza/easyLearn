package com.gmmp.easylearn.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Cartao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pagamento.*
import org.jetbrains.anko.toast

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        supportActionBar?.hide()
        toolbarPagamento.inflateMenu(R.menu.salvar)
        toolbarPagamento.setOnMenuItemClickListener{
            if(text_numero_cartao.text.toString().isNotEmpty()){
                if(text_nome_cartao.text.toString().isNotEmpty()){
                    if(text_data_validade.text.toString().isNotEmpty()){
                        if(text_codigo.text.toString().isNotEmpty()){
                            var c = Cartao(text_codigo.text.toString(), text_data_validade.text.toString(), text_nome_cartao.text.toString(), text_numero_cartao.text.toString())
                            cadastrar(c)
                        }else{
                            Toast.makeText(this, "Erro: Preencha o código", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Erro: Preencha a data de validade", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Erro: Preencha o nome do cartão", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Erro: Preencha o número do cartão", Toast.LENGTH_SHORT).show()
            }
            false
        }
        val viewDialog = ViewDialog(this)

        // Carrega as Informações do Usuário no Seu Perfil
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid).child("cartao")

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val c = dataSnapshot.getValue(Cartao::class.java)

                if (c != null) {
                    text_numero_cartao.setText(c.numeroCartao)
                    text_nome_cartao.setText(c.nomeCartao.toUpperCase())
                    text_data_validade.setText(c.dataValidade)
                    text_codigo.setText(c.codigo)
                }

                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // CULPA DO PAULO | GLADSON
//        usuariosReferencia().child(auth).child("cartoes").addValueEventListener(object : ValueEventListener {
//
//        })


    }

    fun cadastrar(cartao: Cartao){
        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        usuariosReferencia().child(auth).child("cartao").setValue(cartao).addOnSuccessListener{
            toast("Cartão adicionado com sucesso")
            finish()
        }.addOnFailureListener{
            toast("Erro ao adicionar cartão de crédito")
        }
    }

}
