package com.seibel.distanthorizons.forge;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DistantHorizonsLoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public @Nullable String[] getASMTransformerClass()
	{
		return new String[0];
	}
	@Override
	public @Nullable String getModContainerClass()
	{
		return null;
	}
	@Override
	public @Nullable String getSetupClass()
	{
		return null;
	}
	@Override
	public void injectData(Map<String, Object> data) { }
	@Override
	public @Nullable String getAccessTransformerClass()
	{
		return null;
	}
	
}
