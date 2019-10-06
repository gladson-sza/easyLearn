package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Video
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_novo_video.*
import org.jetbrains.anko.toast

class NovoVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_video)

        val videoReferece = FirebaseDatabase.getInstance().reference
                .child("cursos")
                .child("TODO CURSO") // TODO
                .child("modulos")
                .child("TODO MODULO") // TODO
                .child("videos")

        imgUploadVideo.setOnClickListener {
            // TODO UPLOAD DE MÍDIA
        }

        btnIncluirMaterial.setOnClickListener {
            // TODO INCLUIR MATERIAL
        }

        btnAdicionarVideo.setOnClickListener {

            if (editNomeVideo.text.toString().isEmpty()) {
                editNomeVideo.error = "Preencha o nome do vídeo"
            } else {

                val nomeVideo = editNomeVideo.text.toString()
                val descricaoVideo = editDescricaoVideo.text.toString()

                val video = Video(nomeVideo,
                        nomeVideo,
                        "",
                        descricaoVideo,
                        null)

                // videoReferece.child(nomeVideo).setValue(video)

                toast("Aula publicada com sucesso!")
            }

        }

    }
}
