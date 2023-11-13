package binar.academy.challengebinar.utils

import binar.academy.challengebinar.R
import binar.academy.challengebinar.data.MenuItemEntity

object InitialDataUtil {
    fun getInitialDataMenu(): List<MenuItemEntity> {
        // Ambil data awal dari sumber tertentu
        return listOf(
            MenuItemEntity(
                menu_id = 1,
                menu_name = "Milkshake Pink",
                menu_category = "minuman",
                menu_price = 12000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.milkshake_pink,
                menu_description = "Nikmati kelezatan segar dengan Milkshake Strawberry kami. Dibuat dengan campuran lembut susu segar dan buah strawberry manis, setiap tegukan adalah ledakan rasa yang menggugah selera. Tekstur krimi dan aroma alami buah akan memanjakan lidah Anda.",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "Kopi Item",
                menu_category = "minuman",
                menu_price = 15000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.kopi,
                menu_description = "Dari biji pilihan hingga proses penyeduhan yang teliti, setiap gelas kopi adalah karya seni yang memukau lidah Anda. Rasakan kekayaan cita rasa dalam setiap tegukan, dari espresso yang kuat hingga latte lembut.",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "Ayam Oven",
                menu_category = "makanan",
                menu_price = 55000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.ayam_oven,
                menu_description = "Nikmati Kesempurnaan dalam Setiap Gigitan! Ayam Panggang Kami: Kulit yang Renyah, Daging yang Lezat, dan Rempah-Rempah yang Menggoda Selera. Pengalaman Sensasi Rasa yang Membuat Lidah Anda Bergoyang. Hidangan Istimewa ini Akan Membuat Anda Tersenyum Puas. Segera Sajikan untuk Kenikmatan Tiada Tanding!",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "Spagheti",
                menu_category = "makanan",
                menu_price = 25000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.spagheti,
                menu_description = "Pesta Sensasi di Mulut Anda! Spaghetti Al Dente yang Disajikan dalam Saus Tomat Segar yang Menggugah Selera, Disempurnakan dengan Taburan Keju Parmesan yang Gurih. Pengalaman Rasa Italia yang Autentik dalam Setiap Gigitan. Rasakan Kenikmatan Sejati dari Tradisi Kuliner Italia!",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "Burger",
                menu_category = "makanan",
                menu_price = 20000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.burger,
                menu_description = "Melejitkan Kenikmatan dalam Setiap Gigitan! Burger Istimewa Kami: Daging Panggang Sempurna, Sayuran Segar yang Meledak Rasa, dan Saus Khas yang Menggoda Selera. Pengalaman Rasa yang Menggetarkan Lidah Anda. Sajikan dengan Cinta, Nikmati dengan Gembira!",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "French Fries",
                menu_category = "makanan",
                menu_price = 15000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.french_fries,
                menu_description = "Gorengan Emas yang Menggoda Selera! French Fries Khas Kami: Potongan Kentang yang Garing Luar Biasa, Dengan Tekstur Empuk di Dalam. Disajikan dengan Saus Pilihan, Membuat Setiap Gigitan Sebuah Sensasi. Nikmati Kenikmatan Sederhana yang Tak Tergantikan!",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "Siomay",
                menu_category = "makanan",
                menu_price = 15000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.siomay,
                menu_description = "Kesenangan dalam Setiap Bungkusan! Siomay Istimewa Kami: Gulungan Tipis Kulit Tahu yang Menggoda, Dipenuhi dengan Isian Gurih dan Ikan Segar. Disajikan dengan Saus Kacang Pedas yang Membuat Lidah Anda Bergoyang. Rasakan Citra Rasa Autentik dari Kelezatan Asia!",
                menu_isrecommendation = false
            ),
            MenuItemEntity(
                menu_name = "Mie Kuah",
                menu_category = "makanan",
                menu_price = 15000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.mie_kuah,
                menu_description = "Kuah Hangat yang Mendalam, Mi Goreng yang Menggoda. Mie Kuah Spesial Kami: Mie Lembut yang Menyerap Aroma Kaldu Kaya Rasa, Dihiasi dengan Sayuran Segar dan Potongan Daging Pilihan. Rasakan Kelezatan Dalam Setiap Sendokannya. Pengalaman Rasa yang Membuat Hatimu Tersenyum!",
                menu_isrecommendation = true
            ),
            MenuItemEntity(
                menu_name = "Dimsum",
                menu_category = "makanan",
                menu_price = 15000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.dimsum,
                menu_description = "Nikmati kelezatan autentik Asia dalam setiap gigitan dengan menu dimsum kami. Dari siomay segar hingga bakpao lembut berisi daging pilihan, setiap hidangan merupakan perpaduan sempurna antara tekstur dan rasa yang memikat. Jelajahi berbagai pilihan, dari har gow klasik hingga rolade ikan kepiting yang menggoda selera.",
                menu_isrecommendation = false
            ),
            MenuItemEntity(
                menu_name = "Sushi",
                menu_category = "makanan",
                menu_price = 25000,
                menu_quantitiy = 20,
                menu_preview = R.drawable.sushi,
                menu_description = "Nikmati kelezatan sushi segar yang dipersiapkan dengan telaten. Setiap gulungan menggabungkan cita rasa alami bahan-bahan terbaik dengan sentuhan kreatif. Dari Nigiri elegan hingga Maki yang inovatif, setiap gigitan adalah pengalaman cita rasa yang memikat.",
                menu_isrecommendation = false
            )
        )
    }
}