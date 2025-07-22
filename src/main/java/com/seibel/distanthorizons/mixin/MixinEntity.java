package com.seibel.distanthorizons.mixin;

import com.seibel.distanthorizons.common.wrappers.misc.IMixinServerPlayer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity
{
	@Inject(at = @At("TAIL"), method = "changeDimension(I)Lnet/minecraft/entity/Entity;")
	public void setDimensionIn(int dimensionIn, CallbackInfoReturnable<Entity> cir)
	{
		if (this instanceof IMixinServerPlayer)
		{
			((IMixinServerPlayer) this).distantHorizons$setDimensionChangeDestination(dimensionIn);
		}
	}
	
}