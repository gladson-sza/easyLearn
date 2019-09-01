package com.gmmp.easylearn.model

data class Usuario(var id: String, var nome: String, var email: String, var senha: String) {

    var urlPerfil: String
    var urlWallpaper: String

    init {
        urlPerfil = ""
        urlWallpaper = ""
    }

    constructor (id: String, nome: String, email: String, senha: String, urlPerfil: String, urlWallpaper: String) : this(id, nome, email, senha) {
        this.urlWallpaper = urlWallpaper
        this.urlPerfil = urlPerfil
    }
}