package com.example.trackingapp2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingapp2.service.MyAccessibilityService


class SplashActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_DRAW_OVER_OTHER_APP = 99
        const val REQUEST_ACCESSIBILITY_SERVICE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)

        checkDrawOverOtherApps()
    }

    private fun checkDrawOverOtherApps() {
        if (Settings.canDrawOverlays(this)) {
            checkAccessibilitySetting()
        }

        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, REQUEST_DRAW_OVER_OTHER_APP)
    }

    private fun checkAccessibilitySetting() {
        if (isAccessibilityEnable()) {
            goToMainActivity()
        } else {
            openAccessibilityServiceSetting()
        }
    }

    private fun openAccessibilityServiceSetting() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivityForResult(intent, REQUEST_ACCESSIBILITY_SERVICE)
    }

    private fun isAccessibilityEnable(): Boolean {
        val s = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return s.contains(getString(R.string.app_name))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_DRAW_OVER_OTHER_APP -> {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "Need to grant permission to continue using app", Toast.LENGTH_SHORT).show()
                    return
                }

                checkAccessibilitySetting()
            }
            REQUEST_ACCESSIBILITY_SERVICE -> {
                if (!isAccessibilitySettingsOn()) {
                    Toast.makeText(this, "Need to grant permission to continue using app", Toast.LENGTH_SHORT).show()
                    return
                }
                goToMainActivity()
            }
        }
    }

    private fun isAccessibilitySettingsOn(): Boolean {
        var accessibilityEnabled = 0
        val service = packageName + "/" + MyAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {

        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        } else {
        }
        return false
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}