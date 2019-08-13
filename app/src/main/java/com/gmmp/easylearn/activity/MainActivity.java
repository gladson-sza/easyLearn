package com.gmmp.easylearn.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gmmp.easylearn.R;

public class MainActivity extends AppCompatActivity {

    private Button buttonComecar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        //Esconde a Actionbar
        getSupportActionBar().hide();

        //Inicializa o botão
        buttonComecar = findViewById(R.id.buttonComecar);
        buttonComecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre tela de cadastro
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}
