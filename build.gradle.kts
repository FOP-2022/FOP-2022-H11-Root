import org.sourcegrade.submitter.submit

plugins {
    java
    application
    id("org.sourcegrade.style") version "1.2.0"
    id("org.sourcegrade.submitter") version "0.4.0"
}

version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

submit {
    assignmentId = "h11"
    studentId = "ab12cdef"
    firstName = "sol_first"
    lastName = "sol_last"
}

// It is (for now) important to create the grader sourceSet AFTER the "submit" task has been configured.
// This is to prevent the grader from being present in the submission jar

val grader: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.test.get().compileClasspath
    runtimeClasspath += output + compileClasspath
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    "graderImplementation"("org.sourcegrade:jagr-launcher:0.4.0-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

application {
    mainClass.set("h11.Main")
}

tasks {
    val runDir = File("build/run")
    named<JavaExec>("run") {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
    val graderTest by creating(Test::class) {
        group = "verification"
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        testClassesDirs = grader.output.classesDirs
        classpath = grader.runtimeClasspath
        useJUnitPlatform()
    }
    named("check") {
        dependsOn(graderTest)
    }
    create<Jar>("graderJar") {
        group = "build"
        afterEvaluate {
            archiveFileName.set("FOP-2022-H11-${project.version}.jar")
            from(sourceSets.main.get().allSource)
            from(sourceSets.test.get().allSource)
            from(grader.allSource)
        }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    jar {
        enabled = false
    }
}
