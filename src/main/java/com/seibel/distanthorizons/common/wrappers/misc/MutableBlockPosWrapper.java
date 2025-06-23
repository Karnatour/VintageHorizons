package com.seibel.distanthorizons.common.wrappers.misc;

import com.seibel.distanthorizons.core.wrapperInterfaces.misc.IMutableBlockPosWrapper;
import net.minecraft.util.math.BlockPos;

public class MutableBlockPosWrapper implements IMutableBlockPosWrapper
{
	public final BlockPos pos;
	
	
	
	//=============//
	// constructor //
	//=============//
	
	public MutableBlockPosWrapper()
	{
		this.pos = new BlockPos.MutableBlockPos();
	}
	
	
	
	//===========//
	// overrides //
	//===========//
	
	@Override
	public Object getWrappedMcObject() { return this.pos; }
	
}
