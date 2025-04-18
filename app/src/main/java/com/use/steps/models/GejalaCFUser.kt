package com.use.steps.models

data class GejalaCFUser(val id:Int? = 0,
                        val name:String,
                        val kategori_id:Int,
                        val cfusers:List<BasisPengetahuan>?)