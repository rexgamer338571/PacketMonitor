package dev.ng5m.mixin;

import dev.ng5m.Handler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageToMessageDecoder.class)
public class MixinMessageToMessageDecoder {
	@Inject(at = @At("HEAD"), method = "channelRead", remap = false)
	private void a(ChannelHandlerContext chc, Object msg, CallbackInfo ci) {
		Handler.onPacket(msg);
	}
}