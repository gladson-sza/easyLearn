package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.ModuloAdapter
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.*
import com.gmmp.easylearn.model.Cartao
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Modulo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_curso.*
import kotlinx.android.synthetic.main.activity_curso.descricaoCurso
import kotlinx.android.synthetic.main.dialog_pagamento.view.*
import kotlinx.android.synthetic.main.dialog_pagamento.view.txt_ultimoDigito
import org.jetbrains.anko.padding
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class CursoActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var deleteDialog: AlertDialog
    private lateinit var txtNomeModulo: EditText
    private lateinit var recyclerModulo : RecyclerView
    private lateinit var adapterModulo : ModuloAdapter
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

        val curso = Curso()
        curso.id = intent.getStringExtra("cursoId")
        curso.idCanal = intent.getStringExtra("cursoIdCanal")
        curso.preco = intent.getDoubleExtra("cursoPreco", 0.0)
        curso.thumbUrl = intent.getStringExtra("cursoThumbUrl")
        curso.disciplina = intent.getStringExtra("cursoDisciplina")
        curso.nome = intent.getStringExtra("cursoNome")
        curso.descricao = intent.getStringExtra("cursoDescricao")

        supportActionBar?.hide()
        Glide.with(applicationContext)
                .load(intent.getStringExtra("cursoThumbUrl"))
                .centerCrop()
                .into(imageCurso)

        tituloCurso.text = intent.getStringExtra("cursoNome")
        descricaoCurso.text = intent.getStringExtra("cursoDescricao")

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
                val modulo = Modulo(UUID.randomUUID().toString(), intent.getStringExtra("cursoId"), nome, 0)
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
        if (!(auth.equals(intent.getStringExtra("cursoIdCanal")))) {

            val dialog = ViewDialog(this)
            dialog.showDialog("Aguarde", "Obtendo dados")

            usuariosReferencia().child(auth).child("matriculados").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val c = ds.getValue(Curso::class.java)
                        if (c?.id == intent.getStringExtra("cursoId")) {
                            inscrito = true
                            break
                        }
                    }

                    if (inscrito) {
                        btnCancelar.visibility = View.VISIBLE
                        btnNovoModulo.visibility = View.GONE
                        btnAdicionar.visibility = View.GONE
                        textPreco.visibility = View.GONE

                        btnCancelar.setOnClickListener {
                            val deletarDialog = AlertDialog.Builder(this@CursoActivity)
                            deletarDialog.setTitle("Tem certeza que deseja cancelar sua inscrição?")
                            deletarDialog.setMessage(Html.fromHtml("Se você remover <b>'${intent.getStringExtra("cursoNome")}'</b> da sua lista de cursos, não será possível recuperar"))
                            deletarDialog.setPositiveButton("Excluir") { dialogInterface, i ->
                                if (intent.getDoubleExtra("cursoPreco", 0.0).equals(0.0)) {
                                    inscrito = false
                                    // Remove a referência de usuário no curso
                                    cursosReferencia().child(intent.getStringExtra("cursoId")).child("inscritos").child(auth).removeValue()
                                    // Remove a referência do curso no usuário
                                    usuariosReferencia().child(auth).child("matriculados").child(intent.getStringExtra("cursoId")).removeValue()
                                } else {
                                    //Arquiva o curso
                                    //POR ENQUANTO SÓ PRA TESTAR O NGC DO PAGAMENTO E TAL

                                    // Remove a referência de usuário no curso
                                    cursosReferencia().child(intent.getStringExtra("cursoId")).child("inscritos").child(auth).removeValue()
                                    // Remove a referência do curso no usuário
                                    usuariosReferencia().child(auth).child("matriculados").child(intent.getStringExtra("cursoId")).removeValue()
                                }
                            }
                            deletarDialog.setNegativeButton("Cancelar", null)
                            deleteDialog = deletarDialog.create()
                            deleteDialog.show()
                        }

                    } else {
                        btnAdicionar.visibility = View.VISIBLE
                        btnNovoModulo.visibility = View.GONE
                        btnCancelar.visibility = View.GONE
                        textPreco.visibility = View.GONE

                        if (intent.getDoubleExtra("cursoPreco", 0.0) != 0.0) { //Se o curso não for gratuito
                            textPreco.visibility = View.VISIBLE
                            textPreco.text = "Por R$ ${intent.getDoubleExtra("cursoPreco", 0.0)}"
                        }

                        btnAdicionar.setOnClickListener {
                            if (intent.getDoubleExtra("cursoPreco", 0.0) != 0.0) {
                                val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
                                var cartao : Cartao
                                Log.i("TESTE", usuarioId)

                                usuariosReferencia().child(usuarioId).child("cartao").addValueEventListener(object : ValueEventListener{
                                    override fun onCancelled(p0: DatabaseError) {
                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            val c = dataSnapshot.getValue(Cartao::class.java)
                                            cartao = Cartao(c)
                                            Log.i("TESTE", "nome ${cartao.nomeCartao}")

                                            val mDialogView = LayoutInflater.from(this@CursoActivity).inflate(R.layout.dialog_pagamento, null)
                                            val mBuilder = AlertDialog.Builder(this@CursoActivity)
                                                    .setView(mDialogView)
                                            val  mAlertDialog = mBuilder.show()

                                            val ultimosDigitos = cartao.numeroCartao.substring(cartao.numeroCartao.length-4)
                                            Log.i("TESTE", "cartao: ${cartao.numeroCartao}")
                                            Log.i("TESTE", "ud: $ultimosDigitos")

                                            val preco = intent.getDoubleExtra("cursoPreco", 0.0).toString()
                                            mDialogView.txt_precoCurso.text = "R$ ${preco.substring(0, preco.length-2)},00"
                                            mDialogView.txt_ultimoDigito.text = ultimosDigitos
                                            mDialogView.txt_cancelar.setOnClickListener{mAlertDialog.dismiss()}

                                            mDialogView.btn_finalizarCompra.setOnClickListener {
                                                if(cartao.saldo >= intent.getDoubleExtra("cursoPreco", 0.0)){
                                                    //Desconta o preço no saldo
                                                    usuariosReferencia().child(auth).child("cartao").child("saldo").setValue(cartao.saldo-intent.getDoubleExtra("cursoPreco", 0.0))

                                                    inscrito = true
                                                    // Registra a referência de usuário no curso
                                                    cursosReferencia().child(intent.getStringExtra("cursoId")).child("inscritos").child(auth).setValue(auth)
                                                    // Registra a referência do curso no usuário

                                                    usuariosReferencia().child(auth).child("matriculados").child(intent.getStringExtra("cursoId")).setValue(curso)

                                                    toast("'${intent.getStringExtra("cursoNome")}' foi adicionado aos seus cursos")

                                                    btnCancelar.visibility = View.VISIBLE
                                                    btnNovoModulo.visibility = View.GONE
                                                    btnAdicionar.visibility = View.GONE
                                                    textPreco.visibility = View.GONE
                                                }else{
                                                    val builder = AlertDialog.Builder(this@CursoActivity)
                                                    builder.setTitle("Você não possui saldo suficiente para realizar esta compra")
                                                    builder.setPositiveButton("Entendi"){dialog, which ->
                                                        startActivity(Intent(this@CursoActivity, PagamentoActivity::class.java))
                                                    }
                                                    builder.setNegativeButton("Cancelar"){_,_ -> }
                                                    builder.show()
                                                }
                                                mAlertDialog.dismiss()
                                            }

                                        }else{ //Se não possui
                                            val builder = AlertDialog.Builder(this@CursoActivity)
                                            builder.setTitle("Nenhuma forma de pagamento foi encontrada!")
                                            builder.setMessage("Para adquirir cursos pagos, você deve adicionar uma forma de pagamento")
                                            builder.setPositiveButton("Entendi"){dialog, which ->
                                                startActivity(Intent(this@CursoActivity, PagamentoActivity::class.java))

                                            }
                                            builder.setNegativeButton("Cancelar"){_,_ -> }
                                            builder.show()
                                        }
                                    }
                                })

                            }else {
                                inscrito = true
                                // Registra a referência de usuário no curso
                                cursosReferencia().child(intent.getStringExtra("cursoId")).child("inscritos").child(auth).setValue(auth)
                                // Registra a referência do curso no usuário
                                usuariosReferencia().child(auth).child("matriculados").child(intent.getStringExtra("cursoId")).setValue(curso)

                                btnAdicionar.visibility = View.GONE
                                btnNovoModulo.visibility = View.GONE
                                btnCancelar.visibility = View.VISIBLE
                                textPreco.visibility = View.GONE
                                toast("'${intent.getStringExtra("cursoNome")}' foi adicionado aos seus cursos")

                                btnCancelar.visibility = View.VISIBLE
                                btnNovoModulo.visibility = View.GONE
                                btnAdicionar.visibility = View.GONE
                                textPreco.visibility = View.GONE
                            }
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

        if (!(auth.equals(intent.getStringExtra("cursoIdCanal")))) {
            if (comprado == true) {
                btnNovoModulo.text = "comprado"
            } else btnNovoModulo.text = "R$ ${intent.getDoubleExtra("cursoPreco", 0.0)}"

        }

    }

    /**
     * Método responsável por carregar os métodos.
     */
    private fun carregarModulos() {
        listaModulos.clear()
        modulosReferencia(intent.getStringExtra("cursoId")).addValueEventListener(object : ValueEventListener {
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
                adapterModulo = ModuloAdapter(this@CursoActivity, listaModulos)
                recyclerModulo.adapter = adapterModulo
                ativarSlide()

                if(listaModulos.size.equals(0)) {
                    nenhumModulo.visibility = View.VISIBLE
                    recyclerModulo.visibility = View.GONE
                }else {
                    nenhumModulo.visibility = View.GONE
                    recyclerModulo.visibility = View.VISIBLE
                }
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


    private fun ativarSlide() {
        object : SwipeHelper(this, recyclerModulo) {
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder, underlayButtons: MutableList<SwipeHelper.UnderlayButton>) {
                val i = viewHolder.adapterPosition
                underlayButtons.add(SwipeHelper.UnderlayButton(
                        "Excluir",
                        0,
                        Color.parseColor("#FF3C30"),
                        UnderlayButtonClickListener {
                            val itemSelected = listaModulos.get(i)
                            val builder = AlertDialog.Builder(this@CursoActivity)
                            builder.setTitle("Tem certeza de que deseja excluir este modulo?")
                            builder.setMessage(Html.fromHtml("Se excluir, todas as aulas registradas em <b>'${itemSelected.nome}'</b> serão perdidas"))

                            // Set a positive button and its click listener on alert dialog
                            builder.setPositiveButton("Sim"){dialog, which ->
                                adapterModulo.removeItem(i)
                                modulosReferencia(itemSelected.cursoId).child(itemSelected.id).removeValue()
                            }

                            builder.setNegativeButton("Cancelar"){_,_ ->
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()

                        },
                        this@CursoActivity
                ))

                underlayButtons.add(SwipeHelper.UnderlayButton(
                        "Editar",
                        0,
                        Color.parseColor("#FF9502"),
                        UnderlayButtonClickListener {
                            val builderDialog = AlertDialog.Builder(this@CursoActivity)
                            val itemSelected = listaModulos.get(i)
                            builderDialog.setTitle("Editar módulo")

                            val container = FrameLayout(this@CursoActivity)
                            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                            params.leftMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
                            params.rightMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
                            params.topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)

                            txtNomeModulo = EditText(this@CursoActivity)
                            txtNomeModulo.hint = "Nome do módulo"
                            txtNomeModulo.padding = 16
                            txtNomeModulo.setBackgroundResource(R.color.colorEditText)
                            txtNomeModulo.setTextColor(resources.getColor(R.color.colorDescricao))
                            txtNomeModulo.layoutParams = params
                            txtNomeModulo.setText(itemSelected.nome)

                            container.addView(txtNomeModulo)
                            builderDialog.setView(container)
                            builderDialog.setPositiveButton("Confirmar") { dialogInterface, i ->
                                val nome = txtNomeModulo.text.toString()
                                modulosReferencia(itemSelected.cursoId).child(itemSelected.id).child("nome").setValue(nome)
                            }
                            builderDialog.setNegativeButton("Cancelar", null)
                            alertDialog = builderDialog.create()
                            alertDialog.show()


                        },
                        this@CursoActivity
                ))
            }
        }
    }

    fun getCartao (){

    }
}