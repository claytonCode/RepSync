package com.example.repsync.view.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.repsync.R
import com.example.repsync.data.model.FitnessStat
import com.example.repsync.util.constants.FitnessStatType
import com.example.repsync.view.components.MiniGraph
import java.util.Locale

@Composable
fun StatCard(
    stat: FitnessStat,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val title = when (stat.type) {
                FitnessStatType.CALORIES ->
                    stringResource(R.string.calories)

                FitnessStatType.STEPS ->
                    stringResource(R.string.steps)

                FitnessStatType.DISTANCE ->
                    stringResource(R.string.distance)
            }
            Text(
                text = title.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

        }

        Spacer(     modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stat.value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (stat.unit.isNotEmpty()) {
                Spacer( modifier = Modifier.width(5.dp))
                Text(
                    text = stat.unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MiniGraph(
            values = stat.graph,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        )
    }
}