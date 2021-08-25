package presentation.component

import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.intellij.openapi.ui.ComboBox
import java.awt.event.ItemEvent

class AppModulesComboBox(
    private val androidModules: List<AndroidModuleModel>,
    private val onChanged: (AndroidModuleModel?) -> Unit = {},
) : ComboBox<AppModulesComboBox.Item>(androidModules.map { Item(it) }.toTypedArray()) {

    init {
        addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                onChanged(androidModules.getOrNull(selectedIndex))
            }
        }
    }

    data class Item(
        val androidModule: AndroidModuleModel,
    ) {
        override fun toString(): String = androidModule.moduleName
    }
}