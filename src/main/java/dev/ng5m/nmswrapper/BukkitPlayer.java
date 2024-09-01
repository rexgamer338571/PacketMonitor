package dev.ng5m.nmswrapper;

import dev.ng5m.PluginMain;
import org.bukkit.entity.Player;

public class BukkitPlayer {

    final Player bukkit;

    public BukkitPlayer(Player player) {
        this.bukkit = player;
    }

    /**
     * @return cast of the {@link bukkit} to an Object of CraftPlayer
     */
    public CraftPlayer bukkitToCraftPlayer() {
        return new CraftPlayer(classCraftPlayer().cast(bukkit));
    }

    public static Class<?> classCraftPlayer() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + PluginMain.VERSION + ".entity.CraftPlayer"); // always the same, craftbukkit
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

}
