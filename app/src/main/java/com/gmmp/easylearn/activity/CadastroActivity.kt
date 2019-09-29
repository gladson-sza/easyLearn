package com.gmmp.easylearn.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.Usuario
import com.gmmp.easylearn.dialog.ViewDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class CadastroActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100

    private lateinit var textNome: EditText
    private lateinit var textEmail: EditText
    private lateinit var textSenha: EditText
    private lateinit var textConfirmarSenha: EditText
    private lateinit var checkTermos: CheckBox
    private lateinit var buttonContinuar: Button
    private lateinit var textLogin: TextView
    private lateinit var btnGoogle: LinearLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewDialog : ViewDialog
    private lateinit var googleSignInClient: GoogleSignInClient

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

        firebaseAuth = FirebaseAuth.getInstance()

        // Configura o Token do Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configura clique do botão do Google
        btnGoogle = findViewById(R.id.buttonCadastroGoogle)
        btnGoogle.setOnClickListener {
            Toast.makeText(applicationContext, "AAA", Toast.LENGTH_SHORT)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }


    fun registrarUsuario(email: String, senha: String) {

        val carregando = ViewDialog(this)
        carregando.showDialog("Autenticando", "Carregando...")

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    viewDialog.hideDialog()
                    if (task.isSuccessful) {
                        //Pega a referência do nó de usuários
                        val reference = FirebaseDatabase.getInstance().reference.child("usuarios")

                        //Cria um objeto de usuário
                        val usuario = Usuario(firebaseAuth!!.currentUser!!.uid,
                                textNome.text.toString(),
                                textEmail.text.toString(),
                                textSenha.text.toString())

                        //Adiciona ao Firebase
                        reference.child(usuario.id).setValue(usuario)

                        carregando.hideDialog()

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

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@CadastroActivity, NavegacaoActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Falha ao tentar autenticar com o google", Toast.LENGTH_SHORT)
                    }
                }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }
}