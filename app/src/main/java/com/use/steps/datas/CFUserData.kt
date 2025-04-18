package com.use.steps.datas

import com.use.steps.models.Keyakinan


class CFUserData {
    private var dataList = mutableListOf<Keyakinan>()

    init {
        initData()
    }

    private fun initData() {
        dataList.add(Keyakinan(name = "Tidak", value = 0.0))
        dataList.add(Keyakinan(name = "Tidak Tahu", value = 0.2))
        dataList.add(Keyakinan(name = "Sedikit Yakin", value = 0.4))
        dataList.add(Keyakinan(name = "Cukup Yakin", value = 0.6))
        dataList.add(Keyakinan(name = "Yakin", value = 0.8))
        dataList.add(Keyakinan(name = "Sangat Yakin", value = 1.0))
    }

    fun getDataCFUser() = dataList
}