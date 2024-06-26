package eryaz.software.zeusBase.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.core.notificationManager.NotificationHelper
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.databinding.ActivityMainBinding
import eryaz.software.zeusBase.ui.base.BaseActivity
import eryaz.software.zeusBase.util.KeyboardEventListener
import eryaz.software.zeusBase.util.StatusBarUtil
import eryaz.software.zeusBase.util.extensions.findNavHostNavController


class MainActivity : BaseActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        findNavHostNavController(R.id.nav_host)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucent(this)
        setContentView(binding.root)
        keyboardListener()
        requestPermissions()
        NotificationHelper.createNotificationChannel(this)
    }

    private fun keyboardListener() {
        KeyboardEventListener(
            activity = this,
            root = binding.root,
            resizeableView = binding.navHost,
            bottomView = null
        )
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onUserLeaveHint() {
        if (SessionManager.appIsLocked)
            startActivity(Intent(this, MainActivity::class.java))
        super.onUserLeaveHint()
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    override fun getContentView() = binding.root

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
