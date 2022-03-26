package haivv.learning.biometricapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import haivv.learning.biometricapplication.biometric.*
import haivv.learning.biometricapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                // handle negative button clicked
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val biometricPrompt = createBiometricPrompt(this, callback)
        val promptInfo = createPromptInfo(this)

        binding.btnFingerprint.setOnClickListener {
            if (detectedChangeFingerprint(SECRET_KEY_NAME)) {
                // handle detected change fingerprint
                return@setOnClickListener
            }

            if (canBiometric(this)) {
                authenticate(promptInfo, SECRET_KEY_NAME, biometricPrompt)
            }
        }

    }
}