package com.gmmp.easylearn.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.CursoActivity
import com.gmmp.easylearn.activity.NavegacaoActivity
import com.gmmp.easylearn.model.Curso
import java.util.*

class VerticalAdapter(private val context: Context, private val listCursos: ArrayList<Curso>) : RecyclerView.Adapter<VerticalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_vertical_video, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val (id, idCanal, titulo, descricao, thumbUrl, disciplina, preco) = listCursos[i]

        viewHolder.textTitulo.text = titulo
        viewHolder.textDescricao.text = descricao
        viewHolder.textCategoria.text = disciplina

        if (!thumbUrl.isEmpty()) {
            Glide.with(context)
                    .load(listCursos[i].thumbUrl)
                    .centerCrop()
                    .into(viewHolder.imageThumbnail)
        }

        var txtBotao = ""
        if (preco != 0.0) {
            val price = preco.toString()
            if (price.substring(price.length - 1).equals("0")) {
                txtBotao = "R$ ${price.substring(0, price.length - 2)},00"
            } else {
                txtBotao = "R$ ${price.substring(0, price.length - 2)},${price.substring(price.length - 1)}0"
            }
        } else {
            txtBotao = "Gratuito"
        }

        viewHolder.buttonPreco.text = txtBotao

        viewHolder.linearLayout.setOnClickListener {
            NavegacaoActivity.cursoGlobal = listCursos[i]
            context.startActivity(Intent(context, CursoActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return listCursos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageThumbnail: ImageView
        var textTitulo: TextView
        var textDescricao: TextView
        var textCategoria: TextView
        var buttonPreco: Button
        var linearLayout: LinearLayout

        init {
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail)
            textTitulo = itemView.findViewById(R.id.textTitulo)
            textDescricao = itemView.findViewById(R.id.textDescricao)
            textCategoria = itemView.findViewById(R.id.textCategoria)
            buttonPreco = itemView.findViewById(R.id.buttonAbrir)
            linearLayout = itemView.findViewById(R.id.linearLayout)
        }

    }
}
