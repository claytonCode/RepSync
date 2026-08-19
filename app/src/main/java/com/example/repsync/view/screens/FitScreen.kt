package com.example.repsync.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.repsync.R
import com.example.repsync.ui.theme.Lime
import com.example.repsync.util.constants.FitnessStatType
import com.example.repsync.view.components.ActivitySection
import com.example.repsync.view.components.GoalReachedDialog
import com.example.repsync.view.components.cards.StatCard
import com.example.repsync.viewmodel.FitViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitScreen(
    viewModel: FitViewModel = hiltViewModel(),
    onHistoryClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val goalReached by viewModel.goalReached.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    var showGoalDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(
                        onClick = onHistoryClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = stringResource(
                                R.string.workout_history
                            )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            ActivitySection(
                calories = uiState.calories,
                goal = uiState.calorieGoal,
                progress = uiState.activeEnergyProgress
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        R.string.goal_calories,
                        uiState.calorieGoal
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                IconButton(
                    onClick ={showGoalDialog = true}
                ) {
                    Icon(
                        imageVector = Icons.Default.ChangeCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = uiState.elapsedTime.toComponents { hours, minutes, seconds, _ ->
                    val format = String.format(
                        Locale.getDefault(),
                        "%02d:%02d:%02d",
                        hours,
                        minutes,
                        seconds
                    )
                    format
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (uiState.isRunning) {
                            viewModel.pauseWorkout()
                        } else {
                            viewModel.startWorkout()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (uiState.isRunning) {
                            stringResource(R.string.pause)
                        } else {
                            stringResource(R.string.start)
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    onClick = {
                        viewModel.resetWorkout()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = Lime
                    )
                ) {

                    Text(
                        text = stringResource(R.string.reset),
                        fontWeight = FontWeight.Medium,

                        )
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = stringResource(R.string.activity),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                uiState.stats
                    .firstOrNull { it.type == FitnessStatType.STEPS }
                    ?.let { stat ->
                        StatCard(
                            stat = stat,
                            modifier = Modifier.weight(1f)
                        )
                    }

                uiState.stats
                    .firstOrNull { it.type == FitnessStatType.DISTANCE }
                    ?.let { stat ->
                        StatCard(
                            stat = stat,
                            modifier = Modifier.weight(1f)
                        )
                    }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            uiState.stats
                .firstOrNull { it.type == FitnessStatType.CALORIES }
                ?.let { stat ->
                    StatCard(
                        stat = stat,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }

    if (showGoalDialog) {
        CalorieGoalDialog(
            currentGoal = uiState.calorieGoal,
            onDismiss = {
                showGoalDialog = false
            },
            onSave = { goal ->
                viewModel.setCalorieGoal(goal)
                showGoalDialog = false
            }
        )
    }

    if (goalReached) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissGoalReached()
            },
            title = {
                Text(
                    text = stringResource(
                        R.string.goal_reached_title
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.goal_reached_message,
                        uiState.calorieGoal
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissGoalReached()
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.got_it
                        )
                    )
                }
            }
        )
    }

    if (goalReached) {
        GoalReachedDialog(
            calorieGoal = uiState.calorieGoal,
            onFinish = {
                viewModel.finishWorkout()
                viewModel.dismissGoalReached()
            },
            onKeepGoing = {
                viewModel.dismissGoalReached()
            }
        )
    }
}


@Composable
fun CalorieGoalDialog(
    currentGoal: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {

    var goalText by remember {
        mutableStateOf(
            currentGoal.toString()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.set_calorie_goal)
            )
        },

        text = {
            OutlinedTextField(
                value = goalText,
                onValueChange = { value ->
                    if (value.all { it.isDigit() }) {
                        goalText = value
                    }
                },
                label = {
                    Text(stringResource(R.string.calories))
                },
                suffix = {
                    Text(stringResource(R.string.calories_unit))
                },
                singleLine = true
            )
        },

        confirmButton = {

            Button(
                onClick = {

                    val goal =
                        goalText.toIntOrNull()

                    if (goal != null && goal > 0) {
                        onSave(goal)
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}