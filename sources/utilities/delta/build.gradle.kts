plugins {
    `java-library`
}

dependencies {
    api(files("libs/delta.jar"))
    //Was implementation, but let's see if runtimeOnly works.
    runtimeOnly("com.koloboke:koloboke-api-jdk8:1.0.0")
    runtimeOnly("com.koloboke:koloboke-impl-jdk8:1.0.0")
    runtimeOnly("com.koloboke:koloboke-impl-common-jdk8:1.0.0")
    runtimeOnly("com.google.code.findbugs:jsr305:3.0.2")
}
