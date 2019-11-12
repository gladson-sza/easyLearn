package com.gmmp.easylearn.model

data class Modulo(var id : String, var cursoId: String, var nome: String, var qtdAulas : Int) {
    constructor() : this("","","", 0)
}