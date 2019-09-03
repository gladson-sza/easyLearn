package com.gmmp.easylearn.model

data class Disciplina(var id: Long, var nome: String, var icon: String) {
    constructor() : this(0L, "", "")
}
