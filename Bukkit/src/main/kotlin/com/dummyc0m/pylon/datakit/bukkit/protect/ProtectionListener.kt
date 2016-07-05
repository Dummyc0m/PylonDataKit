package com.dummyc0m.pylon.datakit.bukkit.protect

import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*

/**
 * Created by Dummy on 7/5/16.
 */
class ProtectionListener(private val protect: ProtectionHandler) : Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        protect.lock(event.player.uniqueId)
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerDisconnect(event: PlayerQuitEvent) {
        protect.unlock(event.player.uniqueId)
    }

    //Starting Player Lock

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerAchieve(event: PlayerAchievementAwardedEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerAnimate(event: PlayerAnimationEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerEnterBed(event: PlayerBedEnterEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerFillBucket(event: PlayerBucketFillEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerEmptyBucket(event: PlayerBucketEmptyEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerEditBook(event: PlayerEditBookEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerFish(event: PlayerFishEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerChangeGameMode(event: PlayerGameModeChangeEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerInventory(event: InventoryInteractEvent) {
        if (protect.isLocked(event.whoClicked.uniqueId)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerInventoryOpen(event: InventoryOpenEvent) {
        if (protect.isLocked(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerConsumeItem(event: PlayerItemConsumeEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerHoldItem(event: PlayerItemHeldEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerMove(event: PlayerMoveEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerShearEntity(event: PlayerShearEntityEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerStatIncrement(event: PlayerStatisticIncrementEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerToggleSprint(event: PlayerToggleSprintEvent) {
        handlePlayerEvent(event)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerUnleashEntity(event: PlayerUnleashEntityEvent) {
        if (protect.isLocked(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerVelocity(event: PlayerVelocityEvent) {
        handlePlayerEvent(event)
    }

    private inline fun <T> handlePlayerEvent(event: T) where T : PlayerEvent, T : Cancellable {
        if (protect.isLocked(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

}