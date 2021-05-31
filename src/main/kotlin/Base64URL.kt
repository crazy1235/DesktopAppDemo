import kotlin.Throws
import java.io.IOException
import java.util.*

object Base64URL {
    fun base64EncodeUrl(input: ByteArray?): ByteArray {
        val base64 = Base64.getEncoder().encode(input)
        for (i in base64.indices) when (base64[i]) {
            '+'.toByte() -> base64[i] = '*'.toByte()
            '/'.toByte() -> base64[i] = '-'.toByte()
            '='.toByte() -> base64[i] = '_'.toByte()
            else -> {
            }
        }
        return base64
    }

    @Throws(IOException::class)
    fun base64DecodeUrl(input: ByteArray): ByteArray {
        val base64 = input.clone()
        for (i in base64.indices) when (base64[i]) {
            '*'.toByte() -> base64[i] = '+'.toByte()
            '-'.toByte() -> base64[i] = '/'.toByte()
            '_'.toByte() -> base64[i] = '='.toByte()
            else -> {
            }
        }
        return Base64.getDecoder().decode(base64)
    }
}