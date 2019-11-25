package com.gmmp.easylearn.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmmp.easylearn.R
import com.gmmp.easylearn.adapter.MensagensAdapter
import com.gmmp.easylearn.model.Mensagen
import kotlinx.android.synthetic.main.activity_notificacao.*
import java.util.*

class NotificacaoActivity : AppCompatActivity() {

    private var listaMensagens: ArrayList<Mensagen> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacao)

        supportActionBar?.hide()

        listaMensagens.add(Mensagen("321", "Fala Galera!!!", "Nossa Mano!", Date()))
        listaMensagens.add(Mensagen("123", "Mensagem aleatoria", "serio mesmo", Date()))
        listaMensagens.add(Mensagen("4323", "RIP IRON MAN", "Tony Stark Morreu", Date()))
        listaMensagens.add(Mensagen("213", "Luciano Hulk", "", Date()))
        listaMensagens.add(Mensagen("123", "Bolsolixo", "Birolo ataca novamente", Date()))

        // Configura o Adapter
        val mensagensAdapter = this.let { MensagensAdapter(listaMensagens, it) }
        recyclerViewNotificacoes!!.layoutManager = LinearLayoutManager(this)
        recyclerViewNotificacoes!!.adapter = mensagensAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Botão adicional na ToolBar
        when (item.itemId) {
            //Método para matar a activity e não deixa-lá indexada na pilhagem | PAULO
            //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            android.R.id.home -> finish()
        }

        return true
    }
}
