package dev.ng5m.event;

import dev.ng5m.PacketWrapper;
import dev.ng5m.PluginMain;
import dev.ng5m.util.Injector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class PacketReceivedHandler implements Listener {

    @EventHandler
    public void onPacket(PacketReceivedEvent event) {
        Object realPacket = event.getPacket();

        if (!PluginMain.MONITORS.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            HashMap<String, Object> zuper = new HashMap<>();
            try {
                lalala(map, realPacket.getClass(), realPacket);

                if (realPacket.getClass().getSuperclass() != null) {
                    lalala(zuper, realPacket.getClass().getSuperclass(), realPacket);
                }
            } catch (Exception x) {
                throw new RuntimeException(x);
            }

            PluginMain.PACKETS.add(new PacketWrapper(realPacket, event.getPlayer().getName(), map, zuper));
            PluginMain.notifyMonitors();
        }
    }

    private void lalala(HashMap<String, Object> map, Class<?> packetClass, Object object) {
        try {
            for (Field field : packetClass.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) != 0) continue;

                field.setAccessible(true);
                Object o = field.get(object);

                if (o == null) continue;

                map.put(field.getName(), o);
            }
        } catch (Exception x) {
            throw new RuntimeException(x);
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new Injector(event.getPlayer()).inject();
    }

}
