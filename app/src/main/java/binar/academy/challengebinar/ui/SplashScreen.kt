package binar.academy.challengebinar.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import binar.academy.challengebinar.R

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIMEOUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // Pindah ke aktivitas berikutnya setelah SPLASH_TIMEOUT
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup aktivitas splash screen agar tidak dapat dikembalikan
        }, SPLASH_TIMEOUT)
    }
}