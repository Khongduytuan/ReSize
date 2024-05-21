package com.eagletech.resize

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Constraints
import com.eagletech.resize.dataUser.MangerSharedPreferences
import com.eagletech.resize.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var flashLightStatus = false
    private lateinit var preferences: MangerSharedPreferences
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = MangerSharedPreferences.getInstance(this)
        if (!hasFlash()) {
            binding.toggleButton.isEnabled = false
            return
        }
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]
        checkUI()
        clickApp()
    }

    private fun clickApp() {
        binding.toggleButton.setOnClickListener {
            if (preferences.getBuy() > 0 || preferences.isPremiumBuy == true){
                flashLightStatus = !flashLightStatus
                toggleFlashLight(flashLightStatus)
                preferences.removeBuy()
                checkUI()
            } else{
                Toast.makeText(this, "You have to buy it to turn on the light", Toast.LENGTH_LONG).show()
            }

        }
        binding.topbar.pay.setOnClickListener {
            val intent = Intent(this, BuyTimeActivity::class.java)
            startActivity(intent)
        }

        binding.topbar.info.setOnClickListener {
            showInfoDialog()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkUI() {
        if (flashLightStatus) {
            binding.background.setBackgroundColor(resources.getColor(R.color.light, null))
            binding.toggleButton.text = "ON"
        } else {
            binding.background.setBackgroundColor(resources.getColor(R.color.dark, null))
            binding.toggleButton.text = "OF"
        }
    }

    private fun toggleFlashLight(status: Boolean) {
        try {
            cameraManager.setTorchMode(cameraId, status)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Show dialog cho dữ liệu SharePreferences
    private fun showInfoDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Information")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        if (preferences.isPremiumBuy == true) {
            dialog.setMessage("You have successfully registered")
        } else {
            val turns = (preferences.getBuy() / 2).toInt()
            dialog.setMessage("You have $turns turns use")
        }
        dialog.show()
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    override fun onResume() {
        super.onResume()
        checkUI()
    }
}