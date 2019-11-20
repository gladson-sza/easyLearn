package com.gmmp.easylearn.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.TodosCursosActivity
import com.gmmp.easylearn.helper.listarPor
import com.gmmp.easylearn.model.Disciplina
import com.gmmp.easylearn.model.Modulo
import de.hdodenhof.circleimageview.CircleImageView

class ModuloAdapter(private val context: Context, private val modulos: ArrayList<Modulo>) : RecyclerView.Adapter<ModuloAdapter.MyViewHolder>() {

    fun removeItem(position: Int) {
        modulos.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, modulos.size)
    }

    fun restoreItem(modulo: Modulo, position: Int) {
        modulos.add(position, modulo)
        // notify item added by position
        notifyItemInserted(position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_modulo, viewGroup, false)
        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val (id , cursoId, nome, qtdAulas) = modulos[i]

        myViewHolder.txt_nomeModulos.text = nome
        myViewHolder.txt_qtdAulas.text = "$qtdAulas aulas"
    }

    override fun getItemCount(): Int {
        return modulos.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_nomeModulos: TextView
        val txt_qtdAulas: TextView

        init {
            txt_nomeModulos = itemView.findViewById(R.id.txt_nomeModulo)
            txt_qtdAulas = itemView.findViewById(R.id.txt_qtdAulas)
        }
    }
}