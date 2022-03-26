package haivv.learning.biometricapplication.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import haivv.learning.biometricapplication.R

fun createBiometricPrompt(
    fragmentActivity: FragmentActivity,
    authenticationCallback: BiometricPrompt.AuthenticationCallback
): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(fragmentActivity)

    return BiometricPrompt(fragmentActivity, executor, authenticationCallback)
}

fun createPromptInfo(fragmentActivity: FragmentActivity): BiometricPrompt.PromptInfo =
    BiometricPrompt.PromptInfo.Builder().apply {
        setTitle(fragmentActivity.getString(R.string.prompt_info_title))
        setSubtitle(fragmentActivity.getString(R.string.prompt_info_subtitle))
        setDescription(fragmentActivity.getString(R.string.prompt_info_description))
        setConfirmationRequired(false)
        setNegativeButtonText(fragmentActivity.getString(R.string.prompt_info_use_app_password))
    }.build()

fun authenticate(
    promptInfo: BiometricPrompt.PromptInfo,
    secretKey: String,
    biometricPrompt: BiometricPrompt
) {
    val cipher = getInitializedCipherForEncryption(secretKey)
    biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
}

fun supportedBiometric(context: Context): Boolean {
    return BiometricManager.from(context)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
}

fun enrolledBiometric(context: Context): Boolean {
    return BiometricManager.from(context)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
}

fun canBiometric(context: Context): Boolean {
    return BiometricManager.from(context)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
}
