import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object NetworkUtils {

    private val JSON = MediaType.parse("application/json; charset=utf-8")

    private val client = OkHttpClient()

    /**TLSSigAPIv2
     * get
     */
    fun get(url: String): String {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }

    /**
     * post json
     */
    fun postJson(url: String, json: String): String {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder().url(url).post(body).build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }
}