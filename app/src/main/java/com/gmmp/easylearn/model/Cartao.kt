package com.gmmp.easylearn.model

data class Cartao(val codigo: String, val dataValidade: String, val nomeCartao: String, val numeroCartao: String) {

    constructor() : this("", "", "", "")
}