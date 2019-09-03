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
import de.hdodenhof.circleimageview.CircleImageView

import java.util.ArrayList

class MeusCursosAdapter(private val context: Context, private val listCursos: ArrayList<Curso>) : RecyclerView.Adapter<MeusCursosAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_meus_cursos, viewGroup, false)

        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        var (id, nome, descricao, thumb, disciplina) = listCursos[i]

        if (!thumb.isEmpty()) {
            Glide.with(context)
                    .load(listCursos[i].thumbUrl)
                    .centerCrop()
                    .into(myViewHolder.thumbCurso)
        }
        myViewHolder.textNome.text = nome
        myViewHolder.textDescricao.text = descricao

    }

    override fun getItemCount(): Int {
        return listCursos.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbCurso: ImageView
        var textNome: TextView
        var textDescricao: TextView
        var btnAbrir: Button

        init {
            thumbCurso = itemView.findViewById(R.id.imageMeusCursosThumb)
            textNome = itemView.findViewById(R.id.textMeusCursosNome)
            textDescricao = itemView.findViewById(R.id.textMeusCursosDescricao)
            btnAbrir = itemView.findViewById(R.id.buttonAbrirCurso)
        }
    }
}
