import java.lang.Exception
import java.nio.charset.Charset
import java.util.zip.Deflater
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.KeyFactory
import java.security.Signature
import java.util.*

class TLSSigAPIv2(private val sdkappid: Long, private val key: String) {
    /**
     * 生成userSig
     *
     * @param userId 用户名
     * @param expire userSig有效期，出于安全考虑建议为300秒，您可以根据您的业务场景设置其他值。
     * @return 生成的userSig
     */
    fun genUserSig(userId: String, expire: Int): String {
        return ""
    }

    /**
     * ECDSA-SHA256签名
     *
     * @param data 需要签名的数据
     * @return 签名
     */
    private fun sign(data: ByteArray): ByteArray? {
        try {
            val signer = Signature.getInstance("SHA256withECDSA")
            signer.initSign(initPrivateKey())
            signer.update(data)
            return signer.sign()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun initPrivateKey(): PrivateKey? {
        val encodedKey = Base64.getDecoder().decode(key.toByteArray())
        try {
            val keySpec = PKCS8EncodedKeySpec(encodedKey)
            val keyFactory = KeyFactory.getInstance("EC")
            return keyFactory.generatePrivate(keySpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}