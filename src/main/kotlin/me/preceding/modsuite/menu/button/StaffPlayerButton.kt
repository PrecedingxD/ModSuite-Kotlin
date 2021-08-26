package me.preceding.modsuite.menu.button

import cc.fyre.proton.menu.Button
import me.preceding.modsuite.utils.CC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class StaffPlayerButton constructor(private val target: Player): Button() {

    override fun getName(player: Player): String {
        return CC.translate("&r${target.displayName}")
    }

    override fun getDescription(player: Player): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        list.add("")
        list.add(CC.translate("&6Mod Mode&7: " + (if (target.hasMetadata("modmode")) "&aEnabled" else "&cDisabled")))
        list.add(CC.translate("&6Invisible&7: " + (if (target.hasMetadata("invisible")) "&aEnabled" else "&cDisabled")))
        list.add("")
        return list
    }

    override fun getMaterial(player: Player): Material {
        return Material.SKULL_ITEM
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        when(clickType){
            ClickType.LEFT -> {
                val target: Player = Bukkit.getServer().getPlayer(ChatColor.stripColor(target.displayName))
                player.teleport(target)
                player.sendMessage(CC.translate("&6Teleported you to &r${target.displayName}&r&6."))
            }
            else -> {
                /* Empty */
            }
        }
    }

}