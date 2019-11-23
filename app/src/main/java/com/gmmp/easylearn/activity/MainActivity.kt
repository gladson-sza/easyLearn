package com.gmmp.easylearn.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.gmmp.easylearn.R
import com.gmmp.easylearn.helper.validaPermissoes
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var buttonComecar: Button

    private val permissoes = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
    }

    fun inicializar() {

        validaPermissoes(permissoes, this, 1)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            //Esconde a Actionbar
            supportActionBar!!.hide()

            //Inicializa o botão
            buttonComecar = findViewById(R.id.buttonComecar)
            buttonComecar.setOnClickListener {
                //Abre tela de cadastro
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        } else {
            val intent = Intent(this@MainActivity, NavegacaoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     * Cria uma janela informando que a permição foi negada.
     */
    fun mensagemPermissaoNegada() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissão negada!")
        builder.setMessage("Para utilizar o app corretamente, você deve aceitar as permições.")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar") { _, _ -> finish() }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (permissionResult in grantResults) {
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                mensagemPermissaoNegada()
            }
        }
    }
}
