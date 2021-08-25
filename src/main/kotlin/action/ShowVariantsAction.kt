package action

import com.android.AndroidProjectTypes.PROJECT_TYPE_APP
import com.android.AndroidProjectTypes.PROJECT_TYPE_INSTANTAPP
import com.android.tools.idea.apk.ApkFacet
import com.android.tools.idea.gradle.actions.BuildsToPathsMapper
import com.android.tools.idea.gradle.actions.GoToApkLocationTask
import com.android.tools.idea.gradle.project.build.invoker.GradleBuildInvoker
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.intellij.notification.EventLog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.wm.ToolWindowManager
import getAndroidModuleModels
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.idea.debugger.readAction
import presentation.component.VariantListDialog
import com.android.builder.model.AndroidProject.*
import java.io.File
import java.util.*


class ShowVariantsAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { project ->
            println("project: $project")
            val androidModules = ModuleManager.getInstance(project).modules
                .mapNotNull(AndroidFacet::getInstance)
                .mapNotNull(AndroidModuleModel::get)

            androidModules.forEach {
                println("android module: $it")
                println("application id: ${it.applicationId}")
                println("variant names: ${it.variantNames}")
                it.allSourceProviders.forEach { variant ->
//                    it.variantNames.forEach { v ->
//                        BuildVariantUpdater.getInstance(project).apply {
//                            updateSelectedBuildVariant(project, it.moduleName, v)
//                            println(it.mainArtifact.assembleTaskName)
//                        }
//                    }
                    println("\tvariant: $variant")
                }
            }

            ApkFacet.getInstance(ModuleManager.getInstance(project).modules[0])

            VariantListDialog.show(project).readAction {
                ToolWindowManager.getInstance(project).getToolWindow(EventLog.LOG_TOOL_WINDOW_ID)
                    ?.apply { activate(null, false) }


                val c = Collections.emptyList<Module>()
                ModuleManager.getInstance(project).modules
                    .filter {
                        AndroidFacet.getInstance(it)?.let { facet ->
                            AndroidModuleModel.get(facet)?.let {
                                if (it.androidProject.projectType == PROJECT_TYPE_APP
                                        || it.androidProject.projectType == PROJECT_TYPE_INSTANTAPP) {
                                    facet.properties.ASSEMBLE_TASK_NAME
                                }
                            }
                        } ?: false
                    }
                    .filter { AndroidModuleModel.get(it) != null }
                    .onEach { c.add(it) }

                val t = GoToApkLocationTask(project, c, "Hi Test", it.selectedVariantNames, null)

                GradleBuildInvoker.getInstance(project).apply {
                    cleanProject()
                    add(t)
                    executeTasks(
                        it.selectedVariantNames.map { ":app:assemble${it.first().toUpperCase()}${it.substring(1)}" }
                    )
//                    executeTasks(
//                        GradleBuildInvoker.Request(
//                            project,
//                            this.gr
//                        )
//                    )
                }
            }
        }
    }
}