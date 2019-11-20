package com.gmmp.easylearn.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.CursoActivity
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.model.Curso
import org.jetbrains.anko.startActivity


import java.util.ArrayList


class CursosAdapter(private val context: Context, private val listCursos: ArrayList<Curso>) : RecyclerView.Adapter<CursosAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_cursos, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        if (!listCursos[i].thumbUrl.isEmpty()) {
            Glide.with(context)
                    .load(listCursos[i].thumbUrl)
                    .centerCrop()
                    .into(myViewHolder.thumbCurso)
        }

        myViewHolder.textNome.text = listCursos[i].nome
        myViewHolder.textDescricao.text = listCursos[i].descricao
        myViewHolder.textCategoria.text = listCursos[i].disciplina

        myViewHolder.linearLayout.setOnClickListener {
            cursoGlobal = listCursos[i]
            context.startActivity<CursoActivity>()
        }
    }

    override fun getItemCount(): Int {
        return listCursos.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbCurso: ImageView
        var textNome: TextView
        var textDescricao: TextView
        var textCategoria: TextView
        var linearLayout : LinearLayout

        init {
            thumbCurso = itemView.findViewById(R.id.imageThumbnail)
            textNome = itemView.findViewById(R.id.textTitulo)
            textDescricao = itemView.findViewById(R.id.textDescricao)
            textCategoria = itemView.findViewById(R.id.textCategoria)
            linearLayout = itemView.findViewById(R.id.linearLayout)
        }

    }
}
