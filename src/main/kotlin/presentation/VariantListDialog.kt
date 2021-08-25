package presentation.component

import asPsProject
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.android.tools.idea.gradle.structure.model.PsProjectImpl
import com.android.tools.idea.gradle.structure.model.android.PsAndroidModule
import com.android.tools.idea.gradle.structure.model.android.PsAndroidModuleDefaultConfigDescriptors
import com.android.tools.idea.gradle.structure.model.meta.ParsedValue
import com.android.tools.idea.gradle.structure.model.meta.maybeValue
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBTextField
import getAndroidModuleModel
import getAndroidModuleModels
import getAndroidPsModule
import getAndroidPsModules
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel
import com.android.tools.idea.gradle.structure.model.helpers.parseString
import com.android.tools.idea.gradle.task.AndroidGradleTaskManager
import com.google.wireless.android.sdk.stats.PSDEvent
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskType
import org.jetbrains.plugins.gradle.service.task.GradleTaskManager
import org.jetbrains.plugins.gradle.util.GradleConstants

class VariantListDialog(
    val project: Project
) : DialogWrapper(true) {
    private val psProject = project.asPsProject()

    private var appModulesComboBox: AppModulesComboBox
    private var appIdTextField: JBTextField
    private var appVersionNameTextField: JBTextField
    private var variantTable: VariantTable

    var selectedModuleModel = project.getAndroidModuleModel()
    var selectedVariantNames = emptyList<String>()


    init {
        title = "Variants"

        // views
        appModulesComboBox = AppModulesComboBox(project.getAndroidModuleModels(), ::onAndroidModuleModelChanged)
        appIdTextField = JBTextField(project.getAndroidModuleModel()?.applicationId).apply { isEditable = false }
        appVersionNameTextField = JBTextField(project.asPsProject().getAndroidPsModules()[0].defaultConfig.versionName.maybeValue).apply { isEditable = false }
        variantTable = VariantTable(project, ::onVariantSelectionChanged)


//        project.asPsProject().getAndroidPsModules().forEach { am: PsAndroidModule ->
//            println("Android Ps Module: $am")
//            println("default config: ${am.defaultConfig}")
//            println("\tapplicationId: ${am.defaultConfig.applicationId.maybeValue}")
//            println("\tversionName: ${am.defaultConfig.versionName.maybeValue}")
//            println("\tversionCode: ${am.defaultConfig.versionCode.maybeValue}")
//            println("build types: ${am.buildTypes.items.toList()}")
//            println("flavor dimensions: ${am.flavorDimensions.items.toList()}")
//            println("product flavors: ${am.productFlavors.items.toList()}")
//            println("parsed model: ${am.parsedModel}")
//            am.parsedModel?.android()?.let { android ->
//                println("\tandroid: $android")
//
//                ApplicationManager.getApplication().runWriteAction {
//                    println("run write action")
//                    PsAndroidModuleDefaultConfigDescriptors.prepareForModification(am.defaultConfig)
//                    PsAndroidModuleDefaultConfigDescriptors.getParsed(am.defaultConfig)?.versionName()?.setValue("1.2")
//                    android.defaultConfig().versionName().setValue("1.2")
//                    PsAndroidModuleDefaultConfigDescriptors.setModified(am.defaultConfig)
//
//                    val property = PsAndroidModuleDefaultConfigDescriptors.versionName.bind(am.defaultConfig)
//                    property.setParsedValue(parseString("1.2").value)
//
//                    am.defaultConfig.versionName = parseString("1.2").value
//                    am.isModified = true
//                }
//            }
//        }
    }

    override fun createCenterPanel(): JComponent {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            add(appModulesComboBox)
            add(TitledSeparator("Configuration"))
            add(appIdTextField)
            add(appVersionNameTextField)
            add(variantTable)
        }
    }

    private fun onAndroidModuleModelChanged(moduleModel: AndroidModuleModel?) {
        println("android module model: $moduleModel")
        selectedModuleModel = moduleModel
    }

    private fun onVariantSelectionChanged(variantNames: List<String>) {
        println("variant names: $variantNames")
        selectedVariantNames = variantNames
    }

    companion object {
        fun show(project: Project): VariantListDialog {
            return VariantListDialog(project).apply {
                init()
                showAndGet()
            }
        }
    }
}