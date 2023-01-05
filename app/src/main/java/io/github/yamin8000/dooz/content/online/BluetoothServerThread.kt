/*
 *     Dooz/Dooz.app.main
 *     BluetoothServerThred.kt Copyrighted by Yamin Siahmargooei at 2023/1/5
 *     BluetoothServerThred.kt Last modified at 2023/1/5
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
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

class BluetoothServerThread(
    adapter: BluetoothAdapter,
    private val onAccept: (BluetoothSocket?) -> Unit,
    private val onFailed: (Exception) -> Unit
) : Thread() {

    @SuppressLint("MissingPermission")
    private val serverSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(
        BluetoothConstants.sdpRecordName,
        UUID.fromString(BluetoothConstants.sdpUUID)
    )

    override fun run() {
        var isListening = true
        while (isListening) {
            val socket = try {
                serverSocket.accept()
            } catch (e: IOException) {
                onFailed?.invoke(e)
                null
            }
            onAccept?.invoke(socket)
            isListening = false
        }
    }

    fun cancel() {
        try {
            serverSocket.close()
        } catch (e: IOException) {
            onFailed?.invoke(e)
        }
    }
}