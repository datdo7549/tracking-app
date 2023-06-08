package com.example.trackingapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.trackingapp2.databinding.ActivityMainBinding
import com.example.trackingapp2.service.FloatingBarService

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()

        val displayMetrics = resources.displayMetrics

        // swipe from middle of screen, 3/4 of the way down
        // up to middle of screen, 1/4 of the way from the top
        val middleX = (displayMetrics.widthPixels / 2).toFloat()
        val bottom = (displayMetrics.heightPixels * 0.95).toFloat()
        val top = (displayMetrics.heightPixels * 0.05).toFloat()

        Log.d("alo1234", "onCreate: $middleX $bottom $top")
    }

    private fun initView() = with(viewBinding) {
        btnOpenAppTest.setOnClickListener {
            val openAppIntent = packageManager.getLaunchIntentForPackage(Constant.PACKAGE_APP_TEST)
            if (openAppIntent != null) {
                openAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            try {
                startActivity(openAppIntent)
            } catch (e: Exception) {
                Log.d("MainActivity", "initView: ${e.message}")
            }
            val floatingBarServiceIntent = Intent(this@MainActivity, FloatingBarService::class.java)
            startService(floatingBarServiceIntent)
        }
    }
}