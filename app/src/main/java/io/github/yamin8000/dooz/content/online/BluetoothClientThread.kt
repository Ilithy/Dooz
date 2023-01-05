/*
 *     Dooz/Dooz.app.main
 *     BluetoothClientThread.kt Copyrighted by Yamin Siahmargooei at 2023/1/5
 *     BluetoothClientThread.kt Last modified at 2023/1/5
 *     This file is part of Dooz/Dooz.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
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
import android.bluetooth.BluetoothSocket
import io.github.yamin8000.dooz.content.online.BluetoothConstants.sdpUUID
import java.io.IOException
import java.util.*

class BluetoothClientThread(
    private val adapter: BluetoothAdapter,
    device: BluetoothDevice,
    private val onSuccess: (BluetoothSocket?) -> Unit,
    private val onFailed: (Exception) -> Unit
) : Thread() {

    @SuppressLint("MissingPermission")
    private val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(
        UUID.fromString(sdpUUID)
    )

    @SuppressLint("MissingPermission")
    override fun run() {
        adapter.cancelDiscovery()
        try {
            socket.connect()
            onSuccess(socket)
        } catch (e: IOException) {
            onFailed(e)
        }
    }

    fun cancel() {
        try {
            socket.close()
        } catch (e: IOException) {
            onFailed.invoke(e)
        }
    }
}