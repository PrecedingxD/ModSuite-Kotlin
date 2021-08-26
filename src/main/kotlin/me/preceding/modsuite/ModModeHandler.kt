package me.preceding.modsuite

import cc.fyre.proton.util.ItemBuilder
import lombok.Getter
import me.preceding.modsuite.utils.CC
import me.preceding.modsuite.utils.EnumArmorType
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

class ModModeHandler {

    @Getter private val modmoded = arrayListOf<UUID>()
    @Getter private val modmodeItems = hashMapOf<UUID, HashMap<Int, ItemStack>>()
    @Getter private val modmodeArmor = hashMapOf<UUID, HashMap<EnumArmorType, ItemStack>>()

    fun enableModMode(player: Player){
        player.sendMessage(CC.translate("&6Mod Mode: &aEnabled"))
        player.setMetadata("modmode", FixedMetadataValue(ModSuite.instance, true))
        player.setMetadata("invisible", FixedMetadataValue(ModSuite.instance, true))
        player.setMetadata("seeOtherStaff", FixedMetadataValue(ModSuite.instance, true))
        player.health = 20.0
        player.foodLevel = 20
        player.saturation = 10.0F
        modmoded.add(player.uniqueId)

        val armorMap = HashMap<EnumArmorType, ItemStack>()
        val itemMap = HashMap<Int, ItemStack>()

        if(player.inventory.boots != null) armorMap[EnumArmorType.BOOTS] = player.inventory.boots
        if(player.inventory.leggings != null) armorMap[EnumArmorType.LEGGINGS] = player.inventory.leggings
        if(player.inventory.chestplate != null) armorMap[EnumArmorType.CHESTPLATE] = player.inventory.chestplate
        if(player.inventory.helmet != null) armorMap[EnumArmorType.HELMET] = player.inventory.helmet
        for(i in 0..27){
            if(player.inventory.getItem(i) != null) {
                itemMap[i] = player.inventory.getItem(i)
                player.inventory.setItem(i, ItemStack(Material.AIR))
            }
        }
        Bukkit.getServer().scheduler.runTaskLaterAsynchronously(ModSuite.instance, {
            player.inventory.clear()
            player.inventory.boots = null
            player.inventory.leggings = null
            player.inventory.chestplate = null
            player.inventory.helmet = null
            applyModItems(player, player.hasPermission("worldedit.*"))
            player.updateInventory()
        }, 1L)
        modmodeItems[player.uniqueId] = itemMap
        modmodeArmor[player.uniqueId] = armorMap
    }

    fun disableModMode(player: Player){
        player.sendMessage(CC.translate("&6Mod Mode: &cDisabled"))
        player.removeMetadata("modmode", ModSuite.instance)
        player.removeMetadata("invisible", ModSuite.instance)
        player.removeMetadata("seeOtherStaff", ModSuite.instance)
        player.gameMode = GameMode.SURVIVAL
        player.isFlying = false
        player.allowFlight = false
        modmoded.remove(player.uniqueId)

        player.inventory.clear()

        for(i in 0..27){
            player.inventory.setItem(i, modmodeItems.getOrDefault(player.uniqueId, HashMap()).getOrDefault(i, ItemStack(Material.AIR)))
        }

        if(modmodeArmor[player.uniqueId]!!.containsKey(EnumArmorType.BOOTS)) player.inventory.boots = modmodeArmor[player.uniqueId]!![EnumArmorType.BOOTS]
        if(modmodeArmor[player.uniqueId]!!.containsKey(EnumArmorType.LEGGINGS)) player.inventory.leggings = modmodeArmor[player.uniqueId]!![EnumArmorType.LEGGINGS]
        if(modmodeArmor[player.uniqueId]!!.containsKey(EnumArmorType.CHESTPLATE)) player.inventory.chestplate = modmodeArmor[player.uniqueId]!![EnumArmorType.CHESTPLATE]
        if(modmodeArmor[player.uniqueId]!!.containsKey(EnumArmorType.HELMET)) player.inventory.helmet = modmodeArmor[player.uniqueId]!![EnumArmorType.HELMET]
    }

    fun isModmoded(player: Player) : Boolean {
        return player.hasMetadata("modmode")
    }

    fun isInvisible(player: Player) : Boolean {
        return player.hasMetadata("invisible")
    }

    private fun applyModItems(player: Player, admin: Boolean) = if(admin){
        player.gameMode = GameMode.CREATIVE
        player.inventory.setItem(0, ItemBuilder.of(Material.COMPASS).name(CC.translate("&bNavigation Compass")).build())
        player.inventory.setItem(1, ItemBuilder.of(Material.BOOK).name(CC.translate("&bInspect Inventory")).build())
        player.inventory.setItem(2, ItemBuilder.of(Material.WOOD_AXE).name(CC.translate("&bWorldEdit Wand")).build())
        player.inventory.setItem(3, ItemBuilder.of(Material.CARPET).name(CC.translate("&bBetter View")).data(14).build())
        player.inventory.setItem(7, ItemBuilder.of(Material.INK_SACK).name(CC.translate("&bBecome Visible")).data(8).build())
        player.inventory.setItem(8, ItemBuilder.of(Material.SKULL_ITEM).name(CC.translate("&bOnline Staff")).build())
        player.updateInventory()
    }else{
        player.gameMode = GameMode.SURVIVAL
        player.allowFlight = true
        player.inventory.setItem(0, ItemBuilder.of(Material.COMPASS).name(CC.translate("&bNavigation Compass")).build())
        player.inventory.setItem(1, ItemBuilder.of(Material.BOOK).name(CC.translate("&bInspect Inventory")).build())
        player.inventory.setItem(2, ItemBuilder.of(Material.CARPET).name(CC.translate("&bBetter View")).data(14).build())
        player.inventory.setItem(7, ItemBuilder.of(Material.INK_SACK).name(CC.translate("&bBecome Visible")).data(8).build())
        player.inventory.setItem(8, ItemBuilder.of(Material.SKULL_ITEM).name(CC.translate("&bOnline Staff")).build())
        player.updateInventory()
    }

}
