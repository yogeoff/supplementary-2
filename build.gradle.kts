//Community Plugins
plugins {
    id("com.diffplug.spotless") version "5.6.1";
    id("com.google.cloud.tools.jib") version "3.3.1";
    id("org.springframework.boot") version "3.3.2";
    id("jacoco");
    id("org.sonarqube") version "4.4.1.3373";
    id("java");
    id("eclipse");
    id("idea");
    id("io.spring.dependency-management") version "1.1.6";
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_18;
    targetCompatibility = JavaVersion.VERSION_18;
}

tasks {
    bootJar {
        archiveBaseName.set("VulnerableApp");
    }
}

configurations {
    configureEach {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging");
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

sonar {
  properties {
    property("sonar.projectKey", "yogeoff_supplementary-2")
    property("sonar.organization", "yogeoff-sonarqube-supplementary-2")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

spotless {
    java {
        // Don't enforce the license, just the format.
        clearSteps()
        googleJavaFormat("1.11.0")
    }
    format("javascript") {
    	target("src/main/resources/**/*.js");
//    	prettier().configFile("file.js");
  	}
}

jib {
    to {
        image = "sasanlabs/owasp-vulnerableapp:unreleased"
    }
    
    // Set up multi-platform build only if the task is not :jibDockerBuild
    if (!project.gradle.startParameter.taskNames.contains("jibDockerBuild")) {
        logger.info("JIB: Enabling Multi-Platform Images")

        from {
            image = "openjdk:8-jre-alpine"
            platforms {
                platform {
                    architecture = "amd64"
                    os = "linux"
                }
                platform {
                    architecture = "arm64"
                    os = "linux"
                }
                platform {
                    architecture = "386"
                    os = "linux"
                }
                platform {
                    architecture = "s390x"
                    os = "linux"
                }
                platform {
                    architecture = "ppc64le"
                    os = "linux"
                }
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.5"
    reportsDirectory = file("${layout.buildDirectory}/reports/jacoco.xml")
}

tasks.register("GenerateSampleVulnerability"){
	group = "SasanLabs"
	description = "Generates Sample Vulnerability template"
	println("Copying SampleVulnerability java file to org.sasanlabs.service.vulnerability.sampleVulnerability package");
	copy {
    	from(file("src/main/resources/sampleVulnerability/sampleVulnerability"))
    	into(file("src/main/java/org/sasanlabs/service/vulnerability/sampleVulnerability"))
    };
    println("Copy of java file is completed");
    println("Copying SampleVulnerability html/css/js files to static/templates/SampleVulnerability/LEVEL_1");
    copy {
    	from(file("src/main/resources/sampleVulnerability/staticResources/LEVEL_1"))
    	into(file("src/main/resources/static/templates/SampleVulnerability/LEVEL_1"))
    };
    println("Copy of html/css/js files is completed");
    println ("SampleVulnerability is generated !!!");
    enabled = false;
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.2");

    // https://mvnrepository.com/artifact/org.mockito/mockito-core
    testImplementation("org.mockito:mockito-core:3.5.13");

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0");

    // https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.17.2");

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.2");

    runtimeOnly("com.h2database:h2:2.2.220");
    
    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-text:1.10.0");
	//Log4j Dependencies
    implementation("org.springframework.boot:spring-boot-starter-log4j2:3.3.2");

    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20231013");

	//https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt
    implementation("com.nimbusds:nimbus-jose-jwt:9.37.2");

    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.7");
    
    implementation("io.github.sasanlabs:facade-schema:1.0.1");

    // https://mvnrepository.com/artifact/org.apache.commons/commons-fileupload2-jakarta-servlet6
    implementation("org.apache.commons:commons-fileupload2-jakarta-servlet6:2.0.0-M2");

    // https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2");
}

tasks {
    test {
        useJUnitPlatform();
        finalizedBy("jacocoTestReport");
    }

    jacocoTestReport {
        dependsOn("test");
        reports {
            xml.required.set(true);
            html.required.set(true);
            csv.required.set(true);
            xml.outputLocation = file("${layout.buildDirectory}/reports/jacoco.xml");
        }
    }
}
