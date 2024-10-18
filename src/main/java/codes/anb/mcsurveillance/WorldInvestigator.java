package codes.anb.mcsurveillance;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldInvestigator extends JavaPlugin implements CommandExecutor {
    private static WorldInvestigator instance;

    static public WorldInvestigator get() {
        return WorldInvestigator.instance;
    }

    @Override
    public void onDisable() {
        // Don't log disabling, Spigot does that for you automatically!
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmdName = command.getName().toLowerCase();
        Player player = (Player) sender;
        if (cmdName.equals("rules")) {
            player.sendMessage("§c§lRules:");
            player.sendMessage("§f    * Respect others (Includes no griefing, stealing, swearing, etc...)");
            player.sendMessage(
                    "§f    * The only allowed mods are Replay Mod and Optifine (No using the Replay Mod to cheat)");
            player.sendMessage(
                    "§f    * The server has a plugin installed to monitor everything anyone does, so please don't try greifing");
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        WorldInvestigator.instance = this;
        getDataFolder().mkdir();
        if (!DB.init()) {
            Bukkit.getLogger().warning("Error loading database");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        LogWriter.get().init();
        // TODO: Make this async
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                LogWriter.get().commit();
            }
        }, 20, 200);

        getCommand("rules").setExecutor(this);

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
    }
}
