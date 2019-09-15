package com.gmmp.easylearn.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Aula

import java.util.ArrayList

class HorizontalAdapter(private val context: Context, private val listAulas: ArrayList<Aula>) : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_horizontal_video, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val (id, titulo, descricao, thumbUrl, duracao) = listAulas[i]

        viewHolder.textTitulo.text = titulo
        viewHolder.textDescricao.text = descricao

        if (!thumbUrl.isEmpty()) {
            Glide.with(context)
                    .load(listAulas[i].thumbUrl)
                    .centerCrop()
                    .into(viewHolder.imageThumbnail)
        }

        if (!duracao.isEmpty()) {
            viewHolder.textDuracao.text = duracao
        } else {
            viewHolder.textDuracao.visibility = View.GONE
        }

        //Tanto faz clicar no título ou na imagem, vai abrir o vídeo
        viewHolder.textTitulo.setOnClickListener(this)
        viewHolder.imageThumbnail.setOnClickListener(this)
    }

    override fun getItemCount(): Int {
        return listAulas.size
    }

    override fun onClick(v: View) {
        //Abre a página do vídeo
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imageThumbnail: ImageView
        internal var textTitulo: TextView
        internal var textDescricao: TextView
        internal var textDuracao: TextView

        init {
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail)
            textTitulo = itemView.findViewById(R.id.textTitulo)
            textDescricao = itemView.findViewById(R.id.textDescricao)
            textDuracao = itemView.findViewById(R.id.textDuracao)
        }

    }
}
