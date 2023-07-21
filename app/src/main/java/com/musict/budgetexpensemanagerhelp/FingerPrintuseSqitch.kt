package com.musict.budgetexpensemanagerhelp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class FingerPrintuseSqitch : AppCompatActivity() {
//    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finger_printuse_sqitch)


//        imageView = findViewById(R.id.imgFingerPrint)

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Please Verify")
                .setDescription("User Authentication")
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                .setDeviceCredentialAllowed(false)
                .build()

            getPrompt().authenticate(promptInfo)
        }


    private fun getPrompt(): BiometricPrompt {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@FingerPrintuseSqitch, errString, Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val intent = Intent(this@FingerPrintuseSqitch, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@FingerPrintuseSqitch, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
        return BiometricPrompt(this, executor, callback)
    }




//        if (isBiometricSupported()) {
//            showBiometricPrompt()
//        } else {
//            // Handle the case when biometric authentication is not supported
//        }
//    }


//
//    private fun showBiometricPrompt() {
//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric Authentication")
//            .setSubtitle("Log in using your biometric credential")
//            .setNegativeButtonText("Cancel")
//            .build()
//
//        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                    // Handle authentication error
//                    showMessage("Authentication error: $errString")
//                }
//
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                    // Handle authentication success
//                    showMessage("Authentication succeeded!")
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    // Handle authentication failure
//                    showMessage("Authentication failed.")
//                }
//            })
//
//        biometricPrompt.authenticate(promptInfo)
//    }
//
//    private fun showMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun isBiometricSupported(): Boolean {
//        val biometricManager = BiometricManager.from(this)
//        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> {
//                // The user can authenticate with biometrics, continue with the authentication process
//                return true
//            }
//
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                // Handle the error cases as needed in your app
//                return false
//            }
//
//            else -> {
//                // Biometric status unknown or another error occurred
//                return false
//            }
//        }
//    }


    }
