package haivv.learning.biometricapplication.biometric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

const val SECRET_KEY_NAME: String = "biometric_application_secret_key"
private const val KEY_SIZE: Int = 256
const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES


fun getInitializedCipherForEncryption(keyName: String): Cipher {
    val cipher = getCipher()
    val secretKey = getOrCreateSecretKey(keyName)
    secretKey?.let {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    }
    return cipher
}

fun getInitializedCipher(keyName: String): Cipher {
    val cipher = getCipher()
    val secretKey = createSecretKey(keyName)
    secretKey?.let {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    }
    return cipher
}

fun detectedChangeFingerprint(keyName: String): Boolean {
    val cipher = getCipher()
    val secretKey = getOrCreateSecretKey(keyName)
    return try {
        secretKey?.let {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        }
        false
    } catch (ex: KeyPermanentlyInvalidatedException) {
        true
    }

}

private fun getCipher(): Cipher {
    val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
    return Cipher.getInstance(transformation)
}

fun getOrCreateSecretKey(keyName: String): SecretKey? {
    val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
    keyStore.load(null)
    keyStore.getKey(keyName, null)?.let { return it as SecretKey }

    return createSecretKey(keyName)
}

fun createSecretKey(keyName: String): SecretKey? {
    try {
        val paramsBuild = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuild.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuild.build()
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    } catch (ex: Exception) {
        return null
    }
}