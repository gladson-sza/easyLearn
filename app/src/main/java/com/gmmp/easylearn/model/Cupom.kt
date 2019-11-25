package com.gmmp.easylearn.model

data class Cupom(var codigo : String, var destinarioId : String, var valor : Int, var usado : Boolean, var validade : String){

    constructor(codigo : String, destinarioId : String, valor : Int, usado : Boolean): this(codigo, destinarioId, valor, usado, "")
    constructor(): this("","",0,false,"")
}