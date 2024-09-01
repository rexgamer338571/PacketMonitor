package dev.ng5m.nmswrapper;

import dev.ng5m.PluginMain;

public class CraftPlayer {
    final Object craftPlayerObject;

    public CraftPlayer(Object craftPlayerObject) {
        if (!BukkitPlayer.classCraftPlayer().isInstance(craftPlayerObject)) {
            throw new IllegalArgumentException("The provided object is not a valid CraftPlayer");
        }

        this.craftPlayerObject = craftPlayerObject;
    }

    /**
     * @return the ServerPlayer handle of the CraftPlayer object
     */
    public ServerPlayer getHandle() {
        var getHandle = methodGetHandle();

        try {
            return new ServerPlayer(BukkitPlayer.classCraftPlayer().getDeclaredMethod(getHandle).invoke(this.craftPlayerObject));
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    public static Class<?> classServerPlayer() {
        try {
            if (PluginMain.VERSION_MAJOR < 17) {
                return Class.forName("net.minecraft.server." + PluginMain.VERSION + ".EntityPlayer");
            } else {
                return Class.forName("net.minecraft.server.level.EntityPlayer");
            }
        } catch (Exception x) {
            throw new RuntimeException(x);
        }

    }

    public static String methodGetHandle() {
        return "getHandle";
    }

}
