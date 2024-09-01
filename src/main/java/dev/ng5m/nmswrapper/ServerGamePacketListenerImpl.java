package dev.ng5m.nmswrapper;

import org.bukkit.Bukkit;

import static dev.ng5m.PluginMain.VERSION_MAJOR;
import static dev.ng5m.PluginMain.VERSION;

public class ServerGamePacketListenerImpl {

    final Object serverGamePacketListenerImplObject;

    public ServerGamePacketListenerImpl(Object serverGamePacketListenerImplObject) {
        if (!ServerPlayer.classServerGamePacketListenerImpl().isInstance(serverGamePacketListenerImplObject)) {
            throw new IllegalArgumentException("The provided object is not a valid ServerGamePacketListenerImpl");
        }

        this.serverGamePacketListenerImplObject = serverGamePacketListenerImplObject;
    }

    /**
     * @return the second Connection wrapper
     */
    public Connection connection() {
        try {
            return new Connection(ServerPlayer.classServerGamePacketListenerImpl().getDeclaredField(fieldConnection()).get(serverGamePacketListenerImplObject));
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    /*

    PlayerConnection a <= 1.16.5

    -----------------------------------------------------------------

    PlayerConnection a 1.17, 1.17.1, 1.18, 1.18.1, 1.18.2
    PlayerConnection b 1.19, 1.19.1, 1.19.2, 1.19.3
    PlayerConnection h 1.19.4, 1.20.1
    ServerCommonPacketListenerImpl c 1.20.2, 1.20.4
    ServerCommonPacketListenerImpl e 1.20.6, 1.21, 1.21.1

     */

    /**
     * @return the second Connection field
     */
    public static String fieldConnection() { // in ServerCommonPacketListenerImpl since 1.20.2
        String a = "a";

        switch (VERSION_MAJOR) {
            case 20 -> {
                if (m() == 3 || m() == 2)
                    a = "c";
                else if (m() == 1)
                    a = "h";
            }
            case 19 -> {
                if (m() == 3)
                    a = "h";
                else if (m() <= 2)
                    a = "b";
            }
            default -> a = "a";
        }

        return a;
    }

    public static int m() {
        return Integer.parseInt(VERSION.split("_")[2].replace("R",""));
    }

    public static Class<?> classConnection() {
        try {
            if (VERSION_MAJOR > 16) {
                return Class.forName("net.minecraft.network.NetworkManager");
            } else {
                return Class.forName("net.minecraft.server." + VERSION + ".NetworkManager");
            }
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

}
