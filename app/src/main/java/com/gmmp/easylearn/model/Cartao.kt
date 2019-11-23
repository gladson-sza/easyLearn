package com.gmmp.easylearn.model

data class Cartao(val codigo: String, val dataValidade: String, val nomeCartao: String, val numeroCartao: String, val saldo : Float) {

    constructor() : this("", "", "", "", 0F)
    constructor(codigo: String, dataValidade: String, nomeCartao: String, numeroCartao: String) : this(codigo, dataValidade, nomeCartao, numeroCartao, 0F)
    constructor(c: Cartao?): this(c!!.codigo, c.dataValidade, c.nomeCartao, c.numeroCartao, c.saldo)
}