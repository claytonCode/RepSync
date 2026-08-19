package com.example.repsync.data.model

import com.example.repsync.util.constants.FitnessStatType

data class FitnessStat(
    val type: FitnessStatType,
    val value: String,
    val unit: String = "",
    val graph: List<Float> = emptyList()
)