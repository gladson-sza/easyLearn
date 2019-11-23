package com.gmmp.easylearn.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Curso

import java.util.ArrayList

class HorizontalCursosAdapter(private val context: Context, private val listCursos: ArrayList<Curso>) : RecyclerView.Adapter<HorizontalCursosAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_horizontal_curso, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {


        viewHolder.textTitulo.text = listCursos[i].nome
        viewHolder.textDescricao.text = listCursos[i].descricao

        if (!listCursos[i].thumbUrl.isEmpty()) {
            Glide.with(context)
                    .load(listCursos[i].thumbUrl)
                    .centerCrop()
                    .into(viewHolder.imageThumbnail)
        }
    }

    override fun getItemCount(): Int {
        return listCursos.size
    }

    override fun onClick(v: View) {
        //Abre a página do vídeo
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imageThumbnail: ImageView
        internal var textTitulo: TextView
        internal var textDescricao: TextView

        init {
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail1)
            textTitulo = itemView.findViewById(R.id.textTitulo1)
            textDescricao = itemView.findViewById(R.id.textDescricao1)
        }

    }
}
