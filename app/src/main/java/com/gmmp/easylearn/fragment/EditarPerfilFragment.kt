package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Usuario
import com.gmmp.easylearn.model.ViewDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_editar_perfil.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class EditarPerfilFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        inicializar(view)

        return view
    }

    fun inicializar(view: View) {

        val viewDialog = ViewDialog(activity)

        // Carrega as Informações do Usuário no Seu Perfil
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                view.editNome.setText(u?.nome)
                view.editDescricao.setText(u?.descricao)

                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // Configuração do Botão de Salvar
        view.buttonSalvar.setOnClickListener {

            viewDialog.showDialog("Aguarde", "Salvando Informações")

            usuario.child("nome").setValue(view.editNome?.text.toString())
            usuario.child("descricao").setValue(view.editDescricao?.text.toString())

            viewDialog.hideDialog()

            // Volta para o fragment de MinhaConta
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameContainer, MinhaContaFragment())
            transaction?.commit()
        }
    }

}
