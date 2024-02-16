package com.kotlinHero.tawfeer.common.utils

import androidx.navigation.NavController

sealed class ChannelAction {
    class Navigate(val navigationAction: suspend (NavController) -> Unit) : ChannelAction()
    class ShowSnackBar(val snackBarEvent: SnackBarEvent) : ChannelAction()
    data object CleanUp : ChannelAction()
}