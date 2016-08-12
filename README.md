# Shoebill-Dialog
An API for creating SAMP Shoebill Dialogs

This API is based on [Shoebill-common dialog package](https://github.com/Shoebill/shoebill-common/tree/master/src/main/java/net/gtaun/shoebill/common/dialog)

Its' purpose is simple: to provide easy dialog creation.


## Examples

### MsgBox dialog
```Kotlin

MsgBoxDialog.create(player, eventManager, {
	caption{ "A dialog caption" }
	body{ "Dialog body" }
	buttonOk{ "Yes" }
	buttonCancel{ "No" }
	onClickOk{ it.player.SendMessage("You clicked ok") }
	onClickCancel{ it.player.sendMessage("You clicked cancel") }
}).show()
```

## Thanks

Developers of [SAMP](http://sa-mp.com) mod

[123marvin123](https://github.com/123marvin123) and [mk12](https://github.com/mk124) for [Shoebill](https://github.com/Shoebill)