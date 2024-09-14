package dev.ng5m.mixin;

import dev.ng5m.PacketMonitor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen extends Screen {

    @Unique
    private final Text l = Text.literal("Packet Monitor");

    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void i(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(l, (button) -> {
            PacketMonitor.openNew();
        }).dimensions(5, 5, 10 + textRenderer.getWidth(l), 10 + textRenderer.fontHeight).build());
    }

}
