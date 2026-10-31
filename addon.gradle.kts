import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.language.jvm.tasks.ProcessResources

tasks.withType<JavaExec>().configureEach {
    if (name.startsWith("runServer")) {
        doFirst {
            val clientOnly = listOf("angelica", "ModularUI2", "catalogue", "wawelauth")
            classpath = classpath.filter { file -> clientOnly.none { file.name.contains(it, ignoreCase = true) } }
        }
    }
}
val testToolchains = extensions.getByType<JavaToolchainService>()
tasks.withType<Test>().configureEach {
    javaLauncher.set(testToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(8)) })
}
tasks.named<ProcessResources>("processResources") {
    from("LICENSE", "COPYING", "COPYING.LESSER")
}
extensions.configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        groupId = "industries.angelic.cherishedworlds"
        pom {
            name.set("Cherished Worlds Legacy")
            description.set("Client-side Cherished Worlds backport for Minecraft 1.7.10.")
            url.set("https://github.com/AngelicIndustries/CherishedWorldsLegacy")
            licenses { license { name.set("LGPL-3.0-or-later"); url.set("https://www.gnu.org/licenses/lgpl-3.0.html") } }
        }
    }
}
