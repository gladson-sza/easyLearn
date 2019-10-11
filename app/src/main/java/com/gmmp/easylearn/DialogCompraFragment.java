package com.gmmp.easylearn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.gmmp.easylearn.helper.UtilKt.cursoGlobal;

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
        nome.setText(cursoGlobal.getNome());
        valor.setText("" + cursoGlobal.getPreco());

        builder.setView(viewRoot);

        return builder.create();
    }

}
