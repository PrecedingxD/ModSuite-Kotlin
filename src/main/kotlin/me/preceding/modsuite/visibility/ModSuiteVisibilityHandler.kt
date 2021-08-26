package me.preceding.modsuite.visibility

import cc.fyre.proton.visibility.action.VisibilityAction
import cc.fyre.proton.visibility.provider.VisibilityProvider
import me.preceding.modsuite.ModSuite
import org.bukkit.entity.Player

class ModSuiteVisibilityHandler: VisibilityProvider {

    override fun getAction(viewer: Player, player: Player): VisibilityAction {
        if(ModSuite.instance!!.modModeHandler!!.isInvisible(player) && !viewer.hasMetadata("seeOtherStaff")){
            return VisibilityAction.HIDE
        }
        return VisibilityAction.NEUTRAL
    }

}