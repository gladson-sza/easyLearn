package com.gmmp.easylearn.model

data class Curso(var id: String, var nome: String,
                 var descricao: String, var thumbUrl: String, var disciplina: String, var preco: Double) {


    constructor(id: String, nome: String, descricao: String, thumbUrl: String, disciplina: String)
            : this(id, nome, descricao, thumbUrl, disciplina, 0.0)

    constructor() : this("", "", "", "", "", 0.0)
}