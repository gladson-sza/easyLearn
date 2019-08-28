package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.MensagensAdapter
import com.gmmp.easylearn.model.Mensagen
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class MensagensFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var mensagensAdapter: MensagensAdapter? = null
    private var listaMensagens: ArrayList<Mensagen>? = ArrayList<Mensagen>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_mensagens, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMensagens)

        listaMensagens!!.add(Mensagen("321", "Fala Galera!!!", "Nossa Mano!", Date()))
        listaMensagens!!.add(Mensagen("123", "Mensagem aleatoria", "serio mesmo", Date()))
        listaMensagens!!.add(Mensagen("4323", "RIP IRON MAN", "Tony Stark Morreu", Date()))
        listaMensagens!!.add(Mensagen("213", "Luciano Hulk", "", Date()))
        listaMensagens!!.add(Mensagen("123", "Bolsolixo", "Birolo ataca novamente", Date()))

        // Configura o Adapter
        mensagensAdapter = activity?.let { MensagensAdapter(listaMensagens!!.toList(), it) }
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.adapter = mensagensAdapter

        return view
    }

}
