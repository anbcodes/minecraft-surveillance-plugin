package codes.anb.mchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class EventsListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Logger.get().debug("Join " + event.getJoinMessage());
    event.getPlayer().sendMessage("§f Welcome to the Friends of Friends SMP! Type /rules to get started");
    LogWriter.get().write(event.getPlayer().getDisplayName(), "joined", "");
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Logger.get().debug("Leave " + event.getQuitMessage());
    LogWriter.get().write(event.getPlayer().getDisplayName(), "left", "");
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    LogWriter.get().write(event.getEntity().getDisplayName(), "died", event.getDeathMessage());
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    LogWriter.get().write(event.getPlayer().getDisplayName(), "wrote", event.getMessage());
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    LogWriter.get().write(event.getPlayer().getDisplayName(), "moved",
        event.getTo().getX() + " " + event.getTo().getY() + " " + event.getTo().getZ());
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    LogWriter.get().write(event.getEntity().getName(), "died", "");
  }

  @EventHandler
  public void onBlockPlaced(BlockPlaceEvent event) {
    LogWriter.get().write(event.getPlayer().getName(), "placed",
        event.getBlock().getBlockData().getAsString() + ", " + event.getBlock().getLocation().getBlockX() + ", "
            + event.getBlock().getLocation().getBlockY() + ", " + event.getBlock().getLocation().getBlockZ());
  }

  @EventHandler
  public void onBlockDestroyed(BlockBreakEvent event) {
    LogWriter.get().write(event.getPlayer().getName(), "broke",
        event.getBlock().getBlockData().getAsString() + ", " + event.getBlock().getLocation().getBlockX() + ", "
            + event.getBlock().getLocation().getBlockY() + ", " + event.getBlock().getLocation().getBlockZ());
  }

  @EventHandler
  public void onBlockBurned(BlockBurnEvent event) {
    LogWriter.get().write(event.getBlock().getBlockData().getAsString(), "burned", "");
  }

  // TODO: Log:

  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event) {
    LogWriter.get().write(event.getEntity().getName(), "picked-up", event.getItem().getName());
  }

  @EventHandler
  public void onEntityDropItem(EntityDropItemEvent event) {
    LogWriter.get().write(event.getEntity().getName(), "droped", event.getItemDrop().getName());
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    ItemStack[] inv = event.getInventory().getStorageContents();
    String items = "";
    for (ItemStack item : inv) {
      if (item == null) {
        items += ", null";
      } else {
        items += ", " + item.toString();
      }
    }
    int x = event.getInventory().getLocation().getBlockX();
    int y = event.getInventory().getLocation().getBlockY();
    int z = event.getInventory().getLocation().getBlockZ();
    LogWriter.get().write(event.getPlayer().getName(), "opened", x + ", " + y + ", " + z + items);
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    ItemStack[] inv = event.getInventory().getStorageContents();
    String items = "";
    for (ItemStack item : inv) {
      if (item == null) {
        items += ", null";
      } else {
        items += ", " + item.toString();
      }
    }
    int x = event.getInventory().getLocation().getBlockX();
    int y = event.getInventory().getLocation().getBlockY();
    int z = event.getInventory().getLocation().getBlockZ();
    LogWriter.get().write(event.getPlayer().getName(), "closed", x + ", " + y + ", " + z + items);
  }

  @EventHandler
  public void onProjectileHit(ProjectileHitEvent event) {
    if (event.getHitEntity() != null) {
      LogWriter.get().write(event.getEntity().getName(), "shot", event.getHitEntity().getName());
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    LogWriter.get().write(event.getEntity().getName(), "was hurt by",
        event.getCause().toString() + ", " + event.getFinalDamage());
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    LogWriter.get().write(event.getDamager().getName(), "hurt",
        event.getEntity().getName() + ", " + event.getCause().toString() + ", " + event.getFinalDamage());
  }

  @EventHandler
  public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
    LogWriter.get().write(event.getCombuster().getName(), "exploded", event.getEntity().getName());
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getItem() == null) {
      return;
    }

    if (event.getClickedBlock() != null) {
      int x = event.getClickedBlock().getLocation().getBlockX();
      int y = event.getClickedBlock().getLocation().getBlockY();
      int z = event.getClickedBlock().getLocation().getBlockZ();
      String type = event.getClickedBlock().getType().toString();
      LogWriter.get().write(event.getPlayer().getName(), "used",
          event.getItem().toString() + ", " + x + ", " + y + ", " + z + ", " + type);
    } else {
      LogWriter.get().write(event.getPlayer().getName(), "used", event.getItem().toString());
    }

  }
}
