package com.gmmp.easylearn.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.videosReferencia
import com.gmmp.easylearn.model.Video
import com.google.firebase.storage.FirebaseStorage
//import com.jaiselrahman.filepicker.activity.FilePickerActivity
//import com.jaiselrahman.filepicker.config.Configurations
//import com.jaiselrahman.filepicker.model.MediaFile
import kotlinx.android.synthetic.main.activity_novo_video.*
import net.alhazmy13.mediapicker.Video.VideoPicker
import org.jetbrains.anko.toast
import java.io.File

class NovoVideoActivity : AppCompatActivity() {

    private val VIDEO = 500
    private var URL_VIDEO = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_video)

        imgUploadVideo.setOnClickListener {

            VideoPicker.Builder(this)
                    .mode(VideoPicker.Mode.GALLERY)
                    .directory(VideoPicker.Directory.DEFAULT)
                    .extension(VideoPicker.Extension.MP4)
                    .enableDebuggingMode(true)
                    .build()

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

                val idCurso = intent.getStringExtra("curso")
                val idModulo = intent.getStringExtra("modulo")

                if (idCurso.isNotEmpty() && idModulo.isNotEmpty() && URL_VIDEO.isNotEmpty()) {

                    val video = Video(nomeVideo,
                            nomeVideo,
                            "",
                            descricaoVideo,
                            URL_VIDEO)

                    videosReferencia(idCurso, idModulo).child(nomeVideo).setValue(video).toString()
                    toast("Aula adicionada com sucesso!")
                } else {
                    toast("Não foi possível publicar a Aula")
                }

                finish()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        toast("RESULT")

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                VIDEO -> {
                    if (data != null) {

                        val dialog = ViewDialog(this)
                        dialog.showDialog("Aguarde", "Fazendo Upload do Vídeo")

                        var video = Uri.fromFile(File(data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH)[0]))
                        val videRef = FirebaseStorage.getInstance().reference.
                                child(intent.getStringExtra("curso")).
                                child(intent.getStringExtra("modulo")).
                                child("videos/${video.lastPathSegment}")

                        val uploadTask = videRef.putFile(video)
                        uploadTask.addOnFailureListener {
                            toast("Não foi possível realizar o upload do vídeo")
                            dialog.hideDialog()
                        }.addOnSuccessListener {
                            URL_VIDEO = it.toString()
                            dialog.hideDialog()
                        }
                    } else {
                        toast("Deu ruim!")
                    }
                }
            }
        }
    }
}
