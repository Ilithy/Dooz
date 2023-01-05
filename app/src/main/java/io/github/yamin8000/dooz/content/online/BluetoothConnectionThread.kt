/*
 *     Dooz/Dooz.app.main
 *     BluetoothConnectionThread.kt Copyrighted by Yamin Siahmargooei at 2023/1/5
 *     BluetoothConnectionThread.kt Last modified at 2023/1/5
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

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.nio.charset.StandardCharsets

class BluetoothConnectionThread(
    private val socket: BluetoothSocket,
    private val onReadData: (String) -> Unit,
) : Thread() {

    private val inputStream = socket.inputStream
    private val outputStream = socket.outputStream

    private val buffer: ByteArray = ByteArray(1024)

    override fun run() {
        var bytes: Int
        while (true) {
            bytes = try {
                inputStream.read(buffer)
            } catch (e: IOException) {
                break
            }
            onReadData(String(buffer, 0, bytes, StandardCharsets.UTF_8))
        }
    }

    fun write(
        data: String
    ) {
        outputStream.write(data.toByteArray(StandardCharsets.UTF_8))
    }

    fun cancel() {
        try {
            socket.close()
        } catch (e: IOException) {
            //
        }
    }
}