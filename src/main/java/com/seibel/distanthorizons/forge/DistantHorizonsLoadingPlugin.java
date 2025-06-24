package com.seibel.distanthorizons.forge;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class DistantHorizonsLoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public @Nullable String[] getASMTransformerClass()
	{
		return null;
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
	public void injectData(Map<String, Object> data)
	{
		//MixinBootstrap.init();
		//Mixins.addConfiguration("distanthorizons.default.mixin.json");
	}
	@Override
	public @Nullable String getAccessTransformerClass()
	{
		return null;
	}
	
}
