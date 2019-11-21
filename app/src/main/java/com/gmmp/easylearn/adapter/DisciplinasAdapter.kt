package com.gmmp.easylearn.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
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
import de.hdodenhof.circleimageview.CircleImageView

class DisciplinasAdapter(private val disciplinasList: ArrayList<Disciplina>, private val context: Context) : RecyclerView.Adapter<DisciplinasAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_disciplinas, viewGroup, false)

        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val (id , nome, iconUrl) = disciplinasList[i]

        if (!iconUrl.isEmpty()) {
            Glide.with(context)
                    .load(disciplinasList[i].icon)
                    .centerCrop()
                    .into(myViewHolder.iconDisciplina)
        }
        myViewHolder.textViewNomeDisciplina.text = nome
        myViewHolder.layout.setOnClickListener {
            listarPor = nome
            context.startActivity(Intent(context, TodosCursosActivity::class.java))
        }

    }

    override fun getItemCount(): Int {
        return disciplinasList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iconDisciplina: ImageView
        val textViewNomeDisciplina: TextView
        val layout: LinearLayout

        init {
            iconDisciplina = itemView.findViewById(R.id.circleImageViewDisciplina)
            textViewNomeDisciplina = itemView.findViewById(R.id.textViewNomedisciplina)
            layout = itemView.findViewById(R.id.layout_discilina)
        }
    }
}