package lt.maze.dialog

import net.gtaun.shoebill.`object`.DialogId
import net.gtaun.shoebill.`object`.Player
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.shoebill.event.player.PlayerDeathEvent
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerNode

/**
 * @author Bebras
 * 2016.08.10.
 */
abstract class AbstractDialog(override val player: Player, override val eventManager: EventManager) : Dialog {

    var shown = false
    var destroyed = false

    override val dialogId: DialogId = DialogId.create()
    override var title: String? = null
    override var body: String? = null
    override var buttonOk: String? = null
    override var buttonCancel: String? = null
    var parentDialogProvider: (() -> Dialog)? = null

    open var clickCancelHandler: ((AbstractDialog) -> Unit)? = null

    val eventNode: EventManagerNode = eventManager.createChildNode()

    open fun prepareBodyString(): String {
        val body = body
        if(body != null)
            return body
        else
            return ""
    }

    override fun show() {
        if(!isDestroyed && !shown) {
            dialogId.show(player, style, title, prepareBodyString(), buttonOk, buttonCancel)
            eventNode.registerHandler(DialogResponseEvent::class.java, { e ->
                if(e.dialog == dialogId) {
                    shown = false
                    eventNode.cancelAll()
                    if(e.dialogResponse == 1) {
                        // click ok
                        onClickOk(e)
                    } else {
                        // click cancel
                        onClickCancel()
                    }
                }
            })

            eventNode.registerHandler(PlayerDisconnectEvent::class.java, { e ->
                if(e.player == player) {
                    destroy()
                }
            })

            eventNode.registerHandler(PlayerDeathEvent::class.java, { e ->
                if(e.player == player) {
                    destroy()
                }
            })
        }
    }

    override fun showParent() {
        parentDialogProvider?.invoke()?.show()
    }

    abstract fun onClickOk(event: DialogResponseEvent)

    open fun onClickCancel() {
        clickCancelHandler?.invoke(this)
    }

    override fun isDestroyed(): Boolean {
        return destroyed
    }

    override fun destroy() {
        destroyed = true
        dialogId.destroy()
        eventNode.destroy()
    }

    override fun toString(): String {
        return String.format("%s[dialogId=%s;player=%s;title=%s;body=%s;buttonOk=%s;buttonCancel=%s;]",
                this.javaClass.name, dialogId.toString(), player.toString(), title?.toString(), body?.toString(), buttonOk?.toString(), buttonCancel?.toString())
    }

    abstract class AbstractDialogBuilder<T, V>(dialog: T) where T: AbstractDialog, V: AbstractDialogBuilder<T, V> {

        private val dialog = dialog

        @Suppress("UNCHECKED_CAST")
        open fun body(text: String): V {
            dialog.body = text
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun body(text: () -> String): V {
            body(text.invoke())
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun caption(caption: () -> String): V {
            dialog.title = caption.invoke()
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun buttonOk(buttonOk: () -> String): V {
            dialog.buttonOk = buttonOk.invoke()
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun buttonOk(buttonOk: String): V {
            dialog.buttonOk = buttonOk
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun buttonCancel(buttonCancel: () -> String): V {
            dialog.buttonCancel = buttonCancel.invoke()
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun onClickCancel(handler: (AbstractDialog) -> Unit): V {
            dialog.clickCancelHandler = handler
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun parent(dialog: AbstractDialog): V {
            dialog.parentDialogProvider = { dialog }
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        open fun parent(parentProvider: () -> AbstractDialog): V {
            dialog.parentDialogProvider = parentProvider
            return this as V
        }

        open fun build(): T {
            return dialog
        }
    }

}