package com.gmmp.easylearn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.CanalAlheioActivity
import com.gmmp.easylearn.activity.NavegacaoActivity
import com.gmmp.easylearn.model.Usuario
import org.jetbrains.anko.startActivity

import java.util.ArrayList

class CanaisAdapter(private val context: Context, private val listCanal: ArrayList<Usuario>) : RecyclerView.Adapter<CanaisAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_horizontal_usuario, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.nome.text = listCanal[i].nome

        if (listCanal[i].urlWallpaper.isNotEmpty()) {
            Glide.with(context)
                    .load(listCanal[i].urlWallpaper)
                    .centerCrop()
                    .into(viewHolder.fundo)
        }

        viewHolder.fundo.setOnClickListener {
            NavegacaoActivity.canalAlheioGlobal = listCanal[i]
            context.startActivity<CanalAlheioActivity>()
        }

    }

    override fun getItemCount(): Int {
        return listCanal.size
    }

    override fun onClick(v: View) {
        //Abre a página do vídeo
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var fundo: ImageView
        internal var nome: TextView

        init {
            fundo = itemView.findViewById(R.id.imageFundo)
            nome = itemView.findViewById(R.id.text_nome)
        }

    }
}
