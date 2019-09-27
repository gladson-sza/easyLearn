package com.gmmp.easylearn.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmmp.easylearn.R;
import com.gmmp.easylearn.model.Aula;
import com.gmmp.easylearn.model.Modulo;

import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class a extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo);

        ExpandableLayout e = findViewById(R.id.expandableLayout);

        e.setRenderer(new ExpandableLayout.Renderer<Modulo,Aula>() {
            @Override
            public void renderParent(View view, Modulo model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tv_parent_name)).setText(model.getTitulo());
                ImageView a = view.findViewById(R.id.arrow);
                        a.setBackground(isExpanded?R.drawable.ic_aroow_down);
            }

            @Override
            public void renderChild(View view, Aula model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tv_child_name)).setText(model.getTitulo());

            }

        });

        e.addSection(getSection());
    }

    private Section<Modulo, Aula> getSection() {
        Section<Modulo, Aula> section = new Section<>();

        List<Aula> listAula = new ArrayList<>();
        Modulo modulo = new Modulo(1, "modulo");


        for(int i = 0; i < 10; i++)
            listAula.add(new Aula("1", "aula" + i, "zsas", "sadasd", "sada"));

        section.parent = modulo;
        section.children.addAll(listAula);
        return section;



    }
}
