import androidx.compose.desktop.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties


fun main() = Window(
    title = "发送腾讯IM", size = IntSize(450, 450)
) {

    MaterialTheme {
        Column(modifier = Modifier.padding(20.dp)) {

            chooseContentType()
            Spacer(Modifier.height(20.dp))

            inputContent()
            Spacer(Modifier.height(20.dp))

            chooseImType()
            Spacer(Modifier.height(20.dp))

            inputUserIdOrGroupId()
            Spacer(Modifier.height(20.dp))

            showPreviewContent()
            showSendResult()

            operateArea({
                ContentState.buildMsg()
                ContentState.previewDialogState.value = true
            }, {
                ContentState.buildMsg()
                ContentState.sendMsg()
            })
        }
    }
}

@Composable
fun chooseContentType() {

    var expanded by remember { mutableStateOf(false) }
    var contentType by remember { ContentState.contentType }

    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            shape = RoundedCornerShape(35f),
            onClick = {
                expanded = true
            }) {
            Text("点击选择消息类型")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    contentType = "toast"
                }) {
                    Text("toast")
                }
                Divider()
                DropdownMenuItem(onClick = {
                    expanded = false
                    contentType = "alert_dialog"
                }) {
                    Text("alert_dialog")
                }
                Divider()
                DropdownMenuItem(onClick = {
                    expanded = false
                    contentType = "dialog"
                }) {
                    Text("dialog")
                }
                Divider()
                DropdownMenuItem(onClick = {
                    expanded = false
                    contentType = "end_link"
                }) {
                    Text("end_link")
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(start = 10.dp),
            value = contentType,
            onValueChange = {},
            readOnly = true
        )
    }
}

@Composable
fun inputContent() {
    var text by remember { ContentState.content }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("请输入文本内容") },
            trailingIcon = {
                Icon(Icons.Filled.Delete, contentDescription = "", modifier = Modifier.clickable(onClick = {
                    text = ""
                }))
            }
        )
    }
}

@Composable
fun chooseImType() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "选择消息类型:",
            style = TextStyle(fontSize = 16.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis

        )
        Row(Modifier.selectableGroup().fillMaxWidth().padding(start = 15.dp, end = 15.dp).height(50.dp)) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically)
                    .selectable(
                        selected = ContentState.isC2CType(),
                        onClick = { ContentState.setCurrentC2CType() },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = ContentState.isC2CType(), onClick = null)
                Text(
                    text = "C2C",
                    style = typography.body2.merge(),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Row(
                Modifier
                    .wrapContentWidth()
                    .padding(start = 10.dp)
                    .selectable(
                        selected = !ContentState.isC2CType(),
                        onClick = { ContentState.setCurrentGroupType() },
                        role = Role.RadioButton
                    )
                    .align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = !ContentState.isC2CType(), onClick = null)
                Text(
                    text = "Group",
                    style = typography.body2.merge(),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }

}

@Composable
fun inputUserIdOrGroupId() {
    var text by remember { ContentState.targetId }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = { text = it },
        label = { Text(if (ContentState.isC2CType()) ContentState.inputIdLabel[0] else ContentState.inputIdLabel[1]) },
        trailingIcon = {
            Icon(Icons.Filled.Delete, contentDescription = "", modifier = Modifier.clickable(onClick = {
                text = ""
            }))
        }
    )
}

@Composable
fun operateArea(previewFun: (() -> Unit), sendFun: (() -> Unit)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(modifier = Modifier.wrapContentSize(align = Alignment.BottomEnd),
            onClick = {
                previewFun.invoke()
            }) {
            Text("预览")
        }

        Button(modifier = Modifier.wrapContentSize(align = Alignment.BottomEnd).padding(start = 20.dp),
            onClick = {
                sendFun.invoke()
            }) {
            Text("发送")
        }
    }

}

@Composable
fun showPreviewContent() {
    var dialogState by remember { ContentState.previewDialogState }

    if (dialogState) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(align = Alignment.Center).padding(10.dp),
            onDismissRequest = {
                dialogState = false
            }, text = {
                SelectionContainer {
                    Text(text = ContentState.previewDialogContent.value)
                }
            },
            buttons = {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    dialogState = false
                }) {
                    Text(text = "OK")
                }
            }, properties = DialogProperties(title = "Preview IM Data Structure", size = IntSize(400, 500))
        )
    }
}

@Composable
fun showSendResult() {
    var showState by remember { ContentState.sendResultState }
    if (showState) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(align = Alignment.Center).padding(10.dp),
            onDismissRequest = {
                showState = false
            }, text = {
                Text(text = ContentState.sendResult.value)
            },
            buttons = {
                Button(modifier = Modifier.wrapContentSize(), onClick = {
                    showState = false
                }) {
                    Text(text = "OK")
                }
            }, properties = DialogProperties(title = "发送结果")
        )
    }
}