/*
 *     Dooz/Dooz.app.main
 *     Permissions.kt Copyrighted by Yamin Siahmargooei at 2022/12/30
 *     Permissions.kt Last modified at 2022/12/30
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

package io.github.yamin8000.dooz.ui.composables

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat

fun hasPermissions(
    context: Context,
    permissions: List<String>
): Boolean {
    if (permissions.isEmpty()) throw IllegalArgumentException("Permissions cannot be empty")
    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun GrantPermission(
    context: Context,
    permission: String,
    onGrant: (Boolean) -> Unit
) {
    GrantMultiplePermissions(
        context = context,
        permissions = listOf(permission),
        onGrant = { onGrant(it.values.first()) }
    )
}

@Composable
fun GrantMultiplePermissions(
    context: Context,
    permissions: List<String>,
    onGrant: (Map<String, Boolean>) -> Unit
) {
    val allGranted by remember {
        mutableStateOf(hasPermissions(context, permissions))
    }
    if (allGranted) {
        onGrant(buildMap {
            permissions.forEach { put(it, true) }
        })
    } else {
        var launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>? =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { onGrant(it) }
            )
        DisposableEffect(Unit) {
            launcher?.launch(permissions.toTypedArray())
            onDispose { launcher = null }
        }
        SideEffect { }
    }
}