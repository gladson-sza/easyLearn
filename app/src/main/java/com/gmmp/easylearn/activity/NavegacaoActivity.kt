package com.gmmp.easylearn.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView

import com.gmmp.easylearn.R
import com.gmmp.easylearn.fragment.*

class NavegacaoActivity : AppCompatActivity() {

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
                mudarFragment(MensagensFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_minha_conta -> {
                mudarFragment(MinhaContaFragment())
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
