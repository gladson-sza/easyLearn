package com.gmmp.easylearn.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.ViewDialog
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_esqueci_minha_senha.*
import java.lang.Exception

class EsqueciMinhaSenhaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueci_minha_senha)
        supportActionBar?.hide()

        btnEnviarEmail.setOnClickListener {
            if (txtEmail.text.toString().isNotEmpty()) {

                val carregando = ViewDialog(this)
                carregando.showDialog("Carregando", "Obtendo informações de nossos servidores")

                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(txtEmail.text.toString()).addOnSuccessListener {
                    carregando.hideDialog()
                    Toast.makeText(
                            this,
                            "Email de recuperação enviado para: ${txtEmail.text}",
                            Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    carregando.hideDialog()
                    try {
                        throw it
                    } catch (e: FirebaseAuthInvalidUserException) {
                        if (e.errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            Toast.makeText(
                                    this,
                                    "O email informado não corresponde a um usuário cadastrado",
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                    this,
                                    "Erro no email",
                                    Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: FirebaseAuthException) {
                        Toast.makeText(
                                this,
                                "Você não inseriu um email válido",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                txtEmail.error = "Campo obrigatório"
            }
        }
    }
}
