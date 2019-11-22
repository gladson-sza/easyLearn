package com.gmmp.easylearn.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.moduloGlobal
import com.gmmp.easylearn.helper.videosReferencia
import com.gmmp.easylearn.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.greentoad.turtlebody.mediapicker.MediaPicker
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig
import kotlinx.android.synthetic.main.activity_novo_video.*
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

class NovoVideoActivity : AppCompatActivity() {

    private var URL_VIDEO = ""
    private val idVideo = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_video)

        imgUploadVideo.setOnClickListener {

            val pickerConfig = MediaPickerConfig()
                    .setUriPermanentAccess(false)
                    .setAllowMultiSelection(false)
                    .setShowConfirmationDialog(true)
                    .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            MediaPicker.with(this, MediaPicker.MediaTypes.VIDEO)
                    .setConfig(pickerConfig)
                    .setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener{
                        override fun onMissingFileWarning() {
                            toast("O arquivo não pode ser encontrado!")
                        }
                    })
                    .onResult()
                    .subscribe({

                        var file = it[0]

                        val videoRef = FirebaseStorage.getInstance().reference
                                .child(cursoGlobal.id)
                                .child(moduloGlobal.id)
                                .child(idVideo)

                        val uploadTask = videoRef.putFile(file)

                        val upVideo = ViewDialog(this)
                        upVideo.showDialog("Aguarde", "Realizando upload do vídeo")

                        uploadTask.addOnFailureListener {
                            toast("Não foi possível realizar upload do vídeo")

                            upVideo.hideDialog()
                        }.addOnSuccessListener {
                            videoRef.downloadUrl.addOnCompleteListener {
                                URL_VIDEO = it.result.toString()
                                toast("Upload concluido: $it")

                                upVideo.hideDialog()
                            }
                        }

                    },{
                        toast("Erro: $it")
                    })

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

                if (URL_VIDEO.isNotEmpty()) {

                    val video = Video(idVideo,
                            nomeVideo,
                            "",
                            descricaoVideo,
                            URL_VIDEO)

                    videosReferencia(cursoGlobal.id, moduloGlobal.id).child(idVideo).setValue(video).toString()
                    toast("Aula adicionada com sucesso!")


                } else {
                    toast("Não foi possível publicar a Aula")
                }

                finish()
            }

        }

    }
}
