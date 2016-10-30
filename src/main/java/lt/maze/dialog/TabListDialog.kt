package lt.maze.dialog

import net.gtaun.shoebill.`object`.Player
import net.gtaun.shoebill.constant.DialogStyle
import net.gtaun.util.event.EventManager

/**
 * @author Bebras
 * 2016.08.11.
 */
open class TabListDialog(player: Player, eventManager: EventManager): ListDialog(player, eventManager) {

    protected var headers: MutableList<TabListDialogHeader> = mutableListOf()

    override val style: DialogStyle
        get() {
            if(headers.size == 0)
                return DialogStyle.TABLIST
            else
                return DialogStyle.TABLIST_HEADERS
        }

    override fun prepareBodyString(): String {
        if(headers.size > 0) {
            player.sendMessage(headers.map { it.text }.joinToString("\t"))
            player.sendMessage(headers.map { it.text }.joinToString("\t") + "\n" + super.prepareBodyString())
            return headers.map { it.text }.joinToString("\t") + "\n" + super.prepareBodyString()
        } else
            return super.prepareBodyString()
    }

    override fun destroy() {
        headers.clear()
        super.destroy()
    }

    companion object {
        fun create(player: Player, eventManager: EventManager, init: (TabListDialogBuilder.() -> Unit)): TabListDialog {
            val builder = TabListDialogBuilder(TabListDialog(player, eventManager))
            builder.init()
            return builder.build()
        }
    }

    abstract class AbstractTabListDialogBuilder<T: TabListDialog, V: AbstractTabListDialogBuilder<T, V>>(dialog: T): AbstractListDialogBuilder<T, V>(dialog) {

        fun header(header: () -> TabListDialogHeader): V {
            return header(header.invoke())
        }

        @Suppress("UNCHECKED_CAST")
        fun header(header: TabListDialogHeader): V {
            dialog.headers.add(header.index, header)
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        fun item(item: TabListDialogItem): V {
            dialog.items.add(item)
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        fun item(item: () -> TabListDialogItem): V {
            dialog.items.add(item.invoke())
            return this as V
        }
    }

    class TabListDialogBuilder internal constructor(dialog: TabListDialog): AbstractTabListDialogBuilder<TabListDialog, TabListDialogBuilder>(dialog) {

    }
}