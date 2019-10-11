package com.gmmp.easylearn.helper

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.gmmp.easylearn.model.Curso
import com.google.firebase.database.FirebaseDatabase

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

// Variaeis do Paulo (não mexa)
//Nome do curso
lateinit var cursoGlobal: Curso
//listar por
var listarPor: String? = null
var comprado: Boolean? = false

fun cursosReferencia() = FirebaseDatabase.getInstance().reference.child("cursos")
fun disciplinasReferencia() = FirebaseDatabase.getInstance().reference.child("disciplinas")
fun usuariosReferencia() = FirebaseDatabase.getInstance().reference.child("usuarios")
fun modulosReferencia(idCurso: String) = cursosReferencia().child(idCurso).child("modulos")
fun videosReferencia(idCurso: String, idModulo: String) = modulosReferencia(idCurso).child(idModulo).child("videos")