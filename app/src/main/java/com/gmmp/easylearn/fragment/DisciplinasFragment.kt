package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.gmmp.easylearn.R


/**
 * A simple [Fragment] subclass.
 */
class DisciplinasFragment : Fragment()  {

    private lateinit var listView: ListView
    private var listDisciplinas = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_disciplinas, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view: View){

        listDisciplinas = inserirDisciplinas();

        listView = view.findViewById<ListView>(R.id.listDisciplinas)

        val listItems = inserirDisciplinas()
// 4
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter

    }

    fun inserirDisciplinas(): ArrayList<String>{
        var dados = arrayListOf<String>()
        dados.addAll(listOf("Biologia", "Filosofia", "Física", "Geografia", "História", "Linguagem de Porgramação", "Línguas Estrangeiras", "Matemática", "Português", "Química"))

        return  dados
    }

}
