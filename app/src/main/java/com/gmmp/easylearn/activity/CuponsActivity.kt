package com.gmmp.easylearn.activity

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gmmp.easylearn.R
import kotlinx.android.synthetic.main.activity_cupons.*

class CuponsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cupons)
        iniciar()
    }

    private fun iniciar() {
        
        supportActionBar?.hide()
        val drawable = ContextCompat.getDrawable(this@CuponsActivity, R.drawable.ic_arrow_left_black_48dp)
        toolbarCupom.setNavigationIcon(drawable);
    }
}
