package com.gmmp.easylearn.model

data class Comentario(var id : String, var cursoId: String, var moduloId: String, var videoId: String, var comentario: String) {
    constructor() : this("","","","","")
}