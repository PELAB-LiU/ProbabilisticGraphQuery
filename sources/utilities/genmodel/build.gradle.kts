plugins {
    id("java")
}

dependencies {
    compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.15.1")
    implementation("org.apache.maven:maven-plugin-api:3.9.11")
    implementation("org.gradle:gradle-tooling-api:7.3-20210825160000+0000")

    implementation("org.eclipse.emf:org.eclipse.emf.codegen.ecore:2.33.0")
    implementation("org.eclipse.emf:org.eclipse.emf.ecore:2.33.0")
    implementation("org.apache.maven.plugin-tools:maven-plugin-annotations:3.6.0")
}


/*application {
    // entry point of the generator
    mainClass.set("se.liu.ida.sas.pelab.probq.utilities.genmodel.GenerateGenmodelTask")
}*/


tasks.register<JavaExec>("generateGenModel") {
    group = "codegen"
    description = "Generates a .genmodel file from an .ecore file"

    // Example: if generator uses EMF libraries
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.example.generator.GenModelGenerator")

    // Convention: input and output passed as args
    val inputEcore = project.layout.buildDirectory.file("input/MyModel.ecore")
    val outputGenModel = project.layout.buildDirectory.file("generated/MyModel.genmodel")
    args(inputEcore.get().asFile.absolutePath, outputGenModel.get().asFile.absolutePath)
}

// expose output dir for other projects
val generatedDir = layout.buildDirectory.dir("generated")
val genmodelOutput = generatedDir.map { it.asFile }

tasks.register("cleanGenerated") {
    delete(generatedDir)
}