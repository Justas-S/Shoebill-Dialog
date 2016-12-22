package lt.maze.dialog

import net.gtaun.shoebill.constant.DialogStyle
import net.gtaun.shoebill.entities.Destroyable
import net.gtaun.shoebill.entities.DialogId
import net.gtaun.shoebill.entities.Player
import net.gtaun.util.event.EventManager

/**
 * @author Bebras
 * 2016.08.10.
 */
interface Dialog: Destroyable {

    val dialogId: DialogId
    val player: Player
    val eventManager: EventManager
    val style: DialogStyle

    var title: String
    var body: String
    var buttonOk: String
    var buttonCancel: String

    fun show()
    fun showParent()


}