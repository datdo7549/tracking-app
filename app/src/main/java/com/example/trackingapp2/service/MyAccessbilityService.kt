package com.example.trackingapp2.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import kotlinx.coroutines.*

class MyAccessibilityService : AccessibilityService(){

    private var isButtonTest2Visible = false

    companion object {
        private var accessibilityService: MyAccessibilityService? = null

        private fun setService(s: MyAccessibilityService) {
            this.accessibilityService = s
        }

        private var step = 5
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
       /* Log.d("MyAccessibilityService", "onAccessibilityEvent: ${event?.eventType}")
        val root: AccessibilityNodeInfo? = accessibilityService?.rootInActiveWindow ?: return

        root?.let {
            val btnTestNode = root.findAccessibilityNodeInfosByViewId("com.example.myapp2:id/btnTest2")
            if (btnTestNode.isEmpty()) {
                Log.d("MyAccessibilityService", "onAccessibilityEvent: gone")
                return
            } else {
                if (!isButtonTest2Visible) {
                    isButtonTest2Visible = true
                    buttonTest2Click()
                    Log.d("MyAccessibilityService", "onAccessibilityEvent: visible")
                }
            }
        }*/
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
//        super.onServiceConnected()
        val info = serviceInfo
        info.packageNames = null
        setService(this)
        Log.d("MyAccessibilityService", " >> service connected... ")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)
    }

    fun clickButton() {
        /*if (step > 0) {
            isButtonTest2Visible = false
            if (accessibilityService == null) {
                Toast.makeText(this, "Service is not running", Toast.LENGTH_SHORT).show()
                return
            }

            val root: AccessibilityNodeInfo? = accessibilityService?.rootInActiveWindow ?: return

            root?.let {
                val btnTestNode =
                    root.findAccessibilityNodeInfosByViewId("com.example.myapp2:id/btnTest")
                if (btnTestNode.isEmpty()) {
                    return
                }
                btnTestNode[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }

        } else {
            accessibilityService?.performGlobalAction(GESTURE_SWIPE_RIGHT_AND_LEFT)
        }
        return*/



        // swipe from middle of screen, 3/4 of the way down
        // up to middle of screen, 1/4 of the way from the top
        /*val middleX = (displayMetrics.widthPixels / 2).toFloat()
        val bottom = (displayMetrics.heightPixels * 0.75).toFloat()
        val top = (displayMetrics.heightPixels * 0.25).toFloat()*/
        val path = Path().apply {
            moveTo(540.0f, 1926.6f)
            lineTo(540.0f, 101.4f)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 100L, 200L))
            .build()

        accessibilityService?.dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    private fun buttonTest2Click() {
        val root: AccessibilityNodeInfo? = accessibilityService?.rootInActiveWindow ?: return
        GlobalScope.launch(Dispatchers.IO) {
            root?.let {
                val btnTestNode =
                    root.findAccessibilityNodeInfosByViewId("com.example.myapp2:id/btnTest2")
                if (btnTestNode.isEmpty()) {
                    return@launch
                }
                btnTestNode[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                step -= 1
                delay(1000)
                clickButton()

                withContext(Dispatchers.Main) {
                }

            }
        }
    }
}