package com.example.trackingapp2.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.example.trackingapp2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FloatingBarService: Service() {
    private var windowManager: WindowManager? = null
    private var windowManagerParams: WindowManager.LayoutParams? = null

    private lateinit var floatingWidget: View
    private lateinit var btnSpam: Button
    private lateinit var btnStop: Button

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflateView(inflater)
        return START_STICKY
    }

    private fun inflateView(layoutInflater: LayoutInflater) {
        floatingWidget = layoutInflater.inflate(R.layout.floating_bar_widget, null)
        windowManagerParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        windowManagerParams?.gravity = Gravity.TOP or Gravity.START
        windowManagerParams?.x = 30
        windowManagerParams?.y = 250

        windowManager?.addView(floatingWidget, windowManagerParams)

        btnSpam = floatingWidget.findViewById(R.id.btnSpam)
        btnStop = floatingWidget.findViewById(R.id.btnStop)

        val myAccessibilityService = MyAccessibilityService()

        btnSpam.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                myAccessibilityService.clickButton()
            }
        }

        btnStop.setOnClickListener {

        }
    }
}