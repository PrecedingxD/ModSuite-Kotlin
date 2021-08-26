package me.preceding.modsuite.menu

import cc.fyre.proton.menu.Button
import cc.fyre.proton.menu.Menu
import me.preceding.modsuite.menu.button.StaffPlayerButton
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class OnlineStaffMenu: Menu() {

    override fun getTitle(p0: Player?): String {
        return "Online Staff"
    }

    override fun getButtons(p0: Player?): MutableMap<Int, Button> {
        val buttons: MutableMap<Int, Button> = HashMap()
        var i = 0
        for(player in Bukkit.getServer().onlinePlayers){
            buttons[i++] = StaffPlayerButton(player)
        }
        return buttons
    }

}