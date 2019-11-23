package com.gmmp.easylearn.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmmp.easylearn.R
import com.gmmp.easylearn.helper.cursoGlobal
import com.gmmp.easylearn.helper.videoGlobal
import io.clappr.player.Player
import io.clappr.player.base.ClapprOption
import io.clappr.player.base.Options
import kotlinx.android.synthetic.main.activity_aula.*


class AulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aula)

        supportActionBar?.hide()

        txt_nomeAula.text = videoGlobal.nome
        txt_nomeCurso.text = cursoGlobal.nome

        Player.initialize(this)
        val player = Player()

        player.configure(Options(videoGlobal.midiaUrl, ClapprOption.START_AT.value))

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.videoPlayer, player)
        fragmentTransaction.commit()

    }

    override fun onPause() {
        super.onPause()

    }
}
