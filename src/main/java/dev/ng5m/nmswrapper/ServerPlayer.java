package dev.ng5m.nmswrapper;

import dev.ng5m.PluginMain;

import static dev.ng5m.PluginMain.VERSION_MAJOR;

public class ServerPlayer {

    final Object serverPlayerObject;

    public ServerPlayer(Object serverPlayerObject) {
        if (!CraftPlayer.classServerPlayer().isInstance(serverPlayerObject)) {
            throw new IllegalArgumentException("The provided object is not a valid ServerPlayer");
        }

        this.serverPlayerObject = serverPlayerObject;
    }

    /**
     * @return The first connection
     */
    public ServerGamePacketListenerImpl connection() {
        try {
            return new ServerGamePacketListenerImpl(CraftPlayer.classServerPlayer().getDeclaredField(fieldConnection()).get(serverPlayerObject));
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * This is scuffed
     * @return mapped ServerGamePacketListenerImpl class
     */
    public static Class<?> classServerGamePacketListenerImpl() {
        try {
            if (VERSION_MAJOR < 17) {
                return Class.forName("net.minecraft.server." + PluginMain.VERSION + ".PlayerConnection");
            } else if (VERSION_MAJOR < 20) {
                return Class.forName("net.minecraft.server.network.PlayerConnection");
            } else if (VERSION_MAJOR == 20){
                if (ServerGamePacketListenerImpl.m() != 1) {
                    return Class.forName("net.minecraft.server.network.ServerCommonPacketListenerImpl");
                } else {
                    return Class.forName("net.minecraft.server.network.PlayerConnection");
                }
            } else {
                return Class.forName("net.minecraft.server.network.ServerCommonPacketListenerImpl");
            }
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    /*

    EntityPlayer a <= 1.13.2
    EntityPlayer b 1.14 - 1.19.4
    EntityPlayer c 1.20.1 - 1.21.1

     */

    /**
     * @return first connection field name
     */
    public static String fieldConnection() {
        String a;

        if (VERSION_MAJOR >= 20) {
            a = "c";
        } else if (VERSION_MAJOR > 13) {
            a = "b";
        } else {
            a = "a";
        }

        return a;
    }

}
