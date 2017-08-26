echo on
start java -cp h2-1.4.191.jar org.h2.tools.Server
timeout /t 1
rem gradlew wrapper bootRun
rem https://docs.gradle.org/3.5/userguide/gradle_command_line.html
rem plain | auto | rich
gradlew --console plain wrapper bootRun
