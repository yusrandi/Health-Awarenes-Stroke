package com.use.steps.models

data class GejalaJawaban(val id:Int? = 0,
                         val gejala_id:String,
                         val jawaban_id:String,
                         val gejala: Gejala,
                         val jawaban: Jawaban
                            )