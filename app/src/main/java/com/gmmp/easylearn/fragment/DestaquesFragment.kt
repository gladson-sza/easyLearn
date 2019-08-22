package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gmmp.easylearn.R


/**
 * A simple [Fragment] subclass.
 */
class DestaquesFragment : Fragment() {

    private var recyclerViewEmAlta: RecyclerView? = null;
    private var recyclerViewPrincipais: RecyclerView? = null;
    private var recyclerViewRecomendados: RecyclerView? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_destaques, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view: View){
        recyclerViewEmAlta = view.findViewById(R.id.recyclerViewEmAlta)
        recyclerViewPrincipais = view.findViewById(R.id.recyclerViewPrincipais)
        recyclerViewRecomendados = view.findViewById(R.id.recyclerViewRecomendados)

        /*recyclerViewEmAlta.setHasFixedSize(true)
        recyclerViewEmAlta.layoutManager(LinearLayoutManager(this))
        recyclerViewEmAlta.adapter()*/
    }
}
