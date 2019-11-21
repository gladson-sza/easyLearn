package com.gmmp.easylearn.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Cartao
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pagamento.*

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar?.setHomeButtonEnabled(true)      //Ativar o botão
        supportActionBar?.title = "Forma de pagamento"


        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val viewDialog = ViewDialog(this)

        // Carrega as Informações do Usuário no Seu Perfil
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        val auth2 = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth2!!.uid).child("cartao")

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val c = dataSnapshot.getValue(Cartao::class.java)

                if (c != null) {
                    text_numero_cartao.setText(c.numeroCartao)
                    text_nome_cartao.setText(c.nomeCartao)
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

        button_confirmar.setOnClickListener {
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
        }


    }

    fun cadastrar(cartao: Cartao){
        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        usuariosReferencia().child(auth).child("cartao").setValue(cartao)
        Toast.makeText(this, "Cartão salvo", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Botão adicional na ToolBar
        when (item.itemId) {
            //Método para matar a activity e não deixa-lá indexada na pilhagem | PAULO
            //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            android.R.id.home -> finish()
        }

        return true
    }
}
