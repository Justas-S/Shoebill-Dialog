package lt.maze.dialog

import net.gtaun.shoebill.constant.DialogStyle
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.util.event.EventManager

/**
 * @author Bebras
 * 2016.08.10.
 */
open class InputDialog(player: Player, eventManager: EventManager, val passwordMode: Boolean): AbstractDialog(player, eventManager) {

    private var clickOkHandler: ((InputDialog, String) -> Unit)? = null

    override val style: DialogStyle
        get() {
            if(passwordMode)
                return DialogStyle.PASSWORD
            else
                return DialogStyle.INPUT
        }

    override fun onClickOk(event: DialogResponseEvent) {
        clickOkHandler?.invoke(this, event.inputText)
    }


    companion object {
        fun create(player: Player, eventManager: EventManager, passwordMode: Boolean, init: InputDialogBuilder.() -> Unit): InputDialog {
            val builder = InputDialogBuilder(InputDialog(player, eventManager, passwordMode))
            builder.init()
            return builder.build()
        }

        fun create(player: Player, eventManager: EventManager, init: InputDialogBuilder.() -> Unit): InputDialog {
            return create(player, eventManager, false, init)
        }
    }

    abstract class AbstractInputDialogBuilder<T: InputDialog, V: AbstractInputDialogBuilder<T, V>>(val dialog: T): AbstractDialogBuilder<T, V>(dialog) {

        @Suppress("UNCHECKED_CAST")
        fun clickOk(handler: (InputDialog, String) -> Unit): V {
            dialog.clickOkHandler = handler
            return this as V
        }

    }

    class InputDialogBuilder(dialog: InputDialog): AbstractInputDialogBuilder<InputDialog, InputDialogBuilder>(dialog) {

    }
}