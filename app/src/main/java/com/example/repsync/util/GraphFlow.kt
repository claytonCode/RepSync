package com.example.repsync.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

fun <T> Flow<T>.history(maxSize: Int): Flow<List<T>> =
    scan(emptyList()) { history, value ->
        (history + value).takeLast(maxSize)
    }