package com.gmmp.easylearn.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_preferencias.view.*


/**
 * A simple [Fragment] subclass.
 */
class PreferenciasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_preferencias, container, false)

        iniciar(view)

        return view
    }

    private fun iniciar(view: View) {

        // Opção meu canal | Paulo Ribeiro
        //view.menuMeuCanal.setOnClickListener { startActivity(Intent(activity, MeuCanalActivity::class.java)) }

        view.menuEditarConta.setOnClickListener {
            startActivity(Intent(activity, EditarPerfilActivity::class.java))
        }

        view.menu_formas_pagamento.setOnClickListener {
            startActivity(Intent(activity, PagamentoActivity::class.java))
        }

        view.menuNotificacoes.setOnClickListener {
            startActivity(Intent(activity, NotificacaoActivity::class.java))
        }

        view.menuCompatilharEasyCash.setOnClickListener {
            val auth = FirebaseAuth.getInstance().currentUser
            val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)

            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "type/plain"
            val shareBody = "EasyLearning: Baixe e ganhe promoções na compra de qualquer curso usando o código abaixo, não perca!"
            val shareSub = "Baixe o app em: https://github.com/Gladson0101/easyLearn \n\n" + "Código promociona: l" + auth.uid
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
            startActivity(Intent.createChooser(myIntent, "Compartilhar EasyCash"))
        }

        view.menuSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }


    }

}
