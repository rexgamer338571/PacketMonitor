package dev.ng5m.nmswrapper;

import io.netty.channel.Channel;

import java.lang.reflect.Field;

import static dev.ng5m.PluginMain.VERSION_MAJOR;
import static dev.ng5m.nmswrapper.ServerGamePacketListenerImpl.m;

public class Connection {

    final Object connectionObject;

    public Connection(Object connectionObject) {
        if (!ServerGamePacketListenerImpl.classConnection().isInstance(connectionObject)) {
            throw new IllegalArgumentException("The provided object is not a valid Connection");
        }

        this.connectionObject = connectionObject;
    }

    public Channel channel() {
        try {
            return (Channel) connectionObject.getClass().getDeclaredField(fieldChannel()).get(connectionObject);
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    /*

    NetworkManager k <= 1.14.3
    NetworkManager j 1.14.4 - 1.16.5
    NetworkManager k 1.17 - 1.18.1
    NetworkManager m 1.18.2 - 1.20.1
    NetworkManager n 1.20.2 - 1.21.1

     */

    public String fieldChannel() {
        String a;

        if (VERSION_MAJOR == 21) {
            a = "n";
        } else if (VERSION_MAJOR == 20) {
            if (m() >= 2)
                a = "n";
            else
                a = "m";
        } else if (VERSION_MAJOR == 19) {
            a = "m";
        } else if (VERSION_MAJOR == 18) {
            if (m() == 2)
                a = "m";
            else
                a = "k";
        } else if (VERSION_MAJOR == 17) {
            a = "k";
        } else if (VERSION_MAJOR == 16 || VERSION_MAJOR == 15) {
            a = "j";
        } else if (VERSION_MAJOR == 14) {
            try {
                Field f = connectionObject.getClass().getDeclaredField("j"); // checking if "j" (1.14.4) throws a NoSuchFieldException
                a = "j";
            } catch (NoSuchFieldException x) {
                a = "k";
            }
        } else {
            a = "k";
        }

        return a; // phew
    }

}
