package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gmmp.easylearn.R

class MeuCanalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meu_canal)
    }

    /*
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

     */
}
