package com.gmmp.easylearn.fragment


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_editar_perfil.view.*
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


/**
 * A simple [Fragment] subclass.
 *
 */
class EditarPerfilFragment : Fragment() {

    private val PERFIL_GALERIA = 100
    private val PERFIL_CAMERA = 200
    private val WALLPAPER = 300

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        inicializar(view)

        return view
    }

    fun inicializar(view: View) {

        val viewDialog = ViewDialog(activity)

        // Carrega as Informações do Usuário no Seu Perfil
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {
                    view.editNome.setText(u.nome)
                    view.editDescricao.setText(u.descricao)

                    if (u.urlPerfil.isNotEmpty()) {
                        Glide.with(context!!)
                                .load(u.urlPerfil)
                                .centerCrop()
                                .into(view.editImageProfile)
                    }

                    if (u.urlWallpaper.isNotEmpty()) {
                        Glide.with(context!!)
                                .load(u.urlWallpaper)
                                .centerCrop()
                                .into(view.editImageWallpaper)
                    }
                }

                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        view.editImageProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(activity!!.packageManager) != null) {
                startActivityForResult(intent, PERFIL_GALERIA)
            }
        }

        view.editImageWallpaper.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(activity!!.packageManager) != null) {
                startActivityForResult(intent, WALLPAPER)
            }
        }

        // Configuração do Botão de Salvar
        view.buttonSalvar.setOnClickListener {

            viewDialog.showDialog("Aguarde", "Salvando Informações")

            usuario.child("nome").setValue(view.editNome?.text.toString())
            usuario.child("descricao").setValue(view.editDescricao?.text.toString())

            // Imagem de Perfil

            val bitmapProfile = (view.editImageProfile.drawable as BitmapDrawable).bitmap
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

                    val bitmapWallpaper = (view.editImageWallpaper.drawable as BitmapDrawable).bitmap
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

                            // Volta para o fragment de MinhaConta
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.frameContainer, PreferenciasFragment())
                            transaction?.commit()

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
                    image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)

                    if (image != null) view?.editImageWallpaper?.setImageBitmap(image)
                } else if (requestCode == PERFIL_GALERIA) {
                    val uri = data!!.data
                    image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)

                    if (image != null) view?.editImageProfile?.setImageBitmap(image)
                } else if (requestCode == PERFIL_CAMERA) {
                    val uri = data!!.data
                    image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)

                    if (image != null) view?.editImageProfile?.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

}
