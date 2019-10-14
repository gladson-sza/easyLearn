package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.gmmp.easylearn.helper.*
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Modulo
import com.gmmp.easylearn.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import iammert.com.expandablelib.ExpandableLayout
import iammert.com.expandablelib.Section
import kotlinx.android.synthetic.main.activity_modulo.*
import kotlinx.android.synthetic.main.layout_child.view.*
import kotlinx.android.synthetic.main.layout_parent.view.*
import org.jetbrains.anko.toast
import java.util.*

class ModuloActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var deleteDialog: AlertDialog
    private lateinit var txtNomeModulo: EditText
    private lateinit var layout: ExpandableLayout
    private var inscrito = false

    private val listaModulos = ArrayList<Modulo>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo)

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

        params.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
        params.topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)

        txtNomeModulo = EditText(this)
        txtNomeModulo.hint = "Nome do módulo"
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
                val modulo = Modulo(nome)
                modulosReferencia(cursoGlobal.nome).child(nome).setValue(modulo)

                toast("Módulo adicionado")
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
                            val deletarDialog = AlertDialog.Builder(this@ModuloActivity)
                            deletarDialog.setTitle("Tem certeza que deseja cancelar sua inscrição?")
                            deletarDialog.setMessage("Se você remover da sua lista de cursos, não poderá desfazer")
                            deletarDialog.setView(container)
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

        // Lista de modulos
        layout = findViewById(R.id.expandable)

        layout.setRenderer(object : ExpandableLayout.Renderer<Modulo, Video> {

            // Modulo
            override fun renderParent(view: View, model: Modulo, isExpanded: Boolean, parentPosition: Int) {
                view.tv_parent_name.text = "Seção ${(parentPosition + 1)} ${model.nome}"

                if (isExpanded) view.arrow.setBackgroundResource(R.drawable.ic_seta_para_cima)
                else view.arrow.setBackgroundResource(R.drawable.ic_seta_para_baixo)

            }


            // Video do modulo
            override fun renderChild(view: View, model: Video, parentPosition: Int, childPosition: Int) {
                if (childPosition == 0 && auth.equals(cursoGlobal.idCanal)) {
                    view.tv_child_name.setTypeface(null, Typeface.BOLD)
                    view.tv_child_name.text = "Adicionar vídeo"
                    view.txt_video_duracao.text = " "

                    view.tv_child_name.setOnClickListener {
                        val intent = Intent(applicationContext, NovoVideoActivity::class.java)
                        intent.putExtra("modulo", listaModulos[parentPosition].nome)
                        intent.putExtra("curso", cursoGlobal.nome)
                        startActivity(intent)

                        finish()
                    }

                } else {
                    view.txt_video_duracao.text = model.duracao
                    view.tv_child_name.text = model.nome
                    view.tv_child_name.setOnClickListener {
                        startActivity(Intent(applicationContext, AulaActivity::class.java))
                    }
                }
            }
        })

    }

    /**
     * Método responsável por carregar os métodos.
     */
    private fun carregarModulos() {

        listaModulos.clear()

        modulosReferencia(cursoGlobal.nome).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (ds in dataSnapshot.children) {
                        val modulo = ds.getValue(Modulo::class.java)

                        if (modulo != null) {
                            listaModulos.add(modulo)
                            setSection(modulo)
                        }

                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun setSection(modulo: Modulo) {

        videosReferencia(cursoGlobal.nome, modulo.nome).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Primeiro vídeo obrigatório para que seja substituído pelo "Adicionar Vídeo" | GLADSON
                val videos = ArrayList<Video>()
                videos.add(Video())

                if (dataSnapshot.exists()) {
                    for (ds in dataSnapshot.children) {
                        val video = ds.getValue(Video::class.java)

                        if (video != null) {
                            videos.add(video)
                        }
                    }
                }

                val section = Section<Modulo, Video>()
                section.parent = modulo
                section.children.addAll(videos)
                layout.addSection(section)

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