package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.DisciplinasAdapter
import com.gmmp.easylearn.adapter.MensagensAdapter
import com.gmmp.easylearn.model.Disciplina
import com.gmmp.easylearn.model.Mensagen
import com.gmmp.easylearn.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DisciplinasFragment : Fragment()  {

    private var recyclerView: RecyclerView? = null
    private var disciplinaAdapter: DisciplinasAdapter? = null
    private var listaDisciplinas: ArrayList<Disciplina> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_disciplinas, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewDisciplinas)


        val auth = FirebaseAuth.getInstance().currentUser
        val disciplinas = FirebaseDatabase.getInstance().reference.child("disciplinas")

        // Configura o Adapter
        disciplinaAdapter = activity?.let { DisciplinasAdapter(listaDisciplinas, it) }
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.adapter = disciplinaAdapter

        disciplinas.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (d  in dataSnapshot.children) {
                    var u = d.getValue(Disciplina::class.java)
                    if (u != null) {
                        listaDisciplinas.add(u)
                    }


                }

                disciplinaAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        return view
    }

}
