import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.locks.Condition
import java.util.logging.Level
import java.util.logging.Logger

object ContentState {

    // content type
    val contentType = mutableStateOf("")

    // content
    val content = mutableStateOf("")

    // user id or group id
    val targetId = mutableStateOf("")

    fun buildMsg() {
        if (isC2CType()) {
            buildC2CMsgResult(targetId.value, contentType.value)
        } else {
            buildGroupMsgResult(targetId.value, contentType.value)
        }
        Logger.getGlobal().log(Level.INFO, previewDialogContent.value)
    }

    private fun buildTypeContent(userId: String, msgType: String): String {
        // todo use content targetId contentType
        return when (msgType) {
            "toast" -> Gson().toJson(
                ToastBean(
                    text = content.value,
                    conditions = Conditions(userId)
                )
            )
            "alert_dialog" -> Gson().toJson(
                AlertDialogBean(
                    alert = AlertInnerBean(
                        msg = content.value,
                        passBtn = ButtonBean("OK", "确定"),
                        rejectBtn = ButtonBean("NO", "取消")
                    ),
                    conditions = Conditions(userId)
                )
            )
            "end_link" -> Gson().toJson(
                EndLinkBean(conditions = Conditions(userId))
            )
            else -> ""
        }
    }

    private fun buildGroupMsgResult(groupId: String, msgType: String) {
        previewDialogContent.value = Gson().toJson(
            GroupMsg(
                groupId = groupId,
                msgBody = arrayListOf(
                    MsgBody(
                        content = MsgContent(
                            data = buildTypeContent("", msgType)
                        )
                    )
                )
            )
        )
    }

    private fun buildC2CMsgResult(userId: String, msgType: String) {
        previewDialogContent.value = Gson().toJson(
            C2CMsg(
                toAccount = userId,
                msgBody = arrayListOf(
                    MsgBody(
                        content = MsgContent(
                            data = buildTypeContent(userId, msgType)
                        )
                    )
                )
            )
        )
    }

    // 预览弹窗
    val previewDialogContent = mutableStateOf("")

    val previewDialogState = mutableStateOf(false)

    var imTypeState = mutableStateOf("c2c")
    fun setCurrentC2CType() {
        imTypeState.value = "c2c"
    }

    fun setCurrentGroupType() {
        imTypeState.value = "group"
    }

    fun isC2CType() = imTypeState.value == "c2c"

    val inputIdLabel = mutableStateListOf("请输入用户Id", "请输入群组Id")

    // 发送im结果
    val sendResultState = mutableStateOf(false)
    val sendResult = mutableStateOf("")

    /**
     * 发送 IM
     */
    fun sendMsg() {
        sendResult.value =
            NetworkUtils.postJson(if (isC2CType()) Constants.c2cUrl else Constants.groupUrl, previewDialogContent.value)
        sendResultState.value = true
    }
}
