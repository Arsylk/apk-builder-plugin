package presentation.component

import javax.swing.table.AbstractTableModel
import com.intellij.openapi.project.Project
import getAndroidModuleModel
import variantNamesList
import java.awt.Dimension
import javax.swing.JTable
import javax.swing.event.ListSelectionEvent
import javax.swing.table.JTableHeader
import javax.swing.table.TableColumnModel


class VariantTableModel(val project: Project) : AbstractTableModel() {
    enum class Columns(name: String) {
        VARIANT("Variant"),
        APK("Apk"),
        BUILD("Build");
    }


    override fun getColumnName(column: Int): String = Columns.values()[column].name

    override fun getColumnCount(): Int = Columns.values().size

    override fun getRowCount(): Int {
        return project.getAndroidModuleModel()?.variantNames?.size ?: 0
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        return when (Columns.values()[columnIndex]) {
            Columns.VARIANT -> project.getAndroidModuleModel()?.variantNamesList()?.get(rowIndex)
            Columns.APK -> project.getAndroidModuleModel()?.applicationId
            Columns.BUILD -> true
        }
    }
}

class VariantTable(
    val project: Project,
    onSelectionChanged: (variantNames: List<String>) -> Unit = {}
) : JTable(VariantTableModel(project)) {

    init {
        preferredScrollableViewportSize = Dimension(500, 70)
        fillsViewportHeight = true
        setTableHeader(JTableHeader())
        getSelectionModel().addListSelectionListener {
            onSelectionChanged(selectedRows.toList().mapNotNull { project.getAndroidModuleModel()?.variantNamesList()?.get(it) })
        }
    }
}