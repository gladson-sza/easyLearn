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
import com.gmmp.easylearn.model.Usuario

import java.util.ArrayList

class CanaisAdapter(private val context: Context, private val listCanal: ArrayList<Usuario>) : RecyclerView.Adapter<CanaisAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_horizontal_usuario, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.nome.text = listCanal[i].nome


        if (!listCanal[i].urlPerfil.isEmpty()) {
            Glide.with(context)
                    .load(listCanal[i].urlPerfil)
                    .centerCrop()
                    .into(viewHolder.perfil)
        }

        if (!listCanal[i].urlWallpaper.isEmpty()) {
            Glide.with(context)
                    .load(listCanal[i].urlWallpaper)
                    .centerCrop()
                    .into(viewHolder.fundo)
        }


    }

    override fun getItemCount(): Int {
        return listCanal.size
    }

    override fun onClick(v: View) {
        //Abre a página do vídeo
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var perfil: ImageView
        internal var fundo: ImageView
        internal var nome: TextView

        init {
            perfil = itemView.findViewById(R.id.imgPerfil)
            fundo = itemView.findViewById(R.id.imageFundo)
            nome = itemView.findViewById(R.id.text_nome)
        }

    }
}
