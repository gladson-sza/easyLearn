package com.gmmp.easylearn.model

data class Curso(var id: String, var nome: String,
                 var descricao: String, var thumbUrl: String, var disciplina: String) {


    constructor() : this("", "", "", "", "")
}