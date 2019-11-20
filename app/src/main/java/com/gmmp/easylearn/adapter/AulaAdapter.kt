package com.gmmp.easylearn.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.AulaActivity
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.moduloGlobal
import com.gmmp.easylearn.model.Video
import kotlinx.android.synthetic.main.adapter_aulas.view.*
import org.jetbrains.anko.startActivity

class AulaAdapter(private val aulaList: ArrayList<Video>, private val context: Context) : RecyclerView.Adapter<AulaAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_aulas, viewGroup, false)

        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.txtNomeAula.text = aulaList[i].nome
        myViewHolder.txtDuracao.text = aulaList[i].duracao

        myViewHolder.itemView.setOnClickListener {
            context.startActivity<AulaActivity>()
        }
    }

    override fun getItemCount(): Int {
        return aulaList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNomeAula = itemView.txtNome
        val txtDuracao = itemView.txtDuracao

    }
}