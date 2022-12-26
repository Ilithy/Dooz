/*
 *     Dooz/Dooz.app.main
 *     BluetoothGameContent.kt Copyrighted by Yamin Siahmargooei at 2022/12/26
 *     BluetoothGameContent.kt Last modified at 2022/12/26
 *     This file is part of Dooz/Dooz.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz/Dooz.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz/Dooz.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.dooz.content.online

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun BluetoothGameContent() {
    val state = rememberBluetoothGameState()

    val launcher =
        rememberLauncherForActivityResult(object : ActivityResultContract<Unit, Boolean>() {

            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                return resultCode == RESULT_OK
            }

        }) { ready -> state.isBluetoothEnabled.value = ready }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(32.dp)
        ) {
            Button(
                content = { Text("Enable Bluetooth") },
                onClick = { launcher.launch(Unit) }
            )
            Text(text = "Bluetooth support: ${state.isBluetoothSupported.value}")
            Text(text = "Bluetooth state: ${state.isBluetoothEnabled.value}")
        }
    }
}