package com.gmmp.easylearn.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.*
import com.gmmp.easylearn.model.Curso
import com.gmmp.easylearn.model.Modulo
import com.gmmp.easylearn.model.Video

class NavegacaoActivity : AppCompatActivity() {

    companion object {
        var cursoGlobal = Curso()
        var moduloGlobal = Modulo()
        var videoGlobal = Video()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_destaques -> {
                mudarFragment(DestaquesFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_meus_cursos -> {
                mudarFragment(MeusCursosFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_disciplinas -> {
                mudarFragment(DisciplinasFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mensagens -> {
                mudarFragment(MeuCanalFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_minha_conta -> {
                mudarFragment(PreferenciasFragment())
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    /**
     * Substitui o Fragment atual pelo fragment enviado como parêmetro.
     * @param fragment
     */
    private fun mudarFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainer, fragment)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegacao)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        inicializar()
    }

    private fun inicializar() {
        //Esconde a actionBar
        supportActionBar?.hide()

        // Inicia o primeiro fragment para exibição.
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameContainer, DestaquesFragment())
        transaction.commit()
    }

}
