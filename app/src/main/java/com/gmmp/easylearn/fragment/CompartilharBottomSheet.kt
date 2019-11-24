package com.gmmp.easylearn.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.gmmp.easylearn.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.gmmp.easylearn.helper.cupomDesconto
import com.gmmp.easylearn.helper.nomeUsuario
import android.widget.Toast
import android.content.ClipData
import android.content.ClipboardManager
import org.jetbrains.anko.support.v4.toast
import android.content.Context
import android.util.Log
import android.widget.LinearLayout


class CompartilharBottomSheet : BottomSheetDialogFragment() {

    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.layout_compartilhar, container, false)

        val txt_cupom = v.findViewById<EditText>(R.id.txt_cupom)
        val btn_copiar = v.findViewById<Button>(R.id.btn_copiar)
        txt_cupom.setText(cupomDesconto)
        txt_cupom.isEnabled = false

        btn_copiar.setOnClickListener {
            Log.i("TESTE", "clicou")
            val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("EditText", cupomDesconto.toUpperCase())
            clipboard.setPrimaryClip(clip)

            toast("Código copiado com sucesso!")
        }

        val btn_convidar = v.findViewById<Button>(R.id.btn_convidar)
        btn_convidar.setOnClickListener {
            dismiss()
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "type/plain"
            val shareBody = "${nomeUsuario} te enviou 15% de desconto para compras em cursos online. Aproveite e economize com EasyLearning"
            val shareSub = "Para resgatar sua compra com desconto, baixe o aplicativo usando este link https://bit.ly/2OcwIhX e insira o cupom: $cupomDesconto"
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
            startActivity(Intent.createChooser(myIntent, "Compartilhar EasyCash"))
        }

        return v
    }
}
