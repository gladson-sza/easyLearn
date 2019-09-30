package com.gmmp.easylearn.helper

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.gmmp.easylearn.model.Curso

/**
 * Função que verifica que sem conexão com a internet
 */

fun temConexao(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    return networkInfo != null && networkInfo.isConnected
}

/**
 * Função que valida permissões para o usuário
 */
fun validaPermissoes(permissoes: Array<String>, activity: Activity, requestCode: Int): Boolean {

    if (Build.VERSION.SDK_INT >= 23) {
        ActivityCompat.requestPermissions(activity, permissoes, requestCode)
    }

    return true
}

//Nome do curso
var cursoGlobal: Curso? = null