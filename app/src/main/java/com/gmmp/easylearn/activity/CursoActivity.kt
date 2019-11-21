package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.ModuloAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.*
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Modulo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_curso.*
import org.jetbrains.anko.padding
import org.jetbrains.anko.toast
import java.util.*

class CursoActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var deleteDialog: AlertDialog
    private lateinit var txtNomeModulo: EditText
    private lateinit var recyclerModulo : RecyclerView
    private var inscrito = false

    private val listaModulos = ArrayList<Modulo>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curso)

        iniciar()
        carregarModulos()
    }

    @SuppressLint("ResourceAsColor")
    private fun iniciar() {

        supportActionBar?.hide()
        Glide.with(applicationContext)
                .load(cursoGlobal.thumbUrl)
                .centerCrop()
                .into(imageCurso)

        tituloCurso.text = cursoGlobal.nome
        descricaoCurso.text = cursoGlobal.descricao

        //Btn novo modulo
        val builderDialog = AlertDialog.Builder(this)
        builderDialog.setTitle("Novo módulo")

        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.leftMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        params.topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)

        txtNomeModulo = EditText(this)
        txtNomeModulo.hint = "Nome do módulo"
        txtNomeModulo.padding = 16
        txtNomeModulo.setBackgroundResource(R.color.colorEditText)
        txtNomeModulo.setTextColor(R.color.colorDescricao)
        txtNomeModulo.layoutParams = params

        container.addView(txtNomeModulo)
        builderDialog.setView(container)
        builderDialog.setPositiveButton("Confirmar") { dialogInterface, i ->
            val nome = txtNomeModulo.text.toString()
            if (nome.isEmpty())
                txtNomeModulo.error = "Por favor, entre com o nome do módulo"
            else {
                val modulo = Modulo(UUID.randomUUID().toString(),cursoGlobal.id, nome, 0)
                modulosReferencia(modulo.cursoId).child(modulo.id).setValue(modulo)

                toast("${txtNomeModulo.text} adicionado")
                finish()
                startActivity(Intent(applicationContext, CursoActivity::class.java))

            }
            txtNomeModulo.setText("")
        }

        builderDialog.setNegativeButton("Cancelar", null)
        alertDialog = builderDialog.create()

        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        if (!(auth.equals(cursoGlobal.idCanal))) {

            val dialog = ViewDialog(this)
            dialog.showDialog("Aguarde", "Obtendo dados")

            usuariosReferencia().child(auth).child("matriculados").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val c = ds.getValue(Curso::class.java)
                        if (c?.id == cursoGlobal.id) {
                            inscrito = true
                            break
                        }
                    }

                    if (inscrito) {
                        btnCancelar.visibility = View.VISIBLE
                        btnNovoModulo.visibility = View.GONE
                        btnAdicionar.visibility = View.GONE
                        textPreco.visibility = View.GONE

                        //O curso não poderá ser removido, apenas arquivado | GLADSON
                        //Só se ele for pago | JÚNIOR

                        btnCancelar.setOnClickListener {
                            val deletarDialog = AlertDialog.Builder(this@CursoActivity)
                            deletarDialog.setTitle("Tem certeza que deseja cancelar sua inscrição?")
                            deletarDialog.setMessage("Se você remover da sua lista de cursos, não poderá desfazer")
                            deletarDialog.setPositiveButton("Excluir") { dialogInterface, i ->
                                if(cursoGlobal.preco.equals(0.0)){
                                    inscrito = false
                                    // Remove a referência de usuário no curso
                                    cursosReferencia().child(cursoGlobal.id).child("inscritos").child(auth).removeValue()
                                    // Remove a referência do curso no usuário
                                    usuariosReferencia().child(auth).child("matriculados").child(cursoGlobal.id).removeValue()
                                }else{
                                    //Arquiva o curso
                                }
                            }
                            deletarDialog.setNegativeButton("Cancelar", null)
                            deleteDialog = deletarDialog.create()

                        }

                    } else {
                        btnAdicionar.visibility = View.VISIBLE
                        btnNovoModulo.visibility = View.GONE
                        btnCancelar.visibility = View.GONE
                        textPreco.visibility = View.GONE

                        if (cursoGlobal.preco != 0.0) { //Se o curso não for gratuito
                            toast(cursoGlobal.preco.toString())
                            textPreco.visibility = View.VISIBLE
                            textPreco.text = "Por R$ ${cursoGlobal.preco}"
                        }

                        btnAdicionar.setOnClickListener {
                            inscrito = true
                            // Registra a referência de usuário no curso
                            cursosReferencia().child(cursoGlobal.id).child("inscritos").child(auth).setValue(auth)
                            // Registra a referência do curso no usuário
                            usuariosReferencia().child(auth).child("matriculados").child(cursoGlobal.id).setValue(cursoGlobal)

                            btnAdicionar.visibility = View.GONE
                            btnNovoModulo.visibility = View.GONE
                            btnCancelar.visibility = View.VISIBLE
                            textPreco.visibility = View.GONE
                            toast("'${cursoGlobal.nome}' foi adicionado aos seus cursos")

                            btnCancelar.visibility = View.VISIBLE
                            btnNovoModulo.visibility = View.GONE
                            btnAdicionar.visibility = View.GONE
                            textPreco.visibility = View.GONE
                        }
                    }

                    dialog.hideDialog()
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })


        } else {
            btnNovoModulo.visibility = View.VISIBLE
            btnCancelar.visibility = View.GONE
            btnAdicionar.visibility = View.GONE
            textPreco.visibility = View.GONE

            btnNovoModulo.setOnClickListener {
                alertDialog.show()
                Log.i("MODULO", "TAMANHO: ${listaModulos.size}")
            }
        }

        if (!(auth.equals(cursoGlobal.idCanal))) {
            if (comprado == true) {
                btnNovoModulo.text = "comprado"
            } else btnNovoModulo.text = "R$ ${cursoGlobal.preco}"

        }

        /*
        btnNovoModulo.setOnClickListener {
            if (!(auth.equals(cursoGlobal.idCanal))) {
                // Caixa de dialogo
                var dialogCompraFragment = DialogCompraFragment()
                dialogCompraFragment.isCancelable = false
                var transaction = supportFragmentManager.beginTransaction()

                dialogCompraFragment.show(transaction, "")


                // Registra a referência de usuário no curso
                //cursosReferencia().child(cursoGlobal.nome).child("inscritos").child(auth).setValue(auth)
                // Registra a referência do curso no usuário
                //usuariosReferencia().child(auth).child("matriculados").child(cursoGlobal.id).setValue(cursoGlobal)

                toast("Matrícula realizada com sucesso")
            } else {
                alertDialog.show()
            }
        } */

    }

    /**
     * Método responsável por carregar os métodos.
     */
    private fun carregarModulos() {
        listaModulos.clear()
        modulosReferencia(cursoGlobal.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (ds in dataSnapshot.children) {
                        val modulo = ds.getValue(Modulo::class.java)

                        if (modulo != null) {
                            listaModulos.add(modulo)
                        }

                    }

                }

                recyclerModulo = findViewById(R.id.recyclerModulo)
                recyclerModulo.layoutManager = LinearLayoutManager(this@CursoActivity, LinearLayoutManager.VERTICAL, false)
                val adapterModulo = ModuloAdapter(this@CursoActivity, listaModulos)
                recyclerModulo.adapter = adapterModulo
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Botão adicional na ToolBar
        when (item.itemId) {
            //Método para matar a activity e não deixa-lá indexada na pilhagem | PAULO
            //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            android.R.id.home -> finish()
        }

        return true
    }

}