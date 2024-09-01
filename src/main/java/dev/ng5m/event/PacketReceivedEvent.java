package dev.ng5m.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PacketReceivedEvent extends Event {
    final Player player;
    final Object packet;

    private static final HandlerList HANDLERS = new HandlerList();

    public PacketReceivedEvent(Player player, Object packet) {
        this.player = player;
        this.packet = packet;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }
    public Object getPacket() {
        return this.packet;
    }

}
