package home.core

import home.core.ports.HomeStatePort

suspend fun HomeStatePort.initialize() {
    progress.value = true
    error.value = null
    runCatching { dataSourcePort.getAllRepositories() }
        .onSuccess { repositories.value = it }
        .onFailure { error.value = it }
    progress.value = false
}