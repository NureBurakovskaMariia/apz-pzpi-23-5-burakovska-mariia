package ua.nure.petshelter.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.petshelter.repository.dto.TaskDto
import ua.nure.petshelter.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun PSTaskCard(
    task: TaskDto,
    onStatusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = task.status.equals("completed", ignoreCase = true)
    val formattedDate = formatDate(task.dueDate)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .background(
                if (isCompleted) AppTheme.color.grey.copy(alpha = 0.1f)
                else AppTheme.color.active.copy(alpha = 0.1f)
            )
            .clickable { onStatusChange(!isCompleted) }
            .padding(AppTheme.dimension.normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(if (isCompleted) 0.6f else 1f)
        ) {
            Text(
                text = task.description,
                style = AppTheme.typography.regular.copy(
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                )
            )

            if (formattedDate != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Due: $formattedDate",
                    style = AppTheme.typography.small.copy(color = AppTheme.color.grey)
                )
            }
        }

        Spacer(modifier = Modifier.width(AppTheme.dimension.small))

        Checkbox(
            checked = isCompleted,
            onCheckedChange = { isChecked -> onStatusChange(isChecked) },
            colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.color.active,
                checkmarkColor = AppTheme.color.background,
                uncheckedColor = AppTheme.color.grey
            )
        )
    }
}

private fun formatDate(dateString: String?): String? {
    if (dateString.isNullOrBlank()) return null
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val parsedDate = inputFormat.parse(dateString)
        parsedDate?.let { outputFormat.format(it) } ?: dateString.substringBefore("T")
    } catch (e: Exception) {
        dateString.substringBefore("T")
    }
}

@Preview(showBackground = true)
@Composable
private fun PSTaskCardPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(AppTheme.color.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PSTaskCard(
                task = TaskDto(
                    id = 1,
                    volunteerId = 101,
                    description = "Walk the dogs in block A",
                    status = "pending",
                    dueDate = "2024-05-10T00:00:00.000Z"
                ),
                onStatusChange = {}
            )

            PSTaskCard(
                task = TaskDto(
                    id = 2,
                    volunteerId = 101,
                    description = "Clean the cat enclosures",
                    status = "completed",
                    dueDate = "2024-05-09T00:00:00.000Z"
                ),
                onStatusChange = {}
            )

            PSTaskCard(
                task = TaskDto(
                    id = 3,
                    volunteerId = 101,
                    description = "Feed puppies",
                    status = "pending",
                    dueDate = null
                ),
                onStatusChange = {}
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PSTaskCardDarkPreview() {
    PSTaskCardPreview()
}