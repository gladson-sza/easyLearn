package com.gmmp.easylearn.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.ModuloActivity
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Modulo
import java.util.ArrayList
//
//class ModuloAdapter (private val context: Context, private val listModulos: ArrayList<Modulo>) : RecyclerView.Adapter<ModuloAdapter.MyViewHolder>() {
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
//        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_modulos, viewGroup, false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
//        var (titulo, aulas) = listModulos[i]
//
//        myViewHolder.textNome.text = nome
//        myViewHolder.textDescricao.text = descricao
//        myViewHolder.btnAbrir.setOnClickListener {
//            context.startActivity(Intent(context, ModuloActivity::class.java))
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return listCursos.size
//    }
//
//    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        var numero: textView
//        var titulo: TextView
//        var nomeVideo: TextView
//        var duracaoVideo : TextView
//
//        init {
//            numero = itemView.findViewById(R.id.textViewNumeroModulo)
//            titulo = itemView.findViewById(R.id.textViewTituloModulo)
//            nomeVideo = itemView.findViewById(R.id.textViewNomeVideo)
//            duracaoVideo = itemView.findViewById(R.id.textDuracao)
//        }
//
//    }
//}