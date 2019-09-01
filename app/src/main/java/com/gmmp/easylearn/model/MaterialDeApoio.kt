package com.gmmp.easylearn.model

import java.sql.Blob

data class MaterialDeApoio(var id: String, var nome: String,
                           var descricao: String, var pdf: Blob)