package com.example.ctpa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ctpa.domain.model.Task
import com.example.ctpa.domain.model.TaskStatus
import com.example.ctpa.ui.theme.*

@Composable
fun TaskItem(task: Task, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Indicador de estado (Izquierda)
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(getStatusColor(task.status))
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    maxLines = 2
                )

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray500,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Footer de la tarea (varía según estado)
                when (task.status) {
                    TaskStatus.ACTIVE -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = Gray400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${task.elapsedMinutes}m / ${task.estimatedMinutes}m", style = MaterialTheme.typography.labelSmall, color = Gray500)
                        }
                    }
                    TaskStatus.IN_PROGRESS -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFF59E0B).copy(alpha = 0.1f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("In Progress", color = Color(0xFFD97706), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                    TaskStatus.COMPLETED -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Emerald500, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Signed at ${task.signedTime} • Verified by ${task.signedBy}", style = MaterialTheme.typography.labelSmall, color = Gray500)
                        }
                    }
                    TaskStatus.DONE -> {
                        if (task.photoCount > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Image, contentDescription = null, tint = Gray400, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${task.photoCount} Logged", style = MaterialTheme.typography.labelSmall, color = Gray500)
                            }
                        } else {
                            Text("1st Time", style = MaterialTheme.typography.labelSmall, color = Gray500)
                        }
                    }
                }
            }

            // Icono derecho (Cámara o Check)
            if (task.status == TaskStatus.COMPLETED || task.status == TaskStatus.DONE) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Emerald500,
                    modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                )
            } else {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                )
            }
        }
    }
}

private fun getStatusColor(status: TaskStatus): Color = when (status) {
    TaskStatus.ACTIVE -> Emerald500
    TaskStatus.IN_PROGRESS -> Color(0xFFF59E0B)
    TaskStatus.COMPLETED -> Color(0xFF3B82F6) // Azul
    TaskStatus.DONE -> Gray400
}