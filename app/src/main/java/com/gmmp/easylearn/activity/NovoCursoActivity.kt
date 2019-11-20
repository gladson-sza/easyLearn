package com.gmmp.easylearn.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.model.Disciplina
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_novo_curso.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.util.*


class NovoCursoActivity : AppCompatActivity() {

    private val GALERIA = 100
    private val SPINNER_VAZIO = "Selecione uma disciplina"

    private lateinit var imageThumb: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_curso)
        supportActionBar?.hide()
        inicializar()
    }

    fun inicializar() {
        val viewDialog = ViewDialog(this)
        viewDialog.showDialog("Novo Curso", "Aguarde, carregando informações para seu novo curso...")

        val idCanal = FirebaseAuth.getInstance().currentUser?.uid
        val novoCurso = FirebaseDatabase.getInstance().reference.child("cursos")
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

        groupCurso.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId.equals(R.id.Pago)){
                editPreco.visibility = View.VISIBLE
            }else{
                editPreco.visibility = View.GONE
            }
        }

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


        buttonSalvar.setOnClickListener {

            val disciplinaSelecionada = spinnerDisciplinas.selectedItem as String
            var preco = 0.0
            if(editPreco.text.isNotEmpty()){
                preco = editPreco.text.toString().toDouble()
            }

            when {
                editNomeCurso.text.isEmpty() -> toast("Nome do curso obrigatório")
                editNomeCurso.text.length < 4 -> toast("O nome do curso deve ter ao menos 4 caracteres")
                editDescricaoCurso.text.isEmpty() -> toast("Descrição obrigatória")
                disciplinaSelecionada == SPINNER_VAZIO -> toast("Você não selecionou a disciplina")

                else -> {
                    val viewDialog = ViewDialog(this)
                    viewDialog.showDialog("Carregando", "Aguarde, estamos preparando as coisas por aqui")

                    val cursoId = UUID.randomUUID().toString()

                    novoCurso.child(cursoId).child("id").setValue(cursoId)
                    novoCurso.child(cursoId).child("idCanal").setValue(idCanal)
                    novoCurso.child(cursoId).child("nome").setValue(editNomeCurso.text.toString())
                    novoCurso.child(cursoId).child("descricao").setValue(editDescricaoCurso.text.toString())
                    novoCurso.child(cursoId).child("disciplina").setValue(spinnerDisciplinas.selectedItem.toString())
                    novoCurso.child(cursoId).child("preco").setValue(preco)

                    val bitmap = (imageThumb.drawable as BitmapDrawable).bitmap
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                    val imageBytes = outputStream.toByteArray()

                    val imageRef = FirebaseStorage.getInstance().reference
                            .child("imagens")
                            .child("cursos")
                            .child("$cursoId.jpeg")

                    val uploadTask = imageRef.putBytes(imageBytes)
                    uploadTask.addOnFailureListener {
                        novoCurso.child(cursoId).removeValue()
                        viewDialog.hideDialog()
                        toast("Não foi possível fazer upload da Thumb")
                    }.addOnSuccessListener {
                        imageRef.downloadUrl.addOnFailureListener {
                            novoCurso.child(cursoId).removeValue()
                            viewDialog.hideDialog()
                            toast("Não foi possível fazer upload da Thumb")
                        }.addOnSuccessListener {
                            novoCurso.child(cursoId).child("thumbUrl").setValue(it.toString())
                            viewDialog.hideDialog()
                            toast("Curso criado com sucesso")
                            finish()
                        }


                    }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Botão adicional na ToolBar
        when (item.itemId) {
            android.R.id.home  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            -> finish()  //Método para matar a activity e não deixa-lá indexada na pilhagem
            else -> {
            }
        }
        return true
    }
}
