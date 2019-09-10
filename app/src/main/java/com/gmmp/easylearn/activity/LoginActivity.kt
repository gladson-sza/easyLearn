package com.gmmp.easylearn.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.gmmp.easylearn.R
import com.gmmp.easylearn.model.ViewDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100

    private lateinit var btnIniciarSessao: Button
    private lateinit var txtCadastro: TextView
    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewDialog: ViewDialog
    private lateinit var btnGoogle: LinearLayout
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inicializar()
    }

    fun inicializar() {

        // Inicializa os EditTexts
        editEmail = findViewById(R.id.textEmail)
        editSenha = findViewById(R.id.textSenha)

        viewDialog = ViewDialog(this@LoginActivity)

        supportActionBar?.hide()
        //Inicializa o botão
        btnIniciarSessao = findViewById(R.id.buttonEntrar)
        btnIniciarSessao.setOnClickListener {

            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                viewDialog.showDialog("Autenticando", "Por favor, aguarde enquanto validamos os dados")
                firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this) { task ->
                    viewDialog.hideDialog()
                    if (task.isSuccessful) {
                        val intent = Intent(this@LoginActivity, NavegacaoActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }

        }

        txtCadastro = findViewById(R.id.textCadastro)
        txtCadastro!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, CadastroActivity::class.java))
        })

        // Configura o Token do Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configura clique do botão do Google
        btnGoogle = findViewById(R.id.buttonLoginGoogle)
        btnGoogle.setOnClickListener {
            Toast.makeText(applicationContext, "AAA", Toast.LENGTH_SHORT)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@LoginActivity, NavegacaoActivity::class.java)
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
                Toast.makeText(applicationContext, "Falha ao tentar autenticar com o google", Toast.LENGTH_SHORT)
            }
        }
    }

}