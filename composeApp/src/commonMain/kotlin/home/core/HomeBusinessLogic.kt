package home.core

import home.core.ports.HomeStatePort

suspend fun HomeStatePort.initialize() {
    error.value = null
    progress.value = true
    runCatching { dataSourcePort.getAllRepositories() }
        .onSuccess { repositories.value = it }
        .onFailure { error.value = it }
    progress.value = false
}