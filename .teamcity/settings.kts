import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

version = "2023.11"

project {
    buildType(Run)
    params {
        text("env.TASK_ID", "", display = ParameterDisplay.PROMPT, allowEmpty = true)
        password("env.TASK_API_KEY", "")
        password("env.TASK_WEBHOOK", "")
        text("env.TASK_COMMENT", "", display = ParameterDisplay.PROMPT, allowEmpty = true)
        text("env.TASK_USEREMAIL", "", display = ParameterDisplay.PROMPT, allowEmpty = true)
        text("env.TASK_SITE_URL", "", display = ParameterDisplay.PROMPT, allowEmpty = true)
    }
}


object Run : BuildType({
    name = "Run"
    vcs {
       root(DslContext.settingsRoot)
    }

    steps {
        script {
            id = "WebhookTaskRunner"
            scriptContent = """
                #!/bin/bash

                # Определение переменных
                API_KEY="%env.TASK_API_KEY%"
                WEBHOOK="%env.TASK_WEBHOOK%"
                USEREMAIL="%env.TASK_USEREMAIL%"
                TASKID="%env.TASK_ID%"
                SITE_URL="%env.TASK_SITE_URL%"
                COMMENT="%env.TASK_COMMENT%"

                # Передача аргументов в PHP-файл
                php Chat/Comment/create.php ${'$'}API_KEY ${'$'}WEBHOOK ${'$'}USEREMAIL ${'$'}TASKID ${'$'}SITE_URL ${'$'}COMMENT
            """.trimIndent()
        }
    }
 })
