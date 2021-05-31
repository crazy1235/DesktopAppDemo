import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList
import kotlin.random.Random

data class GroupMsg(
    @SerializedName("GroupId")
    val groupId: String = "",
    @SerializedName("From_Account")
    val fromAccount: String = "admin",
    @SerializedName("MsgRandom")
    val msgRandom: Int = Random.nextInt(10000),
    @SerializedName("MsgPriority")
    val msgPriority: String = "High", // 消息优先级
    @SerializedName("MsgBody")
    val msgBody: ArrayList<MsgBody>
)

data class C2CMsg(
    val SyncOtherMachine: Int = 2,
    @SerializedName("To_Account")
    val toAccount: String,
    val MsgLifeTime: Int = 60,
    @SerializedName("MsgRandom")
    val msgRandom: Int = Random.nextInt(10000),
    @SerializedName("MsgBody")
    val msgBody: ArrayList<MsgBody>
)

data class MsgBody(
    @SerializedName("MsgType") val type: String = "TIMCustomElem",
    @SerializedName("MsgContent") val content: MsgContent
)

data class MsgContent(@SerializedName("Data") val data: String, val Desc: String = "")

/**
 * toast
 */
data class ToastBean(
    val type: String = "",
    val text: String = "",
    val delay: Int = 10,
    val conditions: Conditions = Conditions("")
)

data class AlertDialogBean(
    val type: String = "",
    val alert: AlertInnerBean,
    val conditions: Conditions
)

data class AlertInnerBean(
    val icon: String = "",
    val title: String = "",
    val msg: String = "",
    @SerializedName("pass_btn")
    val passBtn: ButtonBean,
    @SerializedName("reject_btn")
    val rejectBtn: ButtonBean
)

data class ButtonBean(
    val action: String = "",
    val text: String = ""
)

data class Conditions(@SerializedName("target_user_id") val targetUserId: String)

data class EndLinkBean(
    val type: String = "",
    val text: String = "",
    val conditions: Conditions
)