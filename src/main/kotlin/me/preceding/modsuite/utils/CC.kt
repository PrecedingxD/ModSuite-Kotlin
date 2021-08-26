package me.preceding.modsuite.utils

import org.bukkit.ChatColor

object CC {

    @JvmStatic
    fun translate(message: String) : String {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}