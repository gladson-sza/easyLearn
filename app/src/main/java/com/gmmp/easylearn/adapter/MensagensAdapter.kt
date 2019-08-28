package com.gmmp.easylearn.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Mensagen

import de.hdodenhof.circleimageview.CircleImageView

class MensagensAdapter(private val mensagenList: List<Mensagen>, private val context: Context) : RecyclerView.Adapter<MensagensAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_mensagens, viewGroup, false)

        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val (id, titulo, descricao, dataAtividade) = mensagenList[i]

        myViewHolder.textViewDataAtividade.text = dataAtividade.toString()
        myViewHolder.textViewAtividadeCanal.text = titulo
    }

    override fun getItemCount(): Int {
        return mensagenList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val thumbCanal: CircleImageView
        val textViewNomeCanal: TextView
        val textViewAtividadeCanal: TextView
        val textViewDataAtividade: TextView

        init {
            thumbCanal = itemView.findViewById(R.id.circleImageViewMensagemThumbCanal)
            textViewNomeCanal = itemView.findViewById(R.id.textViewMensagemNomeCanal)
            textViewAtividadeCanal = itemView.findViewById(R.id.textViewMensagemAtividadeCanal)
            textViewDataAtividade = itemView.findViewById(R.id.textViewMensagemDataAtividadeCanal)
        }
    }
}