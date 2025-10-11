package com.seibel.distanthorizons.common.wrappers.level;

import com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import com.seibel.distanthorizons.core.dataObjects.fullData.sources.FullDataSourceV2;
import com.seibel.distanthorizons.core.level.IServerKeyedClientLevel;
import com.seibel.distanthorizons.core.pos.blockPos.DhBlockPos;
import com.seibel.distanthorizons.core.wrapperInterfaces.block.IBlockStateWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IBiomeWrapper;
import net.minecraft.client.multiplayer.WorldClient;

public class ServerKeyedClientLevel extends ClientLevelWrapper implements IServerKeyedClientLevel
{
	/** A unique identifier (generally the level's name) for differentiating multiverse levels */
	private final String serverLevelKey;



	public ServerKeyedClientLevel(WorldClient level, String serverLevelKey)
	{
		super(level);
		this.serverLevelKey = serverLevelKey;
	}



	@Override
	public String getServerLevelKey() { return this.serverLevelKey; }

	@Override
	public String getDhIdentifier() { return this.getServerLevelKey(); }
	
	@Override
	public int getBlockColor(DhBlockPos pos, IBiomeWrapper biome, FullDataSourceV2 fullDataSource, IBlockStateWrapper blockState)
	{
		return 0;
	}
	@Override
	public int getWaterBlockColor()
	{
		return 0;
	}
	
}
