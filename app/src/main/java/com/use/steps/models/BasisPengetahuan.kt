package com.use.steps.models

data class BasisPengetahuan(val id:Int? = 0,
                            val gejala_id:String,
                            val jawaban_id:String,
                            val mb:Double,
                            val md:Double,
                            val cf:Double,
                            val jawaban: Jawaban
                            )