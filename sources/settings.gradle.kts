plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "probquery"
include(
//    "core/transformation", 
    "core/trace",
    "utilities/delta",
    "utilities/genmodel"
)

for (project in rootProject.children) {
    project.projectDir = file(project.name)
    project.name = project.name.substringAfterLast('/')
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.eclipse.org/content/groups/releases/")
        //mavenLocal()
        //maven("https://download.eclipse.org/modeling/emf/emf/updates/releases/")
        //maven("https://download.eclipse.org/modeling/tmf/xtext/updates/releases/")
    }
}
