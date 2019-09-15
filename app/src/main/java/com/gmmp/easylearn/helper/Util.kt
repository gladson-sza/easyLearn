package com.gmmp.easylearn.helper

import android.content.Context
import android.net.ConnectivityManager

/**
 * Função que verifica que sem conexão com a internet
 */
fun temConexao(context: Context) : Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    return networkInfo != null && networkInfo.isConnected
}