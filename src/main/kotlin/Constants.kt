object Constants {
    private const val appId = 111L
    private const val key = "111"
    private const val ver = "111"
    private const val c2cServiceName = "111"
    private const val command = "111"
    private const val identifier = "111"

    val c2cUrl =
        "https://console.tim.qq.com/$ver/$c2cServiceName/$command?sdkappid=$appId&identifier=$identifier&usersig=${
            getUserSign(
                identifier
            )
        }&random=123456&contenttype=json"

    val groupUrl =
        "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg?usersig=${getUserSign(identifier)}&identifier=$identifier&sdkappid=$appId&random=1234567&contenttype=json"

    fun getUserSign(userName: String): String {
        return TLSSigAPIv2(
            appId,
            key
        ).genUserSig(
            userName,
            60 * 5
        )
    }
}