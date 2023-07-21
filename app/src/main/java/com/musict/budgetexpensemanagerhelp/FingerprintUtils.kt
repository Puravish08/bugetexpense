package com.musict.budgetexpensemanagerhelp
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


object FingerprintUtils {
    private const val FINGERPRINT_PERMISSION_REQUEST_CODE = 100

    @SuppressLint("NewApi")
    fun isFingerprintSupported(context: Context): Boolean {
        val biometricManager = context.getSystemService(AppCompatActivity.BIOMETRIC_SERVICE) as BiometricManager
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    @SuppressLint("InlinedApi")
    fun isPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.USE_BIOMETRIC
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestFingerprintPermission(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.USE_BIOMETRIC),
                FINGERPRINT_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("NewApi")
    fun authenticateWithFingerprint(
        activity: AppCompatActivity,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val executor = activity.mainExecutor
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess.invoke()
            }

            override fun onAuthenticationFailed() {
                onError.invoke()
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Authentication")
            .setDescription("Scan your fingerprint to unlock the app.")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    }
}
