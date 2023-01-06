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

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.ui.composables.GrantMultiplePermissions
import io.github.yamin8000.dooz.ui.composables.hasPermissions
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Preview
@Composable
fun BluetoothGameContent() {
    val state = rememberBluetoothGameState()

    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val permissions = listOf(
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_ADVERTISE,
            android.Manifest.permission.BLUETOOTH_SCAN
        )
        if (!hasPermissions(context, permissions)) {
            GrantMultiplePermissions(
                context = context,
                permissions = permissions,
                onGrant = { grants ->
                    val isAllGranted = grants.values.reduce { acc, next -> acc && next }
                    state.isBluetoothPermissionsGranted.value = isAllGranted
                }
            )
        } else state.isBluetoothPermissionsGranted.value = true
    }

    if (state.isBluetoothPermissionsGranted.value && state.isEnablingBluetooth.value) {
        EnableDiscoverableBluetooth(
            discoverability = state.isBluetoothEnabled.value,
            discoverabilityChange = { state.isBluetoothEnabled.value = it }
        )
    }

    Surface(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Button(
                    enabled = !state.isBluetoothEnabled.value,
                    content = { Text("Enable") },
                    onClick = { state.isEnablingBluetooth.value = true }
                )
                Button(
                    enabled = state.isReadyToScan,
                    content = { Text("Scan") },
                    onClick = {
                        state.isScanning.value = true
                        state.startScan()
                    }
                )
                Button(
                    enabled = state.isReadyToStartServer,
                    content = { Text("Start Server") },
                    onClick = {
                        state.scope.launch {
                            state.serverAccept()
                        }
                    }
                )
            }
            DevicesList(state.devices.value)
        }
    }
}

@Composable
private fun DevicesList(
    devices: Set<BluetoothDevice>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Test")
        }
        items(devices.toList()) {
            Text(
                text = it.address,
                modifier = Modifier.clickable {

                }
            )
        }
    }
}

@Composable
fun EnableDiscoverableBluetooth(
    discoverabilityDuration: Int = 300,
    discoverability: Boolean,
    discoverabilityChange: (Boolean) -> Unit
) {
    if (!discoverability) {
        val launcher =
            rememberLauncherForActivityResult(object : ActivityResultContract<Unit, Boolean>() {

                override fun createIntent(context: Context, input: Unit): Intent {
                    return Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                        discoverabilityDuration
                    )
                }

                override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                    return resultCode == discoverabilityDuration
                }

            }) { discoverabilityChange(it) }
        LaunchedEffect(Unit) { launcher.launch() }
    } else discoverabilityChange(true)
}