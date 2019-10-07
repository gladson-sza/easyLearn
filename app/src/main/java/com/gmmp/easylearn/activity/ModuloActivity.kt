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
import android.widget.TextView
import com.bumptech.glide.Glide

import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Modulo
import com.gmmp.easylearn.model.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

import iammert.com.expandablelib.ExpandableLayout
import iammert.com.expandablelib.Section

import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.modulosReferencia
import kotlinx.android.synthetic.main.activity_modulo.*
import kotlinx.android.synthetic.main.layout_child.view.*
import kotlinx.android.synthetic.main.layout_parent.view.*
import org.jetbrains.anko.imageView
import org.jetbrains.anko.toast

class ModuloActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var txtNomeModulo: EditText
    private lateinit var layout: ExpandableLayout

    private val listaModulos = ArrayList<Modulo>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo)

        iniciar()
        carregarModulos()
    }

    @SuppressLint("ResourceAsColor")
    private fun iniciar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar?.setHomeButtonEnabled(true)      //Ativar o botão
        supportActionBar?.title = cursoGlobal.nome

        Glide.with(applicationContext)
                .load(cursoGlobal.thumbUrl)
                .centerCrop()
                .into(imageThumbCurso.imageView())

        imageThumbCurso.title = cursoGlobal.nome

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

        btnNovoModulo.setOnClickListener { alertDialog.show() }

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
                if (childPosition == 0) {
                    view.tv_child_name.setTypeface(null, Typeface.BOLD)
                    view.tv_child_name.text = "Adicionar vídeo"
                    view.txt_video_duracao.text = " "

                    view.tv_child_name.setOnClickListener {
                        val intent = Intent(applicationContext, NovoVideoActivity::class.java)
                        intent.putExtra("modulo", listaModulos[parentPosition].nome)
                        intent.putExtra("curso", cursoGlobal.nome)
                        startActivity(intent)
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

        modulosReferencia(cursoGlobal.nome).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    listaModulos.clear()

                    for (ds in dataSnapshot.children) {
                        val modulo = ds.getValue(Modulo::class.java)

                        if (modulo != null) {
                            listaModulos.add(modulo)
                            layout.addSection(setSection(modulo))
                        }

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun setSection(modulo: Modulo?): Section<Modulo, Video> {

        val section = Section<Modulo, Video>()

        val videos = ArrayList<Video>()

        // Primeiro vídeo obrigatório para que seja substituído pelo "Adicionar Vídeo" | GLADSON
        videos.add(Video())

        section.parent = modulo
        section.children.addAll(videos)
        return section
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