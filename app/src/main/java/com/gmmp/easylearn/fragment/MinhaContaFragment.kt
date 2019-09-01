package com.gmmp.easylearn.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.activity.LoginActivity
import com.gmmp.easylearn.activity.NavegacaoActivity
import com.gmmp.easylearn.model.ViewDialog
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass.
 */
class MinhaContaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_minha_conta, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view : View){
        val sair = view.findViewById<ImageView>(R.id.imageSettings)
        val viewDialog = ViewDialog(activity)

        sair.setOnClickListener(View.OnClickListener{
            viewDialog!!.showDialog("Saindo", "Aguarde enquanto fazemos as alterações")
            viewDialog!!.hideDialog()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            FirebaseAuth.getInstance().signOut()
            startActivity(intent)
        })
    }
}
