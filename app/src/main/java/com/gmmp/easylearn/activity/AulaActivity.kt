package com.gmmp.easylearn.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.gmmp.easylearn.activity.NavegacaoActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.ComentarioAdapter
import com.gmmp.easylearn.helper.*
import com.gmmp.easylearn.model.Comentario
import com.gmmp.easylearn.model.Modulo
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.activity_aula.*
import org.jetbrains.anko.padding
import org.jetbrains.anko.toast
import java.util.*


class AulaActivity : AppCompatActivity() {

    var usuarioLogado = Usuario()
    val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var txtComentario: EditText
    private lateinit var alertDialog: AlertDialog
    private lateinit var recyclerComentario : RecyclerView
    private lateinit var adapterComentario : ComentarioAdapter
    private lateinit var adapter: ComentarioAdapter
    private val listaComentarios = ArrayList<Comentario>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aula)

        iniciarBotaoNovoComentario()

        supportActionBar?.hide()

        txt_nomeAula.text = NavegacaoActivity.videoGlobal.nome
        txt_nomeCurso.text = NavegacaoActivity.cursoGlobal.nome

        carregarComentarios()


        videoView.setVideoURI(Uri.parse(NavegacaoActivity.videoGlobal.midiaUrl))
        videoView.setMediaController(mediaControler)
        videoView.setVideoViewCallback(object : UniversalVideoView.VideoViewCallback {
            override fun onBufferingStart(mediaPlayer: MediaPlayer?) {

            }

            override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {

            }

            override fun onPause(mediaPlayer: MediaPlayer?) {

            }

            override fun onScaleChange(isFullscreen: Boolean) {
                if (isFullscreen) {
                    val layoutParams = videoLayout.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    videoLayout.layoutParams = layoutParams
                    scrollConteudo.visibility = View.GONE
                } else {
                    val layoutParams = videoLayout.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = 200
                    videoLayout.layoutParams = layoutParams
                    scrollConteudo.visibility = View.VISIBLE
                }
            }

            override fun onStart(mediaPlayer: MediaPlayer?) {

            }

        })

    }

    @SuppressLint("ResourceAsColor")
    private fun iniciarBotaoNovoComentario() {
        //Btn novo modulo
        val builderDialog = AlertDialog.Builder(this)
        builderDialog.setTitle("Novo comentário")

        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.leftMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        params.topMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)

        txtComentario = EditText(this)
        txtComentario.hint = "Escreva o comentário"
        txtComentario.padding = 16
        txtComentario.setBackgroundResource(R.color.colorEditText)
        txtComentario.setTextColor(R.color.colorDescricao)
        txtComentario.layoutParams = params

        container.addView(txtComentario)
        builderDialog.setView(container)
        builderDialog.setPositiveButton("Confirmar") { dialogInterface, i ->
            val com = txtComentario.text.toString()
            if (com.isEmpty())
                txtComentario.error = "Por favor, escreva um comentário"
            else {
                val comentario = Comentario(UUID.randomUUID().toString(), NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id, NavegacaoActivity.videoGlobal.id, com)
                comentariosReferencia(comentario.cursoId, comentario.moduloId, comentario.videoId).child(comentario.id).setValue(comentario)

                toast("Comentário adicionado")
                finish()
                startActivity(Intent(applicationContext, AulaActivity::class.java))

            }
            txtComentario.setText("")
        }

        builderDialog.setNegativeButton("Cancelar", null)

        alertDialog = builderDialog.create()

        btnNovoComentario.setOnClickListener {
            alertDialog.show()
            Log.i("Comentario", "TAMANHO: ${listaComentarios.size}")
        }

    }

    override fun onPause() {
        super.onPause()
        iniciar()

    }

    fun iniciar() {


        verificacaoDeCurtida()

        btn_gostei.setOnClickListener {
            gostei()
        }

        btn_naoGostei.setOnClickListener {
            naoGostei()
        }
    }

    fun verificacaoDeCurtida() {

        val auth = FirebaseAuth.getInstance().currentUser
        val gosteiReferencia = cursosReferencia().child(NavegacaoActivity.cursoGlobal.id).child("reacao").child("gostei").child(auth!!.uid)
        val naoGosteiReferencia = cursosReferencia().child(NavegacaoActivity.cursoGlobal.id).child("reacao").child("naoGostei").child(auth!!.uid)

        // verifica o usuário logado
        usuariosReferencia().child(auth.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usuarioLogado = dataSnapshot.getValue(Usuario::class.java)!!
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        // verificação de reação
        gosteiReferencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {
                    img_like.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))

                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        naoGosteiReferencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                if (u != null) {
                    img_dislike.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun gostei() {
        // troca de cor
        img_like.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))
        img_dislike.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorInativo)))

        // remove o dislike
        cursosReferencia().child(NavegacaoActivity.cursoGlobal.id).child("reacao").child("naoGostei").child(auth!!.uid).removeValue()

        // salva como gostei
        cursosReferencia().child(NavegacaoActivity.cursoGlobal.id).child("reacao").child("gostei").child(auth.uid).setValue(usuarioLogado)
    }

    fun naoGostei() {
        // troca de cor
        img_dislike.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorPrimary)))
        img_like.setColorFilter((ContextCompat.getColor(this@AulaActivity, R.color.colorInativo)))

        // remove do like
        cursosReferencia().child(NavegacaoActivity.cursoGlobal.id).child("reacao").child("gostei").child(auth!!.uid).removeValue()
        // salva o dislike
        cursosReferencia().child(NavegacaoActivity.cursoGlobal.id).child("reacao").child("naoGostei").child(auth.uid).setValue(usuarioLogado)

    }

    private fun carregarComentarios() {
        listaComentarios.clear()
        comentariosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id, NavegacaoActivity.videoGlobal.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (ds in dataSnapshot.children) {
                        val comentario = ds.getValue(Comentario::class.java)

                        if (comentario != null) {
                            listaComentarios.add(comentario)
                        }

                    }

                }


                recyclerComentario = findViewById(R.id.recyclerComentarios)
                recyclerComentario.layoutManager = LinearLayoutManager(this@AulaActivity, LinearLayoutManager.VERTICAL, false)
                adapterComentario = ComentarioAdapter(this@AulaActivity, listaComentarios)
                recyclerComentario.adapter = adapterComentario
                recyclerComentario.visibility = View.VISIBLE
                ativarSlide()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
    private fun ativarSlide() {
        object : SwipeHelper(this, recyclerComentario) {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder, underlayButtons: MutableList<SwipeHelper.UnderlayButton>) {
                val i = viewHolder.adapterPosition
                underlayButtons.add(SwipeHelper.UnderlayButton(
                        "Excluir",
                        0,
                        Color.parseColor("#FF3C30"),
                        UnderlayButtonClickListener {
                            val itemSelected = listaComentarios.get(i)
                            val builder = AlertDialog.Builder(this@AulaActivity)
                            builder.setTitle("Tem certeza de que deseja excluir este comentário?")

                            // Set a positive button and its click listener on alert dialog
                            builder.setPositiveButton("Sim") { dialog, which ->
                                adapter.removeItem(i)
                                comentariosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id, NavegacaoActivity.videoGlobal.id).child(itemSelected.id).removeValue()
                            }

                            builder.setNegativeButton("Cancelar") { _, _ ->
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()

                        },
                        this@AulaActivity
                ))

                underlayButtons.add(SwipeHelper.UnderlayButton(
                        "Editar",
                        0,
                        Color.parseColor("#FF9502"),
                        UnderlayButtonClickListener {
                            val builderDialog = AlertDialog.Builder(this@AulaActivity)
                            val itemSelected = listaComentarios.get(i)
                            builderDialog.setTitle("Editar Comentário")

                            val linearLayout = LinearLayout(this@AulaActivity)
                            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                            params.marginStart = 20
                            params.marginEnd = 20
                            params.topMargin = 20

                            linearLayout.setLayoutParams(params)
                            linearLayout.setOrientation(LinearLayout.VERTICAL)
                            linearLayout.setPadding(20, 10, 20, 10)

                            txtComentario = EditText(this@AulaActivity)
                            txtComentario.layoutParams = params
                            txtComentario.hint = "Comentário"
                            txtComentario.padding = 16
                            txtComentario.setBackgroundResource(com.gmmp.easylearn.R.color.colorEditText)
                            txtComentario.setTextColor(resources.getColor(R.color.colorDescricao))
                            txtComentario.setText(itemSelected.comentario)


                            linearLayout.addView(txtComentario)


                            builderDialog.setView(linearLayout)
                            builderDialog.setPositiveButton("Confirmar") { dialogInterface, i ->
                                val com= txtComentario.text.toString()

                                comentariosReferencia(NavegacaoActivity.cursoGlobal.id, NavegacaoActivity.moduloGlobal.id, NavegacaoActivity.videoGlobal.id).child(itemSelected.id).child("comentario").setValue(com)
                            }
                            builderDialog.setNegativeButton("Cancelar", null)
                            alertDialog = builderDialog.create()
                            alertDialog.show()


                        },
                        this@AulaActivity
                ))
            }
        }
    }

}
