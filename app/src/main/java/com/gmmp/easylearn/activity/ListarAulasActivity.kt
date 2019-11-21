package com.gmmp.easylearn.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.AulaAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.moduloGlobal
import com.gmmp.easylearn.helper.videosReferencia
import com.gmmp.easylearn.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_listar_aulas.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ListarAulasActivity : AppCompatActivity() {

    val listaVideo = arrayListOf<Video>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_aulas)

        inicializar()
    }

    fun inicializar() {

        // Verifica que o Usuário é dono do Canal
        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        if (cursoGlobal.idCanal != auth) btnAdicionarNovaAula.visibility = View.GONE
        else {
            btnAdicionarNovaAula.setOnClickListener {
                startActivity<NovoVideoActivity>()
            }
        }

        // Configurações básicas de exibição
        supportActionBar?.title = moduloGlobal.nome


        // val dialog = ViewDialog(this)
        // dialog.showDialog("Carregando", "Aguarde, obtendo dados...")
        videosReferencia(cursoGlobal.id, moduloGlobal.id).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    listaVideo.clear()
                    for (ds in dataSnapshot.children) {

                        val video = ds.getValue(Video::class.java)

                        if (video != null) {
                            listaVideo.add(video)
                        }

                    }

                    recyclerAulas.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
                    recyclerAulas.adapter = AulaAdapter(listaVideo, applicationContext)
                    // dialog.hideDialog()
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }

        })
    }
}

