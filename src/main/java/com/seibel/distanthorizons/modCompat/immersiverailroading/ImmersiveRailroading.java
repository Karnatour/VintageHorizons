package com.seibel.distanthorizons.modCompat.immersiverailroading;

import com.seibel.distanthorizons.common.wrappers.block.ClientBlockStateColorCache;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ImmersiveRailroading
{
	public static boolean checkImmersiveRailroadingBlocks(ClientBlockStateColorCache instance, IBlockState blockState, ClientBlockStateColorCache.BlockColorInfo blockColorInfo)
	{
		if (blockState.toString().equals("immersiverailroading:block_rail") || blockState.toString().equals("immersiverailroading:block_rail_gag"))
		{
			blockColorInfo.needPostTinting = false;
			blockColorInfo.needShade = false;
			blockColorInfo.tintIndex = 0;
			IBlockState plankState = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
			blockColorInfo.baseColor = instance.getParticleIconColor(plankState, ClientBlockStateColorCache.ColorMode.Default);
			return true;
		}
		return false;
	}
}
