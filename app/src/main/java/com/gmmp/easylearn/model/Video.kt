package com.gmmp.easylearn.model

import java.sql.Blob

data class Video(var id: String, var nome: String, var avaliacao: Int,
                 var visualizacao: Long, var denuncia: Long,
                 var urlThumb: String, var descricao: String, var duracao: String) {

    var midia: Blob? = null

}