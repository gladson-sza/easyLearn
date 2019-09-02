package com.gmmp.easylearn.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Curso

import java.util.ArrayList

class VerticalAdapter(private val context: Context, private val listCursos: ArrayList<Curso>) : RecyclerView.Adapter<VerticalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_vertical_video, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val (id, titulo, descricao, thumbUrl, preco) = listCursos[i]

        viewHolder.textTitulo.text = titulo
        viewHolder.textDescricao.text = descricao

        if (!thumbUrl.isEmpty()) {
            Glide.with(context)
                    .load(listCursos[i].thumbUrl)
                    .centerCrop()
                    .into(viewHolder.imageThumbnail)
        }

        if (!preco.isEmpty()) {
            viewHolder.buttonPreco.text = preco
        } else {
            viewHolder.buttonPreco.text = "Gratuito"
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
        var buttonPreco : Button

        init {
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail)
            textTitulo = itemView.findViewById(R.id.textTitulo)
            textDescricao = itemView.findViewById(R.id.textDescricao)
            textCategoria = itemView.findViewById(R.id.textCategoria)
            buttonPreco = itemView.findViewById(R.id.buttonAbrir)
        }

    }
}
