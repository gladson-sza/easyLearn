package com.gmmp.easylearn.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.ListarAulasActivity
import com.gmmp.easylearn.activity.NavegacaoActivity
import com.gmmp.easylearn.activity.TodosCursosActivity
import com.gmmp.easylearn.helper.listarPor
import com.gmmp.easylearn.helper.moduloGlobal
import com.gmmp.easylearn.model.Comentario
import com.gmmp.easylearn.model.Disciplina
import com.gmmp.easylearn.model.Modulo
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.image
import org.jetbrains.anko.imageURI
import org.jetbrains.anko.startActivity

class ComentarioAdapter(private val context: Context, private val comentarios: ArrayList<Comentario>) : RecyclerView.Adapter<ComentarioAdapter.MyViewHolder>() {

    fun removeItem(position: Int) {
        comentarios.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, comentarios.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemList = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_comentario, viewGroup, false)
        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val (id , cursoId, moduloId, videoId, comentario) = comentarios[i]

        myViewHolder.txt_comentario.text = comentarios[i].comentario
        //myViewHolder.img_usuario.img = usuarioGloba1.urlWallpaper

    }

    override fun getItemCount(): Int {
        return comentarios.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_comentario: TextView
        val img_usuario: ImageView

        init {
            txt_comentario = itemView.findViewById(R.id.txt_comentario)
            img_usuario = itemView.findViewById(R.id.img_usuario)
        }
    }
}