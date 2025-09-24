plugins {
    id("java")
    id("org.xtext.xtend") version "4.0.0"
    //id("se.liu.ida.sas.pelab.genmodel")
}

sourceSets {
	main {
		java.srcDir("src/main/emf-gen")
	}
}


val eclipseTextVersion: String by project
val coreResourcesVersion: String by project
val xtextVersion: String by project
val emfMwe2Version: String by project
val ecoreXtextVersion: String by project
val emfCommonVersion: String by project
val emfVersion: String by project
val emfCodegenVersion: String by project

// Define a dedicated configuration for the MWE2 generator
val mwe2: Configuration by configurations.creating

dependencies {
    compileOnly(project(":genmodel"))
    compileOnly("org.eclipse.xtext:org.eclipse.xtext:$xtextVersion")
    mwe2("org.eclipse.text:org.eclipse.text:$eclipseTextVersion")
    mwe2("org.eclipse.core:org.eclipse.core.resources:$coreResourcesVersion")
    mwe2("org.eclipse.xtext:org.eclipse.xtext:$xtextVersion")
    mwe2("org.eclipse.xtext:org.eclipse.xtext.ecore:$xtextVersion")
    mwe2("org.eclipse.xtext:org.eclipse.xtext.common.types:$xtextVersion")
    mwe2("org.eclipse.xtext:org.eclipse.xtext.xtext.generator:$xtextVersion")
    mwe2("org.eclipse.emf:org.eclipse.emf.mwe2.launch:$emfMwe2Version")
    mwe2("org.eclipse.emf:org.eclipse.emf.mwe2.language:$emfMwe2Version")
    mwe2("org.eclipse.emf:org.eclipse.emf.mwe2.runtime:$emfMwe2Version")
    mwe2("org.eclipse.emf:org.eclipse.emf.mwe2.lib:$emfMwe2Version")
    mwe2("org.eclipse.emf:org.eclipse.emf.codegen.ecore.xtext:$ecoreXtextVersion")
    mwe2("org.eclipse.emf:org.eclipse.emf.common:$emfCommonVersion")
    mwe2("org.eclipse.emf:org.eclipse.emf.ecore:$emfVersion")
    mwe2("org.eclipse.emf:org.eclipse.emf.ecore.xmi:$emfVersion")
    mwe2("org.eclipse.emf:org.eclipse.emf.codegen:$emfCodegenVersion")
    mwe2("org.eclipse.emf:org.eclipse.emf.codegen.ecore:$emfCodegenVersion")

    implementation(project(":delta"))
}
 
val ecoreFile = layout.projectDirectory.file("model/tracemodel.ecore")
val genmodelFile = layout.projectDirectory.file("model/tracemodel.genmodel")

tasks.register<JavaExec>("generateGenmodel") {
    group = "codegen"
    description = "Generate .genmodel from .ecore"

    classpath = project(":genmodel").sourceSets["main"].runtimeClasspath
    mainClass.set("se.liu.ida.sas.pelab.probq.utilities.genmodel.GenerateGenmodelTask")

    //This is absolutely misconfigured, but at least recognized?
    args("--ecore="+ecoreFile.asFile.absolutePath, 
         "--genmodel="+genmodelFile.asFile.absolutePath,
         "--modeldir=/trace/src/main/emf-gen" //Specify target location of generated file (identical project name should be set here and in the .project file)
    )
    inputs.file(ecoreFile)
    outputs.file(genmodelFile)
}

tasks.register<JavaExec>("generateTraceCode") {
    group = "codegen"
    description = "Runs the EMF MWE2 workflow"

    classpath = mwe2
    mainClass.set("org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher")

    inputs.file("model/GenerateTraceCode.mwe2")
    inputs.file(genmodelFile)
    inputs.file(ecoreFile)
    outputs.dir("src/main/emf-gen")

    args(
        "/$projectDir/model/GenerateTraceCode.mwe2",  // path to your .mwe2 file
        "-p", "rootPath=$projectDir"   // This sets the platform URI
    )
}

tasks.named("generateTraceCode") {
    dependsOn("generateGenmodel")
}
tasks.named("compileJava") {
    dependsOn("generateTraceCode")
}
tasks.named("generateXtext") {
    dependsOn("generateTraceCode")
}
