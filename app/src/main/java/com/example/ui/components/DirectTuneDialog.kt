package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalCockpitColors

@Composable
fun DirectTuneDialog(
  onDismiss: () -> Unit,
  onTune: (freq: String, band: String) -> Unit
) {
  val colors = LocalCockpitColors.current
  var currentInput by remember { mutableStateOf("98.5") }
  var selectedBand by remember { mutableStateOf("FM") }

  Dialog(onDismissRequest = onDismiss) {
    Box(
      modifier = Modifier
        .testTag("direct_tune_dialog")
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(colors.cardBackground)
        .border(2.dp, colors.primaryAccent, RoundedCornerShape(20.dp))
        .padding(20.dp)
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Radio,
              contentDescription = null,
              tint = colors.primaryAccent,
              modifier = Modifier.size(22.dp)
            )
            Text(
              text = "DIRECT FREQUENCY TUNE",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp,
              color = colors.textPrimary
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = colors.textSecondary
            )
          }
        }

        // Band Switcher (FM / AM)
        Row(
          modifier = Modifier
            .clip(CircleShape)
            .background(colors.surfaceContainer)
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf("FM", "AM").forEach { band ->
            val isSelected = selectedBand == band
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) colors.primaryAccent else colors.surfaceContainer)
                .clickable {
                  selectedBand = band
                  if (band == "AM" && currentInput.contains(".")) {
                    currentInput = "720"
                  } else if (band == "FM" && !currentInput.contains(".")) {
                    currentInput = "98.5"
                  }
                }
                .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
              Text(
                text = band,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) colors.onPrimaryAccent else colors.textSecondary
              )
            }
          }
        }

        // Large Readout Display
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surfaceContainer)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = currentInput.ifEmpty { "---.-" },
              fontSize = 42.sp,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Monospace,
              color = colors.primaryAccent
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (selectedBand == "FM") "MHz" else "kHz",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = colors.textSecondary
            )
          }
        }

        // Numeric Keypad
        val keypadRows = listOf(
          listOf("1", "2", "3"),
          listOf("4", "5", "6"),
          listOf("7", "8", "9"),
          listOf(".", "0", "DEL")
        )

        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          keypadRows.forEach { row ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              row.forEach { key ->
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.surfaceContainer)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(10.dp))
                    .clickable {
                      when (key) {
                        "DEL" -> {
                          if (currentInput.isNotEmpty()) {
                            currentInput = currentInput.dropLast(1)
                          }
                        }
                        "." -> {
                          if (selectedBand == "FM" && !currentInput.contains(".")) {
                            currentInput += "."
                          }
                        }
                        else -> {
                          if (currentInput.length < 6) {
                            if (currentInput == "0") currentInput = key else currentInput += key
                          }
                        }
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  if (key == "DEL") {
                    Icon(
                      imageVector = Icons.Default.Backspace,
                      contentDescription = "Delete",
                      tint = colors.textPrimary,
                      modifier = Modifier.size(20.dp)
                    )
                  } else {
                    Text(
                      text = key,
                      fontSize = 20.sp,
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.Monospace,
                      color = colors.textPrimary
                    )
                  }
                }
              }
            }
          }
        }

        // Tune Execution Button
        Button(
          onClick = {
            if (currentInput.isNotEmpty()) {
              onTune(currentInput, selectedBand)
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = colors.primaryAccent,
            contentColor = colors.onPrimaryAccent
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "TUNE FREQUENCY",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
        }
      }
    }
  }
}
