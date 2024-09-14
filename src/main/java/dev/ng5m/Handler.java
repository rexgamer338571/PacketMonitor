package dev.ng5m;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class Handler {
    public static void onPacket(Object realPacket) {
        if (!PacketMonitor.MONITORS.isEmpty()) {
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

            PacketMonitor.PACKETS.add(new PacketWrapper(realPacket, map, zuper));
            PacketMonitor.notifyMonitors();
        }
    }

    private static void lalala(HashMap<String, Object> map, Class<?> packetClass, Object object) {
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
}
