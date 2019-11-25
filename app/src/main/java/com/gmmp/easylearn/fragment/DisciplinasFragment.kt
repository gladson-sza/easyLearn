package com.gmmp.easylearn.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.DisciplinasAdapter
import com.gmmp.easylearn.model.Disciplina
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_disciplinas.view.*


/**
 * A simple [Fragment] subclass.
 */
class DisciplinasFragment : Fragment() {

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
        val layoutManager = LinearLayoutManager(activity)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = disciplinaAdapter
        recyclerView!!.addItemDecoration(DividerItemDecoration(recyclerView!!.context, layoutManager.orientation))

        disciplinas.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    var u = d.getValue(Disciplina::class.java)
                    if (u != null) {
                        listaDisciplinas.add(u)
                    }

                }

                disciplinaAdapter?.notifyDataSetChanged()
                view.progress_circular.visibility = View.GONE
                view.textCarregando.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return view
    }

}