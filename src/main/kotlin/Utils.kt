import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.android.tools.idea.gradle.structure.model.PsProject
import com.android.tools.idea.gradle.structure.model.PsProjectImpl
import com.android.tools.idea.gradle.structure.model.android.PsAndroidModule
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet


fun Project.asPsProject(): PsProjectImpl = PsProjectImpl(this)

fun PsProject.getAndroidPsModules(): List<PsAndroidModule> {
    return modules.mapNotNull { it as? PsAndroidModule }
}

fun PsProject.getAndroidPsModule(): PsAndroidModule? {
    return getAndroidPsModules()
        .firstOrNull()
}

fun Project.getAndroidModuleModels(): List<AndroidModuleModel> {
    return ModuleManager.getInstance(this).modules
        .mapNotNull(AndroidFacet::getInstance)
        .mapNotNull(AndroidModuleModel::get)
}

fun Project.getAndroidModuleModel(): AndroidModuleModel? {
    return getAndroidModuleModels()
        .firstOrNull()
}

fun AndroidModuleModel.variantNamesList(): List<String> {
    return variantNames.toList().sorted()
}