package com.gmmp.easylearn.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.HorizontalAdapter
import com.gmmp.easylearn.model.Aula


/**
 * A simple [Fragment] subclass.
 */
class DestaquesFragment : Fragment() {

    private var listEmAlta : ArrayList<Aula>? = null
    private var listRecomendados : ArrayList<Aula>? = null
    private var listPrincipais: ArrayList<Aula>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_destaques, container, false)
        inicializar(view)
        return view
    }

    fun inicializar(view: View){
        encherEmAlta()
        encherRecomendados()

        val recyclerViewEmAlta = view.findViewById<RecyclerView>(R.id.recyclerViewEmAlta)
        val recyclerViewPrincipais = view.findViewById<RecyclerView>(R.id.recyclerViewPrincipais)
        val recyclerViewRecomendados = view.findViewById<RecyclerView>(R.id.recyclerViewRecomendados)

        //Inicializa Aulas em Alta
        recyclerViewEmAlta.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapterEmAlta = HorizontalAdapter(activity!!, listEmAlta!!)
        recyclerViewEmAlta.adapter = adapterEmAlta

        //Inicializa Canais Recomendados
        recyclerViewRecomendados.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapterRecomendados = HorizontalAdapter(activity!!, listRecomendados!!)
        recyclerViewRecomendados.adapter = adapterRecomendados

        // Inicializa Principais
        recyclerViewPrincipais.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapterPrincipais = HorizontalAdapter(activity!!, listRecomendados!!)
        recyclerViewPrincipais.adapter = adapterPrincipais
    }

    fun encherEmAlta(){
        listEmAlta = ArrayList<Aula>()

        val aula1 = Aula("1","Unidades de Medidas","Leo Gomes","https://www.jovensestudantes.com.br/wp-content/uploads/2018/07/gratis-unidades-de-medidas-e-ord.jpg","25:35")
        val aula2 = Aula("5","Porcentagem","Ferretto","https://images.passeidireto.com/thumbnails/video/65117545/thumb.jpg.xlarge","11:46")
        val aula3 = Aula("3","Teorema de Tales","Alex","https://www.jovensestudantes.com.br/wp-content/uploads/2018/07/gratis-teorema-de-tales-matemati.jpg","25:35")
        val aula4 = Aula("2","Multiplicação e Divisão","Ferretto","https://img.youtube.com/vi/0UGJRHq2PS4/maxresdefault.jpg","15:32")
        val aula5 = Aula("4","Adição e Subtração","Ferretto","https://images.passeidireto.com/thumbnails/video/65117508/thumb.jpg.xlarge","23:22")

        listEmAlta!!.add(aula2)
        listEmAlta!!.add(aula4)
        listEmAlta!!.add(aula5)
        listEmAlta!!.add(aula3)
        listEmAlta!!.add(aula1)
    }

    fun encherRecomendados(){
        listRecomendados = ArrayList<Aula>()

        val aula1 = Aula("1","Prof. Olda","Biologia","https://www.jovensestudantes.com.br/wp-content/uploads/2018/07/gratis-metodo-cientifico-e-alfab.jpg","25:35")
        val aula2 = Aula("5","Descomplica","Concursos","https://images.passeidireto.com/thumbnails/video/65117545/thumb.jpg.xlarge","11:46")
        val aula3 = Aula("3","Teorema de Tales","Alex","https://www.jovensestudantes.com.br/wp-content/uploads/2018/07/gratis-teorema-de-tales-matemati.jpg","25:35")
        val aula4 = Aula("2","Multiplicação e Divisão","Ferretto","https://img.youtube.com/vi/0UGJRHq2PS4/maxresdefault.jpg","15:32")
        val aula5 = Aula("4","Adição e Subtração","Ferretto","https://images.passeidireto.com/thumbnails/video/65117508/thumb.jpg.xlarge","23:22")

        listRecomendados!!.add(aula1)
        listRecomendados!!.add(aula2)
        listRecomendados!!.add(aula3)
        listRecomendados!!.add(aula4)
        listRecomendados!!.add(aula5)
    }
}
