package com.gmmp.easylearn.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.gmmp.easylearn.R;

public class NavegacaoActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_destaques:
                    mTextMessage.setText(R.string.destaques);
                    return true;
                case R.id.navigation_meus_cursos:
                    mTextMessage.setText(R.string.meus_cursos);
                    return true;
                case R.id.navigation_disciplinas:
                    mTextMessage.setText(R.string.disciplinas);
                    return true;
                case R.id.navigation_mensagens:
                    mTextMessage.setText(R.string.mensagens);
                    return true;
                case R.id.navigation_minha_conta:
                    mTextMessage.setText(R.string.minha_conta);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacao);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
