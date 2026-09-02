import org.jetbrains.compose.desktop.application.dsl.TargetFormat

// تنظیمات نسخه دسکتاپ آشپزیار؛ target داخلی این ماژول JVM است و CI باید compileKotlinJvm را اجرا کند.
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AS-AshpazYar"
            packageVersion = "1.0.0"
        }
    }
}
