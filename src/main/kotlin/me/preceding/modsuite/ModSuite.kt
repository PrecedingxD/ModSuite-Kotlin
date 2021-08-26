package me.preceding.modsuite

import cc.fyre.proton.Proton
import lombok.Getter
import me.preceding.modsuite.listener.GeneralListener
import me.preceding.modsuite.visibility.ModSuiteVisibilityHandler
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class ModSuite: JavaPlugin() {

    companion object {
        @Getter
        var instance: ModSuite? = null
    }

    var modModeHandler: ModModeHandler? = null

    override fun onEnable() {
        instance = this

        server.pluginManager.registerEvents(GeneralListener(), this)

        Proton.getInstance().commandHandler.registerAll(this)
        Proton.getInstance().visibilityHandler.registerHandler("ModSuite-Visibility-Handler", ModSuiteVisibilityHandler())

        modModeHandler = ModModeHandler()

    }

    override fun onDisable() {
        server.onlinePlayers.forEach {
            if (it.hasMetadata("modmode")) modModeHandler!!.disableModMode(it)
        }
    }

}