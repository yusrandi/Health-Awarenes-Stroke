package com.use.steps.models

data class Gejala(
    val id:Int? = 0,
    val name:String,
    val kategori_id:Int,
    val jawabans:List<BasisPengetahuan>?)