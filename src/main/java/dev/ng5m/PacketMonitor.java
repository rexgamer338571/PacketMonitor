package dev.ng5m;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PacketMonitor implements ModInitializer {
	public static final String MOD_ID = "packetmonitor";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static List<Monitor> MONITORS = new ArrayList<>();
	public static List<PacketWrapper> PACKETS = new ArrayList<>();

	final KeyBinding bind = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open Monitor", GLFW.GLFW_KEY_APOSTROPHE, "Packet Monitor"));;

	@Override
	public void onInitialize() {
		System.setProperty("java.awt.headless", "false"); // prevents some bugs (the jdk thinks it's headless)

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (bind.wasPressed()) {
				openNew();
			}
		});

	}

	public static void notifyMonitors() {
		for (Monitor monitor : MONITORS)
			monitor.notifyMonitor();
	}

	public static void openNew() {
		if (!MONITORS.isEmpty()) return;

		MONITORS.add(new Monitor());
	}
}