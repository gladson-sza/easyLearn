package com.gmmp.easylearn.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.gmmp.easylearn.R
import com.gmmp.easylearn.dialog.ViewDialog
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_esqueci_minha_senha.*
import org.jetbrains.anko.toast

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


                    toast("Email de recuperação enviado para: ${txtEmail.text}")

                }.addOnFailureListener {
                    carregando.hideDialog()
                    try {
                        throw it
                    } catch (e: FirebaseAuthInvalidUserException) {
                        if (e.errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            toast("O email informado não corresponde a um usuário cadastrado")
                        } else {
                            toast("Erro no email")
                        }

                    } catch (e: FirebaseAuthException) {
                        toast("Você não inseriu um email válido")
                    }
                }
            } else {
                txtEmail.error = "Campo obrigatório"
            }
        }
    }
}
