package com.gmmp.easylearn.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gmmp.easylearn.R;
import com.gmmp.easylearn.activity.NavegacaoActivity;


public class DialogCompraFragment extends DialogFragment {

    private DialogCompraViewModel mViewModel;
    public View viewRoot = null;

    public static DialogCompraFragment newInstance() {
        return new DialogCompraFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Comprar", null);
        builder.setNegativeButton("Cancelar", null);

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        viewRoot = inflater.inflate(R.layout.dialog_compra_fragment, null);
        TextView nome = viewRoot.findViewById(R.id.text_nome_curso);
        TextView valor = viewRoot.findViewById(R.id.text_valor);
        nome.setText(NavegacaoActivity.Companion.getCursoGlobal().getNome());
        valor.setText("" + NavegacaoActivity.Companion.getCursoGlobal().getPreco());



        builder.setView(viewRoot);

        return builder.create();
    }

}
