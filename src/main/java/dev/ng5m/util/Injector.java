package dev.ng5m.util;

import dev.ng5m.PluginMain;
import dev.ng5m.event.PacketReceivedEvent;
import dev.ng5m.nmswrapper.BukkitPlayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public record Injector(Player player) {

    public void inject() {
        var BukkitPlayer = new BukkitPlayer(player);
        var CraftPlayer = BukkitPlayer.bukkitToCraftPlayer();
        var ServerPlayer = CraftPlayer.getHandle();
        var connection = ServerPlayer.connection();
        var channel = connection.connection().channel();

        if (channel.pipeline().get(PluginMain.PIPELINE) != null)
            return;

        channel.pipeline().addAfter("decoder", PluginMain.PIPELINE, new MessageToMessageDecoder<>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Object packet, List<Object> list) {
                list.add(packet);

                if (!PluginMain.getInstance().isEnabled()) return;

                Bukkit.getScheduler().runTask(PluginMain.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new PacketReceivedEvent(Injector.this.player, packet)));
            }
        });

    }

}
