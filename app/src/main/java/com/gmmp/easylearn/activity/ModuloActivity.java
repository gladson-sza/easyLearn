package com.gmmp.easylearn.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmmp.easylearn.R;
import com.gmmp.easylearn.model.Modulo;
import com.gmmp.easylearn.model.Video;

import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

import static com.gmmp.easylearn.helper.UtilKt.getCursoGlobal;

public class ModuloActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo);

        iniciar();
    }

    private void iniciar(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getCursoGlobal().getNome());

        ExpandableLayout layout = findViewById(R.id.expandable);

        layout.setRenderer(new ExpandableLayout.Renderer<Modulo, Video>() {

            @Override
            public void renderParent(View view, Modulo model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tv_parent_name)).setText("Seção " + (parentPosition + 1) + " - " + model.getNome());
                if(isExpanded){
                    view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.ic_seta_para_cima);
                }else {
                    view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.ic_seta_para_baixo);
                }
            }

            @Override
            public void renderChild(final View view, Video model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tv_child_name)).setText(model.getNome());
                ((TextView) view.findViewById(R.id.txt_video_duracao)).setText(model.getDuracao());
                (view.findViewById(R.id.tv_child_name)).setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        TextView txt = (TextView)v;
                        Toast.makeText(ModuloActivity.this, "Video clicado: " + txt.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        layout.addSection(getSection1());
        layout.addSection(getSection2());
    }

    private Section<Modulo, Video> getSection1(){
        Section<Modulo, Video> section = new Section<>();

        List<Video> aulas = new ArrayList<>();
        Modulo modulo = new Modulo("Vamos começar");

        for(int i = 0; i < 5; i++){
            aulas.add(new Video("" + i ,"Vídeo " + i, 0,0,0,"0"," ", i + ".0min"));
        }

        section.parent = modulo;
        section.children.addAll(aulas);
        return section;
    }


    private Section<Modulo, Video> getSection2(){
        Section<Modulo, Video> section = new Section<>();

        List<Video> aulas = new ArrayList<>();
        Modulo modulo = new Modulo("Instalação no Windows");

        for(int i = 0; i < 5; i++){
            aulas.add(new Video("" + i ,"Vídeo " + i, 0,0,0,"0"," ", i + ".0min"));
        }

        section.parent = modulo;
        section.children.addAll(aulas);
        return section;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }


}
