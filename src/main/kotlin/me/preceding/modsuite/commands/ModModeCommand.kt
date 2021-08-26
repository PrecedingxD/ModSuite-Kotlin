package me.preceding.modsuite.commands

import cc.fyre.proton.command.Command
import cc.fyre.proton.command.param.Parameter
import me.preceding.modsuite.ModSuite
import me.preceding.modsuite.utils.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ModModeCommand {

    @JvmStatic
    @Command(names = ["modmode", "mod", "h", "staff", "staffmode", "v", "vanish"], permission = "modsuite.staff")
    fun modmode(sender: CommandSender, @Parameter(name = "target", defaultValue = "self") target: Player){
        if(target != sender && !sender.hasPermission("modsuite.admin")){
            sender.sendMessage(CC.translate("&cYou do not have permission to toggle others' modmode."))
            return
        }

        if(target != sender){
            sender.sendMessage(CC.translate("&aToggled &e" + (if (target.hasMetadata("modmode")) "off" else "on") + " &amodmode for &e${target.displayName}&r&a."))
        }

        if(ModSuite.instance!!.modModeHandler!!.isModmoded(target)){
            ModSuite.instance!!.modModeHandler!!.disableModMode(target)
        }else{
            ModSuite.instance!!.modModeHandler!!.enableModMode(target)
        }
    }

}