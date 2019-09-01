package com.gmmp.easylearn.model

import java.sql.Blob

data class Video(var id: String, var nome: String, var avaliacao: Double,
                 var visualizacao: Long, var denuncia: Long, var midia: Blob,
                 var urlThumb: String, var descricao: String)