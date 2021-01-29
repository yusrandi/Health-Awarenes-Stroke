package com.use.stroke.datas

import com.use.stroke.models.Keyakinan


class CFPakarData {
    private var dataList = mutableListOf<Keyakinan>()

    init {
        initData()
    }

    private fun initData() {
        dataList.add(Keyakinan(name = "Pasti tidak (definitely not)", value = -1.0))
        dataList.add(Keyakinan(name = "Hampir pasti tidak (Almost Certainty Not)", value = -0.8))
        dataList.add(Keyakinan(name = "Mungkin tidak (probably not)", value = -0.6))
        dataList.add(Keyakinan(name = "Barang kali tidak (maybe not)", value = -0.4))
        dataList.add(Keyakinan(name = "Tidak tahu (unknown)", value = 0.2))
        dataList.add(Keyakinan(name = "Barang kali (maybe)", value = 0.4))
        dataList.add(Keyakinan(name = "Mungkin (probably)", value = 0.6))
        dataList.add(Keyakinan(name = "Hampir pasti (Almost certainty)", value = 0.8))
        dataList.add(Keyakinan(name = "Pasti (definitely)", value = 1.0))
    }

    fun getDataCFPakar() = dataList
}