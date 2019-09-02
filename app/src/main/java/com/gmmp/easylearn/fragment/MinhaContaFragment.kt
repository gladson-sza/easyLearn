package com.gmmp.easylearn.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gmmp.easylearn.R
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

        inicializar(view)
        return view
    }

    fun inicializar(view: View) {

        val textNomeCanal = view.textPerfilNomeDoCanal
        val textViewDescricao = view.textPerfilDescricao

        val viewDialog = ViewDialog(activity)
        viewDialog.showDialog("Aguarde", "Obtendo informações de nossos servidores")

        val preferencias = view.imagePreferencias

        preferencias.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameContainer, EditarPerfilFragment())
            transaction?.commit()
        }

        /* TODO ARRUMAR UM BOTÃO PRA SAIR DA CONTA
        preferencias.setOnClickListener {
            viewDialog.showDialog("Saindo", "Aguarde enquanto fazemos as alterações")
            viewDialog.hideDialog()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            FirebaseAuth.getInstance().signOut()
            startActivity(intent)
        }*/

        // Carrega as Informações do Usuário no Seu Perfil
        val auth = FirebaseAuth.getInstance().currentUser
        val usuario = FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.uid)
        val cursos = usuario.child("canal").child("cursos")

        usuario.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = dataSnapshot.getValue(Usuario::class.java)

                textNomeCanal.text = u?.nome
                textViewDescricao.text = u?.descricao

                // Fecha o Dialog após carregar os dados
                viewDialog.hideDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        // Botão de Novo Curso
        val buttonNovoCurso = view.buttonNovoCurso
        buttonNovoCurso.setOnClickListener {
            startActivity(Intent(activity, NovoCursoActivity::class.java))
        }

        // Configura o RecyclerView de CursosDisponibilizados
        val adapter = CursosDisponibilizadosAdapter(listCursos!!, context!!)
        var recyclerView = view.recyclerViewCursosDisponibilizados
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Carrega os Dados
        cursos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listCursos.clear()
                for (ds in dataSnapshot.children) {
                    listCursos.add(ds.getValue(Curso::class.java)!!)
                }

                val textCursosDisponibilizados = view.textNCursosDisponibilizados
                textCursosDisponibilizados.text = listCursos.size.toString()

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

}
