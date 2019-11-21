package com.gmmp.easylearn.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class EditarPerfilActivity : AppCompatActivity() {

    private val PERFIL_GALERIA = 100
    private val PERFIL_CAMERA = 200
    private val WALLPAPER = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        inicializar()
    }

    fun inicializar() {

        val viewDialog = ViewDialog(this)

        // Carrega as Informações do Usuário no Seu Perfil
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {
                    editNome.setText(u.nome)
                    editDescricao.setText(u.descricao)

                    if (u.urlPerfil.isNotEmpty()) {
                        Glide.with(applicationContext)
                                .load(u.urlPerfil)
                                .centerCrop()
                                .into(editImageProfile)
                    }

                    if (u.urlWallpaper.isNotEmpty()) {
                        Glide.with(applicationContext)
                                .load(u.urlWallpaper)
                                .centerCrop()
                                .into(editImageWallpaper)
                    }
                }

                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        editImageProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, PERFIL_GALERIA)
            }
        }

        editImageWallpaper.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, WALLPAPER)
            }
        }

        // Configuração do Botão de Salvar
        buttonSalvar.setOnClickListener {

            viewDialog.showDialog("Aguarde", "Salvando Informações")

            usuario.child("nome").setValue(editNome?.text.toString())
            usuario.child("descricao").setValue(editDescricao?.text.toString())

            // Imagem de Perfil

            val bitmapProfile = (editImageProfile.drawable as BitmapDrawable).bitmap
            val outputStreamProfile = ByteArrayOutputStream()
            bitmapProfile.compress(Bitmap.CompressFormat.JPEG, 70, outputStreamProfile)
            val imageBytesProfile = outputStreamProfile.toByteArray()

            val imageProfileRef = FirebaseStorage.getInstance().reference
                    .child("imagens")
                    .child("usuarios")
                    .child("${auth.uid}perfil.jpeg")

            val uploadTaskProfile = imageProfileRef.putBytes(imageBytesProfile)
            uploadTaskProfile.addOnFailureListener {
                toast("Não foi possível fazer upload da Foto de Perfil")
            }.addOnSuccessListener {
                imageProfileRef.downloadUrl.addOnFailureListener {
                    toast("Não foi possível fazer upload da Foto de Perfil")
                }.addOnSuccessListener {
                    usuario.child("urlPerfil").setValue(it.toString())
                    toast("Foto de perfil atualizado com sucesso")

                    // Imagem de Capa

                    val bitmapWallpaper = (editImageWallpaper.drawable as BitmapDrawable).bitmap
                    val outputStreamWallpaper = ByteArrayOutputStream()
                    bitmapWallpaper.compress(Bitmap.CompressFormat.JPEG, 70, outputStreamWallpaper)
                    val imageBytesWallpaper = outputStreamWallpaper.toByteArray()

                    val imageWallpaperRef = FirebaseStorage.getInstance().reference
                            .child("imagens")
                            .child("usuarios")
                            .child("${auth.uid}wallpaper.jpeg")

                    val uploadTaskWallpaper = imageWallpaperRef.putBytes(imageBytesWallpaper)
                    uploadTaskWallpaper.addOnFailureListener {
                        viewDialog.hideDialog()
                        toast("Não foi possível fazer upload da Foto de Capa")
                    }.addOnSuccessListener {
                        imageWallpaperRef.downloadUrl.addOnFailureListener {
                            toast("Não foi possível fazer upload da Foto de Capa")
                        }.addOnSuccessListener {
                            usuario.child("urlWallpaper").setValue(it.toString())
                            toast("Foto de capa atualizada com sucesso")
                            viewDialog.hideDialog()

                            finish()
                        }
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            var image: Bitmap? = null

            try {
                if (requestCode == WALLPAPER) {
                    val uri = data!!.data
                    image = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                    if (image != null) editImageWallpaper?.setImageBitmap(image)
                } else if (requestCode == PERFIL_GALERIA) {
                    val uri = data!!.data
                    image = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                    if (image != null) editImageProfile?.setImageBitmap(image)
                } else if (requestCode == PERFIL_CAMERA) {
                    val uri = data!!.data
                    image = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                    if (image != null) editImageProfile?.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }
}
