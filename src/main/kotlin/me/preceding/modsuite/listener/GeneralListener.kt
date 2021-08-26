package me.preceding.modsuite.listener

import cc.fyre.proton.util.ItemBuilder
import me.preceding.modsuite.ModSuite
import me.preceding.modsuite.menu.OnlineStaffMenu
import me.preceding.modsuite.utils.CC
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.*
import org.bukkit.metadata.FixedMetadataValue

class GeneralListener : Listener {

    @EventHandler
    fun drop(event: PlayerDropItemEvent) {
        val player: Player = event.player
        if (ModSuite.instance!!.modModeHandler!!.isInvisible(player)) event.isCancelled = true
    }

    @EventHandler
    fun join(event: PlayerJoinEvent){
        val player: Player = event.player

        if(player.hasMetadata("modmode")){
            player.allowFlight = true
            player.isFlying = true
        }
    }



    @EventHandler
    fun pickup(event: PlayerPickupItemEvent) {
        val player: Player = event.player
        if (ModSuite.instance!!.modModeHandler!!.isInvisible(player)) event.isCancelled = true
    }

    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        val player: Player = event.player
        if (ModSuite.instance!!.modModeHandler!!.isModmoded(player)) {
            if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
                if (player.itemInHand != null && player.itemInHand.hasItemMeta() && player.itemInHand.itemMeta.hasDisplayName()) {
                    when (player.itemInHand.itemMeta.displayName) {
                        CC.translate("&bOnline Staff") -> {
                            ModSuite.instance!!.server.scheduler.runTask(ModSuite.instance) {
                                OnlineStaffMenu().openMenu(
                                    player
                                )
                            }
                        }
                        CC.translate("&bBecome Visible") -> {
                            player.removeMetadata("invisible", ModSuite.instance)
                            player.itemInHand =
                                ItemBuilder.of(Material.INK_SACK).name(CC.translate("&bBecome Invisible")).data(10)
                                    .build()
                            player.updateInventory()
                        }
                        CC.translate("&bBecome Invisible") -> {
                            player.setMetadata("invisible", FixedMetadataValue(ModSuite.instance, true))
                            player.itemInHand =
                                ItemBuilder.of(Material.INK_SACK).name(CC.translate("&bBecome Visible")).data(8).build()
                            player.updateInventory()
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun interactPhysical(event: PlayerInteractEvent) {
        val player: Player = event.player
        if (player.hasMetadata("modmode") && event.action == Action.PHYSICAL) {
            if (player.gameMode == GameMode.CREATIVE && player.hasMetadata("invisible")) {
                event.isCancelled = true
            } else if (player.gameMode == GameMode.SURVIVAL) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun saturation(event: FoodLevelChangeEvent){
        if(event.entity !is Player) return
        val player: Player = event.entity as Player
        if(player.hasMetadata("modmode")) {
            event.isCancelled = true
            player.saturation = 10.0F
        }
    }

    @EventHandler
    fun damage(event: EntityDamageEvent){
        if(event.entity !is Player) return
        val player: Player = event.entity as Player
        if(player.hasMetadata("modmode")) event.isCancelled = true
    }

    @EventHandler
    fun damageEntity(event: EntityDamageByBlockEvent){
        if(event.damager !is Player) return
        val player: Player = event.damager as Player
        if(player.hasMetadata("invisible")){
            event.isCancelled = true
            player.sendMessage(CC.translate("&cYou cannot do this in your current state."))
        }
    }

    @EventHandler
    fun changeGameMode(event: PlayerGameModeChangeEvent){
        val player: Player = event.player
        if(player.hasMetadata("modmode") && event.newGameMode == GameMode.SURVIVAL){
            Bukkit.getServer().scheduler.runTaskLaterAsynchronously(ModSuite.instance, {
                player.allowFlight = true
            }, 5L)
        }
    }

    @EventHandler
    fun interactPlayer(event: PlayerInteractEntityEvent) {
        if (event.rightClicked !is Player) return
        val player: Player = event.player
        val target: Player = event.rightClicked as Player
        if (ModSuite.instance!!.modModeHandler!!.isModmoded(player) && player.itemInHand != null && player.itemInHand.hasItemMeta() && player.itemInHand.itemMeta.hasDisplayName() && player.itemInHand.itemMeta.displayName == CC.translate(
                "&bInspect Inventory"
            )
        ) {
            Bukkit.getServer().dispatchCommand(player, "invsee ${target.name}")
        }
    }

    @EventHandler
    fun breakStuff(event: BlockBreakEvent) {
        val player: Player = event.player
        if (ModSuite.instance!!.modModeHandler!!.isModmoded(event.player) && player.gameMode != GameMode.CREATIVE) {
            event.isCancelled = true
            player.sendMessage(CC.translate("&cYou cannot do this in your current state."))
        }
    }

    @EventHandler
    fun placeStuff(event: BlockPlaceEvent) {
        val player: Player = event.player
        if (ModSuite.instance!!.modModeHandler!!.isModmoded(event.player) && player.gameMode != GameMode.CREATIVE) {
            event.isCancelled = true
            player.sendMessage(CC.translate("&cYou cannot do this in your current state."))
        }
    }

    @EventHandler
    fun interact2(event: PlayerInteractEvent) {
        val player: Player = event.player
        if (ModSuite.instance!!.modModeHandler!!.isInvisible(player) && player.gameMode != GameMode.CREATIVE && event.action == Action.RIGHT_CLICK_BLOCK) {
            if (event.clickedBlock == null) return
            if (event.clickedBlock.type.name.contains("BUTTON") || event.clickedBlock.type.name.contains("DOOR") || event.clickedBlock.type.name.contains(
                    "LEVER"
                ) || event.clickedBlock.type.name.contains("REDSTONE") || event.clickedBlock.type.name.contains(
                    "GATE"
                ) || event.clickedBlock.type.name.contains(
                    "CHEST"
                ) || event.clickedBlock.type.name.contains("DROPPER") || event.clickedBlock.type.name.contains("HOPPER") || event.clickedBlock.type.name.contains(
                    "DISPENSER"
                ) || event.clickedBlock.type.name.contains("DIODE")
            ) {
                event.isCancelled = true
                player.sendMessage(CC.translate("&cYou cannot do this in your current state."))
            }
        }
    }

}