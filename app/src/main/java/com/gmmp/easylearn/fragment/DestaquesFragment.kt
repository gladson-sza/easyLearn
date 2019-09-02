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
import com.gmmp.easylearn.adapter.VerticalAdapter
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Curso


/**
 * A simple [Fragment] subclass.
 */
class DestaquesFragment : Fragment() {

    private var listEmAlta : ArrayList<Aula>? = null
    private var listRecomendados : ArrayList<Aula>? = null
    private var listPrincipais: ArrayList<Curso>? = null

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
        encherPrincipais()

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

        //Inicializa Canais Principais
        recyclerViewPrincipais.layoutManager =LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapterPrincipais = VerticalAdapter(activity!!, listPrincipais!!)
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

        val aula1 = Aula("1","Descomplica","Preparatório para Vestibulares","https://www.infoenem.com.br/wp-content/uploads/2018/04/social-share-descomplica-1280x720.jpg","")
        val aula2 = Aula("2","Stoodi","Preparatório para Vestibulares","https://cadernodoenem.com.br/wp-content/uploads/2016/09/stoodi-1024x576.png","")
        val aula3 = Aula("3","Pro ENEM","Preparatório para Vestibulares","https://www.concurseirosdamadrugada.com.br/wp-content/uploads/2018/09/logo-proenem-vale-a-pena.png","")
        val aula4 = Aula("4","AulaLivre.net","Preparatório para Vestibulares","https://sambatech.com/blog/wp-content/uploads/2015/01/banner-2-case-aula-livre1.png","23:22")

        listRecomendados!!.add(aula1)
        listRecomendados!!.add(aula2)
        listRecomendados!!.add(aula3)
        listRecomendados!!.add(aula4)
    }

    fun encherPrincipais(){
        listPrincipais = ArrayList<Curso>()

        val curso1 = Curso("2","Stoodi","Preparatório para Vestibulares","https://cadernodoenem.com.br/wp-content/uploads/2016/09/stoodi-1024x576.png","")
        val curso2 = Curso("3","Pro ENEM","Preparatório para Vestibulares","https://www.concurseirosdamadrugada.com.br/wp-content/uploads/2018/09/logo-proenem-vale-a-pena.png","")
        val curso3 = Curso("4","AulaLivre.net","Preparatório para Vestibulares","https://sambatech.com/blog/wp-content/uploads/2015/01/banner-2-case-aula-livre1.png","")

        listPrincipais!!.add(curso1)
        listPrincipais!!.add(curso2)
        listPrincipais!!.add(curso3)
    }
}
