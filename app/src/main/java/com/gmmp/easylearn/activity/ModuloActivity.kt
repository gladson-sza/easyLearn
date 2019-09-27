package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.MenuItem
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Aula
import com.gmmp.easylearn.model.Modulo
import iammert.com.expandablelib.ExpandableLayout
import kotlinx.android.synthetic.main.activity_modulo.*

class ModuloActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo)

        var bar = getSupportActionBar()
        bar!!.setDisplayHomeAsUpEnabled(true)
        bar!!.setDisplayShowHomeEnabled(true)
        bar!!.setTitle("Nome do curso")

        // Porgramação do ExpandableLayout "dropdown"

//        expandableLayout.setRenderer(ExpandableLayout.Renderer)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item!!.getItemId()

        if(id == android.R.id.home)
            this.finish()
        return super.onOptionsItemSelected(item)
    }
}
