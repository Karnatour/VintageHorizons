package com.seibel.distanthorizons.mixin;

import com.seibel.distanthorizons.common.wrappers.misc.IMixinServerPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerMP.class)
public class MixinEntityPlayerMP implements IMixinServerPlayer
{
	@Unique
	private volatile int distantHorizons$dimensionChangeDestination;
	
	@Override
	public int distantHorizons$getDimensionChangeDestination()
	{
		return this.distantHorizons$dimensionChangeDestination;
	}
	
	@Override
	public void distantHorizons$setDimensionChangeDestination(int dimensionChangeDestination)
	{
		this.distantHorizons$dimensionChangeDestination = dimensionChangeDestination;
	}
	
	@Inject(at = @At("HEAD"), method = "changeDimension", remap = false)
	public void changeDimension(int dimensionIn, ITeleporter teleporter, CallbackInfoReturnable<Entity> cir)
	{
		this.distantHorizons$dimensionChangeDestination = dimensionIn;
	}
	
}