package com.use.steps.datas

import com.use.steps.models.Keyakinan


class CFPakarData {
    private var dataList = mutableListOf<Keyakinan>()

    init {
        initData()
    }

    private fun initData() {
        dataList.add(Keyakinan(name = "Pasti tidak (definitely not) -1.0", value = -1.0))
        dataList.add(Keyakinan(name = "Hampir pasti tidak (Almost Certainty Not) -0.8", value = -0.8))
        dataList.add(Keyakinan(name = "Mungkin tidak (probably not) -0.6", value = -0.6))
        dataList.add(Keyakinan(name = "Barang kali tidak (maybe not) -0.4", value = -0.4))
        dataList.add(Keyakinan(name = "Tidak tahu (unknown) 0.2", value = 0.2))
        dataList.add(Keyakinan(name = "Barang kali (maybe) 0.4", value = 0.4))
        dataList.add(Keyakinan(name = "Mungkin (probably) 0.6", value = 0.6))
        dataList.add(Keyakinan(name = "Hampir pasti (Almost certainty) 0.8", value = 0.8))
        dataList.add(Keyakinan(name = "Pasti (definitely) 1.0", value = 1.0))
    }

    fun getDataCFPakar() = dataList
}