package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Usuario
import com.gmmp.easylearn.model.ViewDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import org.w3c.dom.Text
import android.content.Intent
import com.gmmp.easylearn.fragment.DestaquesFragment


class CadastroActivity : AppCompatActivity() {

    private var textNome: EditText? = null
    private var textEmail: EditText? = null
    private var textSenha: EditText? = null
    private var textConfirmarSenha: EditText? = null
    private var checkTermos: CheckBox? = null
    private var buttonContinuar: Button? = null
    private var textLogin: TextView? = null
    private val layoutGoogle: LinearLayout? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var viewDialog : ViewDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        inicializar()
    }

    fun inicializar() {
        //Esconde a Actionbar
        supportActionBar!!.hide()

        //Inicializa o Firebase
        FirebaseApp.initializeApp(this)

        //Inicializa os campos
        textNome = findViewById(R.id.textNome)
        textEmail = findViewById(R.id.textEmail)
        textSenha = findViewById(R.id.textSenha)
        textConfirmarSenha = findViewById(R.id.textConfirmaSenha)
        checkTermos = findViewById(R.id.checkTermos)

        viewDialog = ViewDialog(this@CadastroActivity)

        //Inicializa o botão de Continuar
        buttonContinuar = findViewById(R.id.buttonContinuar)
        buttonContinuar!!.setOnClickListener {
            //Validação dos campos
            if (!textNome!!.text.toString().isEmpty()) {
                if (!textEmail!!.text.toString().isEmpty()) {
                    if (!textSenha!!.text.toString().isEmpty()) {
                        if (textSenha!!.text.toString() == textConfirmarSenha!!.text.toString()) {
                            viewDialog!!.showDialog("Validando os dados", "Por favor, aguarde enquanto validamos os dados")
                            //Registra usuário no Firebase
                            registrarUsuario(textEmail!!.text.toString(), textSenha!!.text.toString())

                        } else {
                            Toast.makeText(this@CadastroActivity, "Erro: Senhas não coincidem", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@CadastroActivity, "Erro: Senha inválida", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CadastroActivity, "Erro: Endereço de email não pode ficar em branco", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@CadastroActivity, "Erro: Nome não pode ficar em branco", Toast.LENGTH_SHORT).show()
            }
        }

        //Inicializa o texto "clique aqui"
        textLogin = findViewById(R.id.textLogin)
        textLogin!!.setOnClickListener { setContentView(R.layout.activity_login) }
    }


    fun registrarUsuario(email: String, senha: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth!!.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    viewDialog!!.hideDialog()
                    if (task.isSuccessful) {
                        //Pega a referência do nó de usuários
                        val reference = FirebaseDatabase.getInstance().reference.child("usuarios")

                        //Cria um objeto de usuário
                        val usuario = Usuario(firebaseAuth!!.currentUser!!.uid,
                                textNome!!.text.toString(),
                                textEmail!!.text.toString(),
                                textSenha!!.text.toString())

                        //Adiciona ao Firebase
                        reference.child(usuario.id).setValue(usuario)

                        //Abre tela de Destaques
                        val intent = Intent(this@CadastroActivity, NavegacaoActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@CadastroActivity, "Erro", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}