package com.gmmp.easylearn.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Disciplina
import com.gmmp.easylearn.model.ViewDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_novo_curso.*
import java.io.ByteArrayOutputStream


class NovoCursoActivity : AppCompatActivity() {

    private val GALERIA = 100
    private val SPINNER_VAZIO = "---------"

    private var listaCursos = arrayListOf<String>()

    private lateinit var imageThumb: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_curso)

        inicializar()
    }

    fun inicializar() {
        val viewDialog = ViewDialog(this)
        viewDialog.showDialog("Novo Curso", "Aguarde, carregando informações para seu novo curso...")

        val idCanal = FirebaseAuth.getInstance().currentUser?.uid
        val canal = FirebaseDatabase.getInstance().reference.child("cursos")
        imageThumb = findViewById(R.id.imageThumb)

        imageThumb.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(applicationContext.packageManager) != null) {
                startActivityForResult(intent, GALERIA)
            }
        }

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        adapter.add(SPINNER_VAZIO)
        spinnerDisciplinas.adapter = adapter

        val disciplinas = FirebaseDatabase.getInstance().reference.child("disciplinas")
        disciplinas.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (d in dataSnapshot.children) {
                            var disc = d.getValue(Disciplina::class.java)
                            adapter.add(disc?.nome.toString())
                        }

                        viewDialog.hideDialog()

                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })

        val cursos = FirebaseDatabase.getInstance().reference.child("cursos")
        cursos.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (d in dataSnapshot.children) {
                            val c = d.getValue(Curso::class.java)
                            listaCursos.add(c?.nome.toString())
                        }

                        viewDialog.hideDialog()

                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })

        buttonSalvar.setOnClickListener {

            val disciplinaSelecionada = spinnerDisciplinas.selectedItem as String

            if (editNomeCurso.text.isEmpty()) {
                Toast.makeText(applicationContext, "Nome do curso obrigatório", Toast.LENGTH_SHORT).show()
            } else if (editNomeCurso.text.length < 4) {
                Toast.makeText(applicationContext, "O nome do curso deve ter ao menos 4 caracteres", Toast.LENGTH_SHORT).show()
            } else if (listaCursos.contains(editNomeCurso.text.toString())) {

            } else if (editDescricaoCurso.text.isEmpty()) {
                Toast.makeText(applicationContext, "Descrição obrigatória", Toast.LENGTH_SHORT).show()
            } else if (disciplinaSelecionada.equals(SPINNER_VAZIO)) {
                Toast.makeText(applicationContext, "Você não selecionou a disciplina", Toast.LENGTH_SHORT).show()
            } else {

                val viewDialog = ViewDialog(this)
                viewDialog.showDialog("Carregando", "Aguarde, estamos preparando as coisas por aqui")

                val cursoId = editNomeCurso.text.toString()

                canal.child(cursoId).child("id").setValue(editNomeCurso.text.toString())
                canal.child(cursoId).child("idCanal").setValue(idCanal)
                canal.child(cursoId).child("nome").setValue(editNomeCurso.text.toString())
                canal.child(cursoId).child("descricao").setValue(editDescricaoCurso.text.toString())
                canal.child(cursoId).child("thumbUrl").setValue("---")
                canal.child(cursoId).child("disciplina").setValue(spinnerDisciplinas.selectedItem.toString())

                val bitmap = (imageThumb.drawable as BitmapDrawable).bitmap
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                val imageBytes = outputStream.toByteArray()

                val imageRef = FirebaseStorage.getInstance().reference
                        .child("imagens")
                        .child("cursos")
                        .child("$cursoId.jpeg")

                val uploadTask = imageRef.putBytes(imageBytes)
                uploadTask.addOnSuccessListener {
                    viewDialog.hideDialog()
                    Toast.makeText(applicationContext, "Curso criado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                uploadTask.addOnFailureListener {
                    viewDialog.hideDialog()
                    Toast.makeText(applicationContext, "Não foi possível fazer upload da Thumb", Toast.LENGTH_SHORT).show()
                }


            }

        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            var image: Bitmap? = null

            try {
                if (requestCode == GALERIA) {
                    val uri = data!!.data
                    image = MediaStore.Images.Media.getBitmap(application.contentResolver, uri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (image != null) imageThumb.setImageBitmap(image)

        }

    }
}
