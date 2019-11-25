package com.gmmp.easylearn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Cupom

import java.util.ArrayList

class CupomAdapter(private val listCupom: ArrayList<Cupom>) : RecyclerView.Adapter<CupomAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_cupom, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val cupom = listCupom.get(i)

        viewHolder.codigo.setText(cupom.codigo)
        viewHolder.valor.setText(cupom.valor.toString())
        if (cupom.validade.equals("")){
            viewHolder.validade.setText("Sem prazo de validade")
        }else {
            viewHolder.validade.setText(cupom.validade)
        }
    }

    override fun getItemCount(): Int {
        return listCupom.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var codigo: TextView
        internal var valor: TextView
        internal var validade: TextView

        init {
            codigo = itemView.findViewById(R.id.txt_codigoCupom)
            valor = itemView.findViewById(R.id.txt_valorCupom)
            validade = itemView.findViewById(R.id.txt_validade)
        }

    }
}
