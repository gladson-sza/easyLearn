package com.gmmp.easylearn.model

data class Usuario(var id: String, var nome: String, var email: String, var senha: String) {

    var urlPerfil: String
    var urlWallpaper: String
    var descricao: String

    init {
        urlPerfil = ""
        urlWallpaper = ""
        descricao = ""
    }

    constructor() : this("", "", "", "")
    constructor(id: String, nome: String, email: String, senha: String, urlPerfil: String, urlWallpaper: String, descricao: String)
            : this(id, nome, email, senha) {
        this.urlPerfil = urlPerfil
        this.urlWallpaper = urlWallpaper
        this.descricao = descricao
    }
}