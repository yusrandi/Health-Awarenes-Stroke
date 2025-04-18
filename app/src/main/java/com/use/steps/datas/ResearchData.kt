package com.use.steps.datas

import com.use.steps.models.Research

class ResearchData {
    private var dataList = mutableListOf<Research>()
    init {
        initData()
    }
    private fun initData(){
        dataList.add(Research(0, "Latar Belakang Studi", "\t\t\tStroke adalah penyakit umum global yang memiliki efek merugikan pada individu dan//, lebih luas pada masyarakat. Perubahan gaya hidup dapat berkontribusi untuk mengurangi faktor risiko stroke. Meskipun gaya hidup sehat memiliki manfaat langsung, mempertahankan dan memasukkan kegiatan sehat ke dalam kehidupan sehari-hari adalah sebuah tantangan. Melibatkan kegiatan sehari-hari memiliki potensi untuk mendukung perubahan gaya hidup dan untuk mempromosikan pola kegiatan yang berkelanjutan. Layanan kesehatan saat ini gagal mengurangi faktor risiko yang dapat dimodifikasi pada orang yang berisiko, dan di samping praktik saat ini, ada kebutuhan untuk strategi pencegahan stroke non-farmakologis dan non-bedah yang sistematis dan "))
        dataList.add(Research(1, "Tujuan Studi", "\t\t\tTujuan dari studi adalah untuk meningkatkan pengetahuan tentang efek dari program pencegahan dan kelayakannya untuk mempromosikan pola aktivitas yang berkelanjutan dan// sehat di antara orang-orang yang berisiko terkena stroke."))
        dataList.add(Research(2, "Metode dan Analisis", "\t\t\tStudi percontohan yang diusulkan akan menjadi uji coba percontohan paralel paralel dua tangan yang dilakukan secara acak. Studi ini akan mencakup data kelayakan, menyelidiki penerimaan dan// pengiriman intervensi. Orang yang berisiko stroke (n = 60) akan dimasukkan dalam program pencegahan yang didukung ponsel. Program 10 minggu akan dilakukan di klinik perawatan kesehatan primer, menggabungkan pertemuan kelompok dan sumber daya online untuk mendukung manajemen diri dari perubahan gaya hidup. Hasil utama adalah risiko stroke, kebiasaan gaya hidup dan pola aktivitas yang sehat. Penilaian akan dilakukan pada awal dan pada tindak lanjut (segera setelah akhir program dan pada 6 dan 12 bulan). Efek program akan "))
        dataList.add(Research(3, "Diskusi", "\t\t\tBasis teoretis protokol ini kuat dan didasarkan pada EEA sebagai mediator dan tujuan untuk mengurangi risiko stroke dan menjalani kehidupan yang sehat. Teknologi ponsel memungkinkan proses perubahan dengan menawarkan umpan balik individu dan// peningkatan kesadaran akan gaya hidup saat ini dan pendaftaran kebiasaan baru. Studi percontohan ini akan memberikan data awal tentang efek dan kelayakan program pencegahan Gaya Hidup Aktif dan tindakan serta prosedurnya. Data yang kaya tentang dampak dan pengalaman program akan diberikan dari wawancara semi terstruktur, buku catatan, pendaftaran aplikasi, langkah-langkah hasil dan survei."))
    }

    fun getDataTips() = dataList
}