package me.preceding.modsuite.commands

import cc.fyre.proton.command.Command
import cc.fyre.proton.command.param.Parameter
import me.preceding.modsuite.ModSuite
import me.preceding.modsuite.utils.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

object StaffVisibilityCommands {

    @JvmStatic
    @Command(names = ["showstaff", "hidestaff", "togglestaffvis", "togglestaffvisibility"], permission = "modsuite.staff")
    fun showStaff(sender: CommandSender, @Parameter(name = "target", defaultValue = "self") target: Player){
        if(sender !is Player){
            sender.sendMessage(CC.translate("&cThis command may only be performed by players."))
            return
        }
        val player: Player = sender
        if(!player.hasMetadata("seeOtherStaff")){
            player.setMetadata("seeOtherStaff", FixedMetadataValue(ModSuite.instance, true))
            player.sendMessage(CC.translate("&aYou will now see vanished staff members."))
        }else{
            player.removeMetadata("seeOtherStaff", ModSuite.instance)
            player.sendMessage(CC.translate("&cYou will no longer see vanished staff members."))
        }
    }

    @JvmStatic
    @Command(names = ["amivis", "amivisible", "staffvis", "staffvisibility"], permission = "modsuite.staff")
    fun staffvis(sender: CommandSender, @Parameter(name = "target", defaultValue = "self") target: Player){
        sender.sendMessage(CC.translate("&6Visibility status of &r${target.displayName}&r&7:"))
        sender.sendMessage(CC.translate(" -> &6Mod Mode&7: &r" + if (target.hasMetadata("modmode")) "&aYes" else "&cNo"))
        sender.sendMessage(CC.translate(" -> &6Invisible&7: &r" + if (target.hasMetadata("invisible")) "&aYes" else "&cNo"))
    }
}