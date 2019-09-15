package com.gmmp.easylearn.activity

import android.content.Intent
import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.activity_novo_curso.*


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
                canal.child(editNomeCurso.text.toString()).child("id").setValue(editNomeCurso.text.toString())
                canal.child(editNomeCurso.text.toString()).child("idCanal").setValue(idCanal)
                canal.child(editNomeCurso.text.toString()).child("nome").setValue(editNomeCurso.text.toString())
                canal.child(editNomeCurso.text.toString()).child("descricao").setValue(editDescricaoCurso.text.toString())
                canal.child(editNomeCurso.text.toString()).child("thumbUrl").setValue("---")
                canal.child(editNomeCurso.text.toString()).child("disciplina").setValue(spinnerDisciplinas.selectedItem.toString())

                Toast.makeText(applicationContext, "Curso Adicionado", Toast.LENGTH_SHORT).show()
                finish()
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
