import com.google.protobuf.gradle.id

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    id("com.google.protobuf")
}

android {
    namespace = "org.eclipse.kuksa.sdk"
    compileSdk = 34
    version = "1.0.0"

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
            }
        }
    }
}

dependencies {
    api(project(":vss-core")) // Models are exposed

    // needs to be api as long as we expose ProtoBuf specific objects
    api(libs.grpc.protobuf)

    implementation(kotlin("reflect"))

    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.stub)
    implementation(libs.tomcat.annotations)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    plugins {
        id("grpc") {
            artifact = libs.grpc.protoc.java.gen.get().toString()
        }
        generateProtoTasks {
            all().forEach {
                it.builtins {
                    create("java") {
                        option("lite")
                    }
                }
                it.plugins {
                    create("grpc") {
                        option("lite")
                    }
                }
            }
        }
    }
}
