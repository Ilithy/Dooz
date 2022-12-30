/*
 *     Dooz/Dooz.app.main
 *     BluetoothGameState.kt Copyrighted by Yamin Siahmargooei at 2022/12/26
 *     BluetoothGameState.kt Last modified at 2022/12/26
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

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class BluetoothGameState(
    private val context: Context,
    val isBluetoothSupported: MutableState<Boolean>,
    val isBluetoothPermissionsGranted: MutableState<Boolean>,
    val isBluetoothEnabled: MutableState<Boolean>,
    val isEnablingBluetooth: MutableState<Boolean>,
    val isScanning: MutableState<Boolean>,
    val devices: MutableState<Set<BluetoothDevice>>
) {
    private var manager: BluetoothManager? = null
    private var adapter: BluetoothAdapter? = null

    private val bluetoothIntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)

    val isReadyToScan: Boolean
        get() {
            return isBluetoothSupported.value &&
                    isBluetoothPermissionsGranted.value &&
                    isBluetoothEnabled.value &&
                    !isScanning.value
        }


    init {
        initBluetooth()
        isBluetoothEnabled.value = adapter?.isEnabled == true
    }

    fun startScan() {
        context.registerReceiver(receiver, bluetoothIntentFilter)
    }

    fun stopScan() {
        context.unregisterReceiver(receiver)
    }

    private fun initBluetooth() {
        manager = ContextCompat.getSystemService(context, BluetoothManager::class.java)
        adapter = manager?.adapter
        isBluetoothSupported.value = adapter != null
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null)
                    devices.value = buildSet { devices.value + device }
            }
        }
    }

}

@Composable
fun rememberBluetoothGameState(
    context: Context = LocalContext.current,
    isBluetoothSupported: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isBluetoothPermissionsGranted: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isBluetoothEnabled: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isEnablingBluetooth: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isScanning: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    devices: MutableState<Set<BluetoothDevice>> = remember { mutableStateOf(setOf()) }
) = remember(isBluetoothSupported) {
    BluetoothGameState(
        context,
        isBluetoothSupported,
        isBluetoothPermissionsGranted,
        isBluetoothEnabled,
        isEnablingBluetooth,
        isScanning,
        devices
    )
}