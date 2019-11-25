package com.gmmp.easylearn.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.EditarCursoActivity
import com.gmmp.easylearn.activity.NavegacaoActivity
import com.gmmp.easylearn.model.Curso
import kotlinx.android.synthetic.main.adapter_cursos_disponibilizados.view.*

class CursosDisponibilizadosAdapter(private val cursoList: List<Curso>, private val context: Context) : RecyclerView.Adapter<CursosDisponibilizadosAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_cursos_disponibilizados, viewGroup, false)

        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val (id, nome, descricao, thumbUrl, disciplina) = cursoList[i]

        myViewHolder.textNome.text = nome
        myViewHolder.textDescricao.text = descricao
        myViewHolder.buttonEdit.setOnClickListener {

            NavegacaoActivity.cursoGlobal = cursoList[i]
            val intent = Intent(context, EditarCursoActivity::class.java)
            intent.putExtra("idCurso", id)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int = cursoList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val thumbCurso: ImageView
        val textNome: TextView
        val textDescricao: TextView
        val buttonEdit: Button

        init {
            thumbCurso = itemView.imageCursosDisponibilizadosThumb
            textNome = itemView.textCursosDisponibilizadosNome
            textDescricao = itemView.textCursosDisponibilizadosDescricao
            buttonEdit = itemView.buttonCursosDisponibilizadosEditar
        }
    }
}