import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.triggers.retryBuild
import jetbrains.buildServer.configs.kotlin.failureConditions.failOnText
import jetbrains.buildServer.configs.kotlin.failureConditions.BuildFailureOnText

version = "2023.11"

project {
    buildType(deploy)
    buildType(building)
    val projectName: String = DslContext.projectId.value.toLowerCase().substringAfter('_')
    params {
        param("env.MINIO_PREFIX", "$projectName.plan")
        param("env.SWARM_PREFIX", "$projectName")
    }
}

object deploy : BuildType({
    templates(AbsoluteId("deploy"))
    name = "deploy"
    vcs {
        root(DslContext.settingsRoot)
    }
})

object building : BuildType({
    templates(AbsoluteId("building"))
    name = "Building"

    val projectName: String = DslContext.projectId.value.toLowerCase().substringAfter('_')
    vcs {
        root(DslContext.settingsRoot)
        checkoutDir = "/var/www/www-data/data/www/discovery/$projectName/%teamcity.build.branch%-$projectName"
    }

})
