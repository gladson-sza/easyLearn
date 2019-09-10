package com.gmmp.easylearn.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.LoginActivity
import com.gmmp.easylearn.activity.MeuCanalActivity
import com.gmmp.easylearn.activity.NovoCursoActivity
import com.gmmp.easylearn.adapter.CursosDisponibilizadosAdapter
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Usuario
import com.gmmp.easylearn.model.ViewDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_minha_conta.*
import kotlinx.android.synthetic.main.fragment_minha_conta.view.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class MinhaContaFragment : Fragment() {

    private var listCursos: ArrayList<Curso> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_minha_conta, container, false)

        iniciar(view)

        return view
    }

    fun iniciar(view: View){

        var meuCanal = view.findViewById(R.id.meu_canal) as LinearLayout

        meuCanal.setOnClickListener {
            startActivity(Intent(activity, MeuCanalActivity::class.java))
        }

        var editarConta = view.findViewById(R.id.editar_conta) as LinearLayout

        editarConta.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameContainer, EditarPerfilFragment())
            transaction?.commit()
        }
    }


}
