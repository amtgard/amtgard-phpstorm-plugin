import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

plugins {
    id("java")
    id("idea")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.1.0"
    `jacoco-report-aggregation`
    jacoco
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

val lombok_version = "1.18.36"
val jupiter_version = "5.11.4"
val mockito_version = "5.12.0"
val mockito_junit_extension_version = "5.13.0"

dependencies {
    intellijPlatform {
        phpstorm("2024.3")
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }
    compileOnly("org.projectlombok:lombok:" + lombok_version)
    annotationProcessor("org.projectlombok:lombok:" + lombok_version)
    testCompileOnly("org.projectlombok:lombok:" + lombok_version)
    testAnnotationProcessor("org.projectlombok:lombok:" + lombok_version)

    testImplementation("org.junit.jupiter:junit-jupiter-api:" + jupiter_version)
    testImplementation("org.junit.jupiter:junit-jupiter:" + jupiter_version)
    testImplementation("org.mockito:mockito-core:" + mockito_version)
    testImplementation("org.mockito:mockito-junit-jupiter:" + mockito_junit_extension_version)
}

intellijPlatform {
    instrumentCode = true
    projectName = project.name
    pluginConfiguration {
        //version = getVersion()
        //changeNotes = getChangeNotes().ifEmpty { "Everything! ✨" }
        description = getDescription()
    }
    signing {
        certificateChain = System.getenv("CERTIFICATE_CHAIN")
        privateKey = System.getenv("PRIVATE_KEY")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }
    publishing {
        token = System.getenv("PUBLISH_TOKEN")
    }
    pluginVerification {
        // ...

    }
}

tasks {

    withType<JavaCompile>().configureEach {
        options.release.set(17)
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget = JVM_17
    }

    providers.gradleProperty("javaVersion").map {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    patchPluginXml {
        sinceBuild = providers.gradleProperty("pluginSinceBuild")
        untilBuild = providers.gradleProperty("pluginUntilBuild").orNull
    }

    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    test {
        include("**/*Test.class")
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        finalizedBy("jacocoTestReport") // report is always generated after tests run
    }

    withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    jacocoTestReport {
        classDirectories.setFrom(instrumentCode)
    }

    jacocoTestCoverageVerification {
        classDirectories.setFrom(instrumentCode)
        violationRules {
            rule {
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = BigDecimal.valueOf(0.905)
                }
            }
        }
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                setExcludes(listOf(
                    "**/model/*.class",
                    "**/provider/*.class",
                    "**/pipeline/AbstractPipelineStage.class",
                    "**/PhpMethodCompletionContributor.class"
                ))
            }
        }))
    }

    jacocoTestReport {
        reports {
            xml.required = false
            csv.required = false
            html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
        }
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                setExcludes(listOf(
                    "**/model/*.class",
                    "**/provider/*.class",
                    "**/AbstractPipelineStage.class"
                ))
            }
        }))
    }

    check {
        dependsOn(jacocoTestCoverageVerification)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(providers.gradleProperty("javaVersion").get()))
    }
}

fun getGithubUrl(): String {
    return ""
}

fun getVersion(): String {
    return providers.gradleProperty("pluginReleaseVersion")
        .getOrElse("dev-${getGitCommitHash()}-${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)}")
}

fun getDescription(): String {
    return """
        Provides amtgard builder trait completions.<br>
        """
}

fun getGitCommitHash(): String {
    return providers.exec {
        commandLine("git", "rev-parse", "--short=6", "HEAD")
    }.standardOutput.asText.get().trim()
}

fun getChangeNotes(): String {
    val githubUrl = getGithubUrl()

    return providers.exec {
        commandLine(
            "/bin/sh",
            "-c",
            "git log \"\$(git tag --list | grep -E '^v[0-9]+\\.[0-9]+(\\.[0-9]+)?' |  sort -V | tail -n2 | tr '\\n' ' ' | awk '{print \$1\"..\"\$2}')\"  --no-merges --oneline --pretty=format:\"<li><a href='$githubUrl/commit/%H'>%h</a> %s <i style='color: gray;'>— %an</i></li>\""
        )
    }.standardOutput.asText.get().trim()
}
