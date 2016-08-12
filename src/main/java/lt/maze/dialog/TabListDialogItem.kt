package lt.maze.dialog

/**
 * @author Bebras
 * 2016.08.11.
 */
open class TabListDialogItem(itemText: String, data: Any?, selectHandler: ((ListDialogItem) -> Unit)?, enabled: () -> Boolean):
        ListDialogItem(itemText, data, selectHandler, enabled) {

    constructor(): this("", null, null, { true })

    protected var tabSelectHandler: ((TabListDialogItem) -> Unit)? = null

    override var itemText: String = ""
        get() {
            println(columns.joinToString("\t" ))
            return columns.joinToString("\t")
        }
        set(value) {
            field = value
            columns = value.split("\t").toTypedArray()
        }

    var columns = arrayOf("", "", "", "")
        set(value) {
            itemText = value.joinToString("\t", limit = 4)
            if(value.size > 4)
                throw ColumnLimitReachedException("Dialogs can not have more than 4 columns")
            field = value
        }


    fun column(index: Int, text: String, data: Any?, enabled: () -> Boolean, selectHandler: ((TabListDialogItem) -> Unit)?) {
        columns[index] = text
        this.data = data
        this.enabled = enabled
        this.tabSelectHandler = selectHandler
    }

    fun column(index: Int, text: String) {
        column(index, text, null, { true }, null)
    }

    fun column(index: Int, text: String, selectHandler: (TabListDialogItem) -> Unit) {
        column(index, text, null, { true }, selectHandler)
    }

    fun column(index: Int, text: String, data: Any?) {
        column(index, text, data, { true }, null)
    }

    override fun onSelect() {
        tabSelectHandler?.invoke(this)
        super.onSelect()
    }

    companion object {
        fun create(init: TabListDialogItem.() -> Unit): TabListDialogItem {
            val item = TabListDialogItem()
            item.init()
            return item
        }
    }
}