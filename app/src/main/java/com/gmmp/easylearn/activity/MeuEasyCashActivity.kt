package com.gmmp.easylearn.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.CupomAdapter
import com.gmmp.easylearn.helper.cuponsReferencia
import com.gmmp.easylearn.helper.meusCuponsReferencia
import com.gmmp.easylearn.helper.usuariosReferencia
import com.gmmp.easylearn.model.Cupom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_meu_easy_cash.*

class MeuEasyCashActivity : AppCompatActivity() {

    private lateinit var recyclerCupom : RecyclerView
    private lateinit var adapter : CupomAdapter
    private lateinit var listCupons : ArrayList<Cupom>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meu_easy_cash)
        iniciar()
    }

    private fun iniciar() {

        supportActionBar?.hide()
        val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
        listCupons = ArrayList()

        meusCuponsReferencia(usuarioId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(ds: DataSnapshot) {
                if(ds.exists()){
                    //Recupera todos os cupons cadastrados
                    for(datasnapshot in ds.children){
                        val cupom = datasnapshot.getValue(Cupom::class.java)
                        //Verifica se o cupom foi usado
                        if(cupom!!.usado.not()){
                            //Se não, ele adiciona na lista
                            listCupons.add(cupom)
                        }
                    }

                    var qtdCupons : String
                    if(listCupons.size == 1) {
                        qtdCupons = "1 cupom de desconto"
                    }else{
                        qtdCupons = "${listCupons.size} cupons de desconto"
                    }
                    txt_qtdCupons.setText(qtdCupons)
                }
            }

        })

        recyclerCupom = findViewById(R.id.recyclerCupons)
        recyclerCupom.layoutManager = LinearLayoutManager(this@MeuEasyCashActivity, LinearLayoutManager.VERTICAL, false)
        adapter = CupomAdapter(listCupons)
        recyclerCupom.adapter = adapter
    }
}
