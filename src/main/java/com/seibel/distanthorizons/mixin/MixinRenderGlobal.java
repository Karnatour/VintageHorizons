package com.seibel.distanthorizons.mixin;

import com.seibel.distanthorizons.RenderHelper;
import com.seibel.distanthorizons.core.config.Config;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderGlobal.class, priority = 1100) // Apply after Celeritas
public class MixinRenderGlobal
{
	@Inject(
			method = "renderBlockLayer(Lnet/minecraft/util/BlockRenderLayer;DILnet/minecraft/entity/Entity;)I",
			at = @At("HEAD"),
			cancellable = true)
	public void onRenderBlockLayer(BlockRenderLayer blockLayerIn, double partialTicks, int pass, Entity entityIn, CallbackInfoReturnable<Integer> cir)
	{
		if (Config.Client.Advanced.Debugging.lodOnlyMode.get() && blockLayerIn != BlockRenderLayer.CUTOUT){
			cir.cancel();
		}
		if (blockLayerIn == BlockRenderLayer.SOLID)
		{
			RenderHelper.drawLods();
		}
	}
	
}
