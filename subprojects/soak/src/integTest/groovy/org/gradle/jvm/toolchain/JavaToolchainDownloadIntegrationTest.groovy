/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.jvm.toolchain

import org.gradle.integtests.fixtures.AbstractPluginIntegrationTest

class JavaToolchainDownloadIntegrationTest extends AbstractPluginIntegrationTest {

    def "can download missing jdk automatically"() {
        buildFile << """
            apply plugin: "java"

            java {
                toolchain {
                    languageVersion = JavaVersion.VERSION_14
                }
            }
        """

        file("src/main/java/Foo.java") << "public class Foo {}"

        when:
        result = executer
            .withArguments("-Porg.gradle.java.installations.auto-detect=false", "--info")
            .withTasks("compileJava")
            .requireOwnGradleUserHomeDir()
            .run()

        then:
        outputContains("Compiling with toolchain '" +
            executer.gradleUserHomeDir.absolutePath +
            "/jdks/jdk-14.0.2+12/")
        javaClassFile("Foo.class").exists()
    }

}
