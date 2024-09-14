package dev.ng5m;

import java.util.HashMap;

public record PacketWrapper(Object packet, HashMap<String, ?> data, HashMap<String, ?> superData) {

}
