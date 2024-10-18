package codes.anb.mcsurveillance;

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

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class EventsListener implements Listener {

  public static String toStr(Component c) {
    return PlainTextComponentSerializer.plainText().serialize(c);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Logger.get().debug("Join " + toStr(event.joinMessage()));
    event.getPlayer().sendMessage("§fWelcome to the The Last World! Type /rules if you're wondering what's allowed");
    
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.JOIN,
      event.getPlayer().getX(),
      event.getPlayer().getY(),
      event.getPlayer().getZ(),
      null,
      null
    );
    
    LogWriter.get().write(event.getPlayer().getDisplayName(), "joined", "");
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Logger.get().debug("Leave " + event.getQuitMessage());
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.LEAVE,
      event.getPlayer().getX(),
      event.getPlayer().getY(),
      event.getPlayer().getZ(),
      null,
      null
    );
    LogWriter.get().write(event.getPlayer().getDisplayName(), "left", "");
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.DEATH,
      event.getPlayer().getX(),
      event.getPlayer().getY(),
      event.getPlayer().getZ(),
      null,
      toStr(event.deathMessage())
    );

    LogWriter.get().write(event.getEntity().getDisplayName(), "died", event.getDeathMessage());
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.CHAT,
      event.getPlayer().getX(),
      event.getPlayer().getY(),
      event.getPlayer().getZ(),
      null,
      toStr(event.message())
    );
    LogWriter.get().write(event.getPlayer().getDisplayName(), "wrote", PlainTextComponentSerializer.plainText().serialize(event.message()));
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.MOVE,
      event.getTo().getX(),
      event.getTo().getY(),
      event.getTo().getZ(),
      null,
      null
    );
    LogWriter.get().write(event.getPlayer().getDisplayName(), "moved",
        event.getTo().getX() + " " + event.getTo().getY() + " " + event.getTo().getZ());
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    DB.insert(
      toStr(event.getEntity().name()),
      DBEvent.DEATH,
      event.getEntity().getX(),
      event.getEntity().getY(),
      event.getEntity().getZ(),
      null,
      null
    );
    LogWriter.get().write(event.getEntity().getName(), "died", "");
  }

  @EventHandler
  public void onBlockPlaced(BlockPlaceEvent event) {
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.PLACE,
      event.getBlock().getX(),
      event.getBlock().getY(),
      event.getBlock().getZ(),
      event.getBlock().getBlockData().getAsString(),
      null
    );
    LogWriter.get().write(event.getPlayer().getName(), "placed",
        event.getBlock().getBlockData().getAsString() + ", " + event.getBlock().getLocation().getBlockX() + ", "
            + event.getBlock().getLocation().getBlockY() + ", " + event.getBlock().getLocation().getBlockZ());
  }

  @EventHandler
  public void onBlockDestroyed(BlockBreakEvent event) {
    DB.insert(
      toStr(event.getPlayer().displayName()),
      DBEvent.DESTROY,
      event.getBlock().getX(),
      event.getBlock().getY(),
      event.getBlock().getZ(),
      event.getBlock().getBlockData().getAsString(),
      null
    );

    LogWriter.get().write(event.getPlayer().getName(), "broke",
        event.getBlock().getBlockData().getAsString() + ", " + event.getBlock().getLocation().getBlockX() + ", "
            + event.getBlock().getLocation().getBlockY() + ", " + event.getBlock().getLocation().getBlockZ());
  }

  @EventHandler
  public void onBlockBurned(BlockBurnEvent event) {
    DB.insert(
      event.getBlock().getBlockData().getAsString(),
      DBEvent.BURNED,
      event.getBlock().getX(),
      event.getBlock().getY(),
      event.getBlock().getZ(),
      null,
      null
    );


    LogWriter.get().write(event.getBlock().getBlockData().getAsString(), "burned", "");
  }

  // TODO: Log:

  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event) {
    DB.insert(
      toStr(event.getEntity().name()),
      DBEvent.PICKUP,
      event.getEntity().getX(),
      event.getEntity().getY(),
      event.getEntity().getZ(),
      event.getItem().getName(),
      null
    );
    LogWriter.get().write(event.getEntity().getName(), "picked-up", event.getItem().getName());
  }

  @EventHandler
  public void onEntityDropItem(EntityDropItemEvent event) {
    DB.insert(
      toStr(event.getEntity().name()),
      DBEvent.DROP,
      event.getEntity().getX(),
      event.getEntity().getY(),
      event.getEntity().getZ(),
      event.getItemDrop().getName(),
      null
    );
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
    DB.insert(
      toStr(event.getPlayer().name()),
      DBEvent.OPEN_INV,
      x,
      y,
      z,
      event.getInventory().getType().toString(),
      items
    );
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
    DB.insert(
      toStr(event.getPlayer().name()),
      DBEvent.CLOSE_INV,
      x,
      y,
      z,
      event.getInventory().getType().toString(),
      items
    );

    LogWriter.get().write(event.getPlayer().getName(), "closed", x + ", " + y + ", " + z + items);
  }

  @EventHandler
  public void onProjectileHit(ProjectileHitEvent event) {
    if (event.getHitEntity() != null) {
      DB.insert(
        toStr(event.getEntity().name()),
        DBEvent.SHOT,
        event.getHitEntity().getX(),
        event.getHitEntity().getY(),
        event.getHitEntity().getZ(),
        toStr(event.getHitEntity().name()),
        null
      );
      LogWriter.get().write(event.getEntity().getName(), "shot", event.getHitEntity().getName());
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event) {
    DB.insert(
      toStr(event.getEntity().name()),
      DBEvent.HURT_BY,
      event.getEntity().getX(),
      event.getEntity().getY(),
      event.getEntity().getZ(),
      event.getCause().toString(),
      Double.toString(event.getFinalDamage())
    );

    LogWriter.get().write(event.getEntity().getName(), "was hurt by",
        event.getCause().toString() + ", " + event.getFinalDamage());
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    DB.insert(
      toStr(event.getDamager().name()),
      DBEvent.HURT,
      event.getEntity().getX(),
      event.getEntity().getY(),
      event.getEntity().getZ(),
      toStr(event.getEntity().name()),
      Double.toString(event.getFinalDamage())
    );

    LogWriter.get().write(event.getDamager().getName(), "hurt",
        event.getEntity().getName() + ", " + event.getCause().toString() + ", " + event.getFinalDamage());
  }

  @EventHandler
  public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
    DB.insert(
      toStr(event.getCombuster().name()),
      DBEvent.EXPLODED,
      event.getEntity().getX(),
      event.getEntity().getY(),
      event.getEntity().getZ(),
      toStr(event.getEntity().name()),
      null
    );

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

      DB.insert(
        toStr(event.getPlayer().name()),
        DBEvent.INTERACT,
        x,
        y,
        z,
        event.getItem().toString(),
        type
      );

      LogWriter.get().write(event.getPlayer().getName(), "used",
          event.getItem().toString() + ", " + x + ", " + y + ", " + z + ", " + type);
    } else {
      DB.insert(
        toStr(event.getPlayer().name()),
        DBEvent.INTERACT,
        event.getPlayer().getX(),
        event.getPlayer().getY(),
        event.getPlayer().getZ(),
        event.getItem().toString(),
        null
      );
      LogWriter.get().write(event.getPlayer().getName(), "used", event.getItem().toString());
    }

  }
}
