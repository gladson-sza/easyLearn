package com.gmmp.easylearn.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.gmmp.easylearn.R;
import com.gmmp.easylearn.fragment.DestaquesFragment;
import com.gmmp.easylearn.fragment.DisciplinasFragment;
import com.gmmp.easylearn.fragment.MensagensFragment;
import com.gmmp.easylearn.fragment.MeusCursosFragment;
import com.gmmp.easylearn.fragment.MinhaContaFragment;

public class NavegacaoActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_destaques:
                    mudarFragment(new DestaquesFragment());
                    return true;
                case R.id.navigation_meus_cursos:
                    mudarFragment(new MeusCursosFragment());
                    return true;
                case R.id.navigation_disciplinas:
                    mudarFragment(new DisciplinasFragment());
                    return true;
                case R.id.navigation_mensagens:
                    mudarFragment(new MensagensFragment());
                    return true;
                case R.id.navigation_minha_conta:
                    mudarFragment(new MinhaContaFragment());
                    return true;
            }

            return false;
        }
    };

    /**
     * Substitui o Fragment atual pelo fragment enviado como parêmetro.
     * @param fragment
     */
    private void mudarFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacao);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
