package com.sal7one.socketserver

import javax.swing.JOptionPane
import kotlin.system.exitProcess

fun exitDialog(appFrame: AppFrame) {
    if (JOptionPane.showConfirmDialog(
            appFrame,
            "Shutdown server?", "Close Window?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION
    ) {
        exitProcess(0)
    }
}