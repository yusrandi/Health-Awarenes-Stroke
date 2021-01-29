package com.use.stroke.models

data class CFValue(val id:Int? = 0,
                   val gejala_jawaban_id:Int,
                   val cf:String,
                   val gejala_jawaban: GejalaJawaban
)