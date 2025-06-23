package com.seibel.distanthorizons.common.wrappers.block;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * For wrapping/utilizing around TextureAtlasSprite
 *
 * @author Ran
 */
public class TextureAtlasSpriteWrapper {
	
	public static int getPixelRGBA(TextureAtlasSprite sprite, int frameIndex, int x, int y) {
		if (sprite == null || sprite.getFrameCount() <= frameIndex) {
			return 0xFFFF00FF;
		}
		
		// Get the frame data
		int[][] frameData = sprite.getFrameTextureData(frameIndex);
		if (frameData == null || frameData.length == 0) {
			return 0xFFFF00FF;
		}
		
		int[] pixelData = frameData[0];
		int width = sprite.getIconWidth();
		int height = sprite.getIconHeight();
		
		// Bounds check
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return 0xFFFF00FF;
		}
		
		return pixelData[y * width + x];
	}
}
