package dev.dreamhopping.astatine.mixins;

import com.google.gson.Gson;
import dev.dreamhopping.astatine.json.ScreenConfigJson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.InputStreamReader;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    private final Gson gson = new Gson();
    private ScreenConfigJson screenConfig = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            // Read the gui config file from the resource pack
            Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier("astatine", "config/screen.json"));
            screenConfig = gson.fromJson(new InputStreamReader(resource.getInputStream()), ScreenConfigJson.class);

            System.out.println(screenConfig.textHex);
        } catch (Throwable ignored) {
        }
    }

    @ModifyVariable(method = "drawInternal(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZIIZ)I", ordinal = 0, at = @At("HEAD"), argsOnly = true)
    public int replaceColor(int i) {
        return screenConfig != null ? Color.decode(screenConfig.textHex).getRGB() : i;
    }
}
