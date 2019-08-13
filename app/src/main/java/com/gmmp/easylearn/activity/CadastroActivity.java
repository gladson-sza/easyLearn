package com.gmmp.easylearn.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmmp.easylearn.R;
import com.gmmp.easylearn.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActivity extends AppCompatActivity {

    private EditText textNome;
    private EditText textEmail;
    private EditText textSenha;
    private EditText textConfirmarSenha;
    private CheckBox checkTermos;
    private Button buttonContinuar;
    private LinearLayout layoutGoogle;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        init();
    }

    public void init() {
        //Esconde a Actionbar
        getSupportActionBar().hide();

        //Inicializa o Firebase
        FirebaseApp.initializeApp(this);

        //Inicializa os campos
        textNome = findViewById(R.id.textNome);
        textEmail = findViewById(R.id.textEmail);
        textSenha = findViewById(R.id.textSenha);
        textConfirmarSenha = findViewById(R.id.textConfirmaSenha);
        checkTermos = findViewById(R.id.checkTermos);

        //Inicializa o botão de Continuar
        buttonContinuar = findViewById(R.id.buttonContinuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validação dos campos
                if (!textNome.getText().toString().isEmpty()) {
                    if (!textEmail.getText().toString().isEmpty()) {
                        if (!textSenha.getText().toString().isEmpty()) {
                            if (textSenha.getText().toString().equals(textConfirmarSenha.getText().toString())) {
                                //Registra usuário no Firebase
                                registrarUsuario(textEmail.getText().toString(), textSenha.getText().toString());

                            } else {
                                Toast.makeText(CadastroActivity.this, "Erro: Senhas não coincidem", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CadastroActivity.this, "Erro: Senha inválida", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroActivity.this, "Erro: Endereço de email não pode ficar em branco", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro: Nome não pode ficar em branco", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrarUsuario(String email, String senha) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Pega a referência do nó de usuários
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");

                            //Cria um objeto de usuário
                            Usuario usuario = new Usuario();
                            usuario.setId(firebaseAuth.getCurrentUser().getUid());
                            usuario.setNome(textNome.getText().toString());
                            usuario.setEmail(textEmail.getText().toString());
                            usuario.setSenha(textSenha.getText().toString());

                            //Adiciona ao Firebase
                            reference.child(usuario.getId()).setValue(usuario);
                        } else {

                        }

                    }
                });
    }
}