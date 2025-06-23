package com.seibel.distanthorizons.mixin;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TextureAtlasSprite.class)
public class MixinTextureAtlasSprite {
	
	@Shadow
	protected List<int[][]> framesTextureData;
	
	private int[] spriteData;
	
	@Inject(method = "updateAnimation", at = @At("RETURN"))
	private void injectUpdateAnimation(CallbackInfo ci) {
		if (framesTextureData != null && !framesTextureData.isEmpty()) {
			int[][] frameSet = framesTextureData.get(0);
			if (frameSet != null && frameSet.length > 0) {
				int[] frame = frameSet[0];
				spriteData = frame.clone();
				
				// Reorder ARGB to RGBA
				for (int i = 0; i < spriteData.length; i++) {
					int pixel = spriteData[i];
					int a = (pixel >> 24) & 0xFF;
					int r = (pixel >> 16) & 0xFF;
					int g = (pixel >> 8) & 0xFF;
					int b = pixel & 0xFF;
					spriteData[i] = (r << 24) | (g << 16) | (b << 8) | a;
				}
			}
		}
	}
}
