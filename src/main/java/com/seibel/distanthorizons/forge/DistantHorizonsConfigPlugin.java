package com.seibel.distanthorizons.forge;

import net.minecraftforge.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

public class DistantHorizonsConfigPlugin implements IMixinConfigPlugin
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void onLoad(String mixinPackage)
	{ }
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return switch (mixinClassName.split("\\.")[5]) {
			case "mist" -> Loader.isModLoaded("mist");
			default -> true;	
		};
	}
	
	@Override public String getRefMapperConfig() { return null; }
	@Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
	@Override public List<String> getMixins() { return null; }
	@Override public void preApply(String targetClassName, ClassNode classNode, String mixinClassName, IMixinInfo mixinInfo) { }
	@Override public void postApply(String targetClassName, ClassNode classNode, String mixinClassName, IMixinInfo mixinInfo) { }
	
}
