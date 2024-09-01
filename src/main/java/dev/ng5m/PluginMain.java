package dev.ng5m;

import dev.ng5m.event.PacketReceivedHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PluginMain extends JavaPlugin {
    private static PluginMain INSTANCE;
    public static List<Monitor> MONITORS = new ArrayList<>();
    public static List<PacketWrapper> PACKETS = new ArrayList<>();
    public static final String PIPELINE = randomString();
    public static final String VERSION = Bukkit.getServer().getClass().getPackageName().split("\\.")[3];
    public static final int VERSION_MAJOR = Integer.parseInt(VERSION.split("_")[1]);

    @Override
    public void onEnable() {
        INSTANCE = this;

        getServer().getPluginManager().registerEvents(new PacketReceivedHandler(), this);

        getCommand("monitor").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage("§c[PacketMonitor] This command can only be ran as console.");
                return true;
            }

            if (Boolean.parseBoolean(System.getProperty("java.awt.headless"))) {
                sender.sendMessage("§c[PacketMonitor] The Packet Monitor requires a non-headless JDK.");
                return true;
            }

            MONITORS.add(new Monitor());

            return true;
        });
    }

    private static String randomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((char) new Random().nextInt(33, 127));
        }
        return sb.toString();
    }

    public static void notifyMonitors() {
        for (Monitor monitor : MONITORS)
            monitor.notifyMonitor();
    }

    public static PluginMain getInstance() {
        return INSTANCE;
    }

}
