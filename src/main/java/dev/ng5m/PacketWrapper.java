package dev.ng5m;

import net.minecraft.network.protocol.Packet;

import java.util.HashMap;

public record PacketWrapper(Object packet, String player, HashMap<String, ?> data, HashMap<String, ?> superData) {

}
