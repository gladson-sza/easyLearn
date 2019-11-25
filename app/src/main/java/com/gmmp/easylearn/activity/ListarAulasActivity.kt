package com.gmmp.easylearn.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.AulaAdapter
import com.gmmp.easylearn.helper.SwipeHelper
import com.gmmp.easylearn.helper.SwipeHelper.UnderlayButtonClickListener
import com.gmmp.easylearn.helper.videosReferencia
import com.gmmp.easylearn.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_listar_aulas.*
import org.jetbrains.anko.padding
import org.jetbrains.anko.startActivity

class ListarAulasActivity : AppCompatActivity() {

    val listaVideo = arrayListOf<Video>()
    private lateinit var recyclerAulas: RecyclerView
    private lateinit var adapter: AulaAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var txtTitulo: EditText
    private lateinit var txtDescricao: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_aulas)

        inicializar()
    }

    fun inicializar() {


        // Verifica que o Usuário é dono do Canal
        val auth = FirebaseAuth.getInstance().currentUser?.uid.toString()
        if (NavegacaoActivity.cursoGlobal.idCanal != auth) btnAdicionarNovaAula.visibility = View.GONE
        else {
            btnAdicionarNovaAula.setOnClickListener {
                startActivity<NovoVideoActivity>(
                        "idCurso" to NavegacaoActivity.cursoGlobal.id,
                        "idModulo" to NavegacaoActivity.moduloGlobal.id)
            }
        }

        // Configurações básicas de exibição
        supportActionBar?.hide()
        toolbarModulo.title = NavegacaoActivity.moduloGlobal.nome

        recyclerAulas = findViewById(R.id.recyclerAulas)
        recyclerAulas.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        adapter = AulaAdapter(listaVideo, applicationContext)
        recyclerAulas.adapter = adapter
        ativarSlide()

        // val dialog = ViewDialog(this)
        // dialog.showDialog("Carregando", "Aguarde, obtendo dados...")
        videosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    listaVideo.clear()
                    for (ds in dataSnapshot.children) {

                        val video = ds.getValue(Video::class.java)

                        if (video != null) {
                            listaVideo.add(video)
                        }

                    }


                    if (listaVideo.size.equals(0)) {
                        nenhumModulo.visibility = View.VISIBLE
                        recyclerAulas.visibility = View.GONE
                    } else {
                        nenhumModulo.visibility = View.GONE
                        recyclerAulas.visibility = View.VISIBLE
                    }
                    // dialog.hideDialog()
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }

        })
    }

    private fun ativarSlide() {
        object : SwipeHelper(this, recyclerAulas) {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder, underlayButtons: MutableList<SwipeHelper.UnderlayButton>) {
                val i = viewHolder.adapterPosition
                underlayButtons.add(SwipeHelper.UnderlayButton(
                        "Excluir",
                        0,
                        Color.parseColor("#FF3C30"),
                        UnderlayButtonClickListener {
                            val itemSelected = listaVideo.get(i)
                            val builder = AlertDialog.Builder(this@ListarAulasActivity)
                            builder.setTitle("Tem certeza de que deseja excluir esta aula?")
                            builder.setMessage(Html.fromHtml("Se excluir, todas as aulas registradas em <b>'${itemSelected.nome}'</b> serão perdidas"))

                            // Set a positive button and its click listener on alert dialog
                            builder.setPositiveButton("Sim") { dialog, which ->
                                adapter.removeItem(i)
                                videosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id).child(itemSelected.id).removeValue()
                            }

                            builder.setNegativeButton("Cancelar") { _, _ ->
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()

                        },
                        this@ListarAulasActivity
                ))

                underlayButtons.add(SwipeHelper.UnderlayButton(
                        "Editar",
                        0,
                        Color.parseColor("#FF9502"),
                        UnderlayButtonClickListener {
                            val builderDialog = AlertDialog.Builder(this@ListarAulasActivity)
                            val itemSelected = listaVideo.get(i)
                            builderDialog.setTitle("Editar Aula")

                            val linearLayout = LinearLayout(this@ListarAulasActivity)
                            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                            params.marginStart = 20
                            params.marginEnd = 20
                            params.topMargin = 20

                            linearLayout.setLayoutParams(params)
                            linearLayout.setOrientation(LinearLayout.VERTICAL)
                            linearLayout.setPadding(20, 10, 20, 10)

                            txtTitulo = EditText(this@ListarAulasActivity)
                            txtTitulo.layoutParams = params
                            txtTitulo.hint = "Nome da Aula"
                            txtTitulo.padding = 16
                            txtTitulo.setBackgroundResource(com.gmmp.easylearn.R.color.colorEditText)
                            txtTitulo.setTextColor(resources.getColor(R.color.colorDescricao))
                            txtTitulo.setText(itemSelected.nome)

                            txtDescricao = EditText(this@ListarAulasActivity)
                            txtDescricao.layoutParams = params
                            txtDescricao.hint = "Descrição da Aula"
                            txtDescricao.padding = 16
                            txtDescricao.setBackgroundResource(R.color.colorEditText)
                            txtDescricao.setTextColor(resources.getColor(R.color.colorDescricao))
                            txtDescricao.setText(itemSelected.descricao)

                            linearLayout.addView(txtTitulo)
                            linearLayout.addView(txtDescricao)

                            builderDialog.setView(linearLayout)
                            builderDialog.setPositiveButton("Confirmar") { dialogInterface, i ->
                                val nome = txtTitulo.text.toString()
                                val descricao = txtDescricao.text.toString()

                                videosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id).child(itemSelected.id).child("nome").setValue(nome)
                                videosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id).child(itemSelected.id).child("descricao").setValue(descricao)
                            }
                            builderDialog.setNegativeButton("Cancelar", null)
                            alertDialog = builderDialog.create()
                            alertDialog.show()


                        },
                        this@ListarAulasActivity
                ))
            }
        }
    }
}

