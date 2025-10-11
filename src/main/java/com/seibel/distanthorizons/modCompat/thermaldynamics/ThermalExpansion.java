package com.seibel.distanthorizons.modCompat.thermaldynamics;

import cofh.thermaldynamics.block.BlockDuct;
import cofh.thermaldynamics.duct.TDDucts;
import com.seibel.distanthorizons.common.wrappers.block.ClientBlockStateColorCache;
import net.minecraft.block.state.IBlockState;

public class ThermalExpansion
{
	public static boolean checkThermalExpansionBlocks(ClientBlockStateColorCache instance, IBlockState blockState, ClientBlockStateColorCache.BlockColorInfo blockColorInfo)
	{
		blockColorInfo.needPostTinting = false;
		blockColorInfo.needShade = false;
		blockColorInfo.tintIndex = 0;
		
		if (blockState.getBlock() instanceof BlockDuct)
		{
			int meta = blockState.getBlock().getMetaFromState(blockState);
			int idOffset = 0;
			
			String name = blockState.getBlock().getRegistryName().toString();
			
			if (name.contains("thermaldynamics:duct_32"))
			{
				idOffset = TDDucts.OFFSET_ITEM;
				blockColorInfo.baseColor = instance.getParticleIconColor(TDDucts.getType(meta + idOffset).iconBaseTexture, ClientBlockStateColorCache.ColorMode.Default);
			}
			else if (name.contains("thermaldynamics:duct_64"))
			{
				idOffset = TDDucts.OFFSET_TRANSPORT;
				blockColorInfo.baseColor = instance.getParticleIconColor(TDDucts.getType(meta + idOffset).iconFrameTexture, ClientBlockStateColorCache.ColorMode.Default);
			}
			else if (name.contains("thermaldynamics:duct_16"))
			{
				idOffset = TDDucts.OFFSET_FLUID;
				blockColorInfo.baseColor = instance.getParticleIconColor(TDDucts.getType(meta + idOffset).iconBaseTexture, ClientBlockStateColorCache.ColorMode.Default);
			}
			else if (name.contains("thermaldynamics:duct_80"))
			{
				idOffset = TDDucts.OFFSET_ENDER;
				blockColorInfo.baseColor = instance.getParticleIconColor(TDDucts.getType(meta + idOffset).iconBaseTexture, ClientBlockStateColorCache.ColorMode.Default);
			}
			else
			{
				blockColorInfo.baseColor = instance.getParticleIconColor(TDDucts.getType(meta + idOffset).iconBaseTexture, ClientBlockStateColorCache.ColorMode.Default);
				
			}
			
			return true;
		}
		return false;
	}
	
}
