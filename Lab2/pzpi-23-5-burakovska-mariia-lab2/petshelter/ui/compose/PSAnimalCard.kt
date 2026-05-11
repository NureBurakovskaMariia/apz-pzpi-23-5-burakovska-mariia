package ua.nure.petshelter.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ua.nure.petshelter.repository.dto.AnimalDto
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun PSAnimalCard(
    animal: AnimalDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimension.small)
            .height(120.dp),
        shape = RoundedCornerShape(AppTheme.dimension.normal),
        colors = CardDefaults.cardColors(containerColor = AppTheme.color.cardBackground),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .padding(AppTheme.dimension.normal)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = animal.name,
                        style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = animal.status.uppercase(),
                        style = AppTheme.typography.small.copy(
                            color = AppTheme.color.active,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = "${animal.species} • ${animal.breed ?: "Unknown"}",
                    style = AppTheme.typography.small.copy(color = AppTheme.color.grey)
                )

                Text(
                    text = when (animal.gender?.uppercase()) {
                        "MALE" -> "♂ Male"
                        "FEMALE" -> "♀ Female"
                        else -> "Gender not specified"
                    },
                    style = AppTheme.typography.regular.copy(color = AppTheme.color.active)
                )
            }
        }
    }
}