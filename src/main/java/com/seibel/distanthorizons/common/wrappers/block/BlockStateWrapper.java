/*
 *    This file is part of the Distant Horizons mod
 *    licensed under the GNU LGPL v3 License.
 *
 *    Copyright (C) 2020 James Seibel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, version 3.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.seibel.distanthorizons.common.wrappers.block;

import com.seibel.distanthorizons.api.enums.rendering.EDhApiBlockMaterial;
import com.seibel.distanthorizons.core.config.Config;
import com.seibel.distanthorizons.core.config.types.ConfigEntry;
import com.seibel.distanthorizons.core.logging.DhLoggerBuilder;
import com.seibel.distanthorizons.core.util.ColorUtil;
import com.seibel.distanthorizons.core.util.LodUtil;
import com.seibel.distanthorizons.core.wrapperInterfaces.block.IBlockStateWrapper;

import com.seibel.distanthorizons.core.wrapperInterfaces.world.ILevelWrapper;
import com.seibel.distanthorizons.forge.ForgeMain;
import com.seibel.distanthorizons.modCompat.botania.Botania;
import com.seibel.distanthorizons.modCompat.furenikusroads.FurenikusRoads;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

public class BlockStateWrapper implements IBlockStateWrapper
{
	/** example "minecraft:water" */
	public static final String RESOURCE_LOCATION_SEPARATOR = ":";
	/** example "minecraft:water_STATE_{level:0}" */
	public static final String STATE_STRING_SEPARATOR = "_STATE_";
	
	
	// must be defined before AIR, otherwise a null pointer will be thrown
	private static final Logger LOGGER = DhLoggerBuilder.getLogger();
	
	public static final ConcurrentHashMap<IBlockState, BlockStateWrapper> WRAPPER_BY_BLOCK_STATE = new ConcurrentHashMap<>();
	public static final ConcurrentHashMap<String, BlockStateWrapper> WRAPPER_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();
	
	public static final String AIR_STRING = "AIR";
	public static final BlockStateWrapper AIR = new BlockStateWrapper(null, null);
	
	public static final String DIRT_RESOURCE_LOCATION_STRING = "minecraft:dirt";
	
	public static HashSet<IBlockStateWrapper> rendererIgnoredBlocks = null;
	public static HashSet<IBlockStateWrapper> rendererIgnoredCaveBlocks = null;
	
	public static HashSet<String> blockResourceLocationsColorBelow = null;
	
	/** keep track of broken blocks so we don't log every time */
	private static final HashSet<String> BROKEN_RESOURCE_LOCATIONS = new HashSet<>();
	
	
	
	// properties //
	
	@Nullable
	public final IBlockState blockState;
	/** technically final, but since it requires a method call to generate it can't be marked as such */
	private String serialString;
	private final int hashCode;
	/**
	 * Cached opacity value, -1 if not populated. <br>
	 * Should be between {@link LodUtil#BLOCK_FULLY_OPAQUE} and {@link LodUtil#BLOCK_FULLY_OPAQUE}
	 */
	private int opacity = -1;
	/** used by the Iris shader mod to determine how each LOD should be rendered */
	private byte blockMaterialId = 0;
	
	private final boolean isBeaconBlock;
	private final boolean isBeaconBaseBlock;
	private final boolean allowsBeaconBeamPassage;
	/** null if this block can't tint beacons */
	private final Color beaconTintColor;
	private final Color mapColor;
	
	
	
	//==============//
	// constructors //
	//==============//
	
	public static BlockStateWrapper fromBlockState(IBlockState blockState, ILevelWrapper levelWrapper)
	{
		if (blockState == null || blockState.getBlock() == Blocks.AIR)
		{
			return AIR;
		}
		
		if (WRAPPER_BY_BLOCK_STATE.containsKey(blockState))
		{
			return WRAPPER_BY_BLOCK_STATE.get(blockState);
		}
		else
		{
			BlockStateWrapper newWrapper = new BlockStateWrapper(blockState, levelWrapper);
			WRAPPER_BY_BLOCK_STATE.put(blockState, newWrapper);
			return newWrapper;
		}
	}
	
	/**
	 * Can be faster than {@link BlockStateWrapper#fromBlockState(IBlockState, ILevelWrapper)}
	 * in cases where the same block state is expected to be referenced multiple times.
	 */
	public static BlockStateWrapper fromBlockState(IBlockState blockState, ILevelWrapper levelWrapper, IBlockStateWrapper guess)
	{
		IBlockState guessBlockState = (guess == null || guess.isAir()) ? null : (IBlockState) guess.getWrappedMcObject();
		IBlockState inputBlockState = (blockState == null || blockState.getBlock() == Blocks.AIR) ? null : blockState;
		
		if (guess instanceof BlockStateWrapper
				&& guessBlockState == inputBlockState)
		{
			return (BlockStateWrapper) guess;
		}
		else
		{
			return fromBlockState(blockState, levelWrapper);
		}
	}
	
	private BlockStateWrapper(IBlockState blockState, ILevelWrapper levelWrapper)
	{
		this.blockState = blockState;
		this.serialString = this.serialize(levelWrapper);
		this.hashCode = Objects.hash(this.serialString);
		this.blockMaterialId = this.calculateEDhApiBlockMaterialId().index;
		
		// beacon blocks
		String lowercaseSerial = this.serialString.toLowerCase();
		boolean isBeaconBaseBlock = false;
		for (int i = 0; i < LodUtil.BEACON_BASE_BLOCK_NAME_LIST.size(); i++)
		{
			String baseBlockName = LodUtil.BEACON_BASE_BLOCK_NAME_LIST.get(i);
			if (lowercaseSerial.contains(baseBlockName))
			{
				isBeaconBaseBlock = true;
				break;
			}
		}
		this.isBeaconBaseBlock = isBeaconBaseBlock;
		this.isBeaconBlock = lowercaseSerial.contains("minecraft:beacon");
		
		// beacon tint color
		Color beaconTintColor = null;
		if (this.blockState != null
				// beacon blocks also show up here, but since they block the beacon beam we don't want their color
				&& !this.isBeaconBlock)
		{
			Block block = this.blockState.getBlock();
			if (block instanceof BlockBeacon)
			{
				int colorInt;
				colorInt = this.blockState.getMaterial().getMaterialMapColor().colorValue;
				
				beaconTintColor = ColorUtil.toColorObjRGB(colorInt);
			}
		}
		this.beaconTintColor = beaconTintColor;
		
		
		// allow/deny beacon beam passage
		boolean allowsBeaconBeamPassage;
		if (this.blockState != null)
		{
			// get block properties (defaults to the values used by air)
			boolean canOcclude = this.getCanOcclude();
			boolean propagatesSkyLightDown = this.getPropagatesSkyLightDown();
			
			if (lowercaseSerial.contains("minecraft:bedrock"))
			{
				// bedrock is a special case fully opaque block that does allow beacons through
				allowsBeaconBeamPassage = true;
			}
			else if (propagatesSkyLightDown || !canOcclude)
			{
				// stairs, cake, fences, etc.
				allowsBeaconBeamPassage = true;
			}
			else
			{
				// non-opaque blocks (glass, mob spawners, etc.)
				// all allow beacons through
				allowsBeaconBeamPassage = (this.opacity != LodUtil.BLOCK_FULLY_OPAQUE);
			}
		}
		else
		{
			// air allows beacons through
			allowsBeaconBeamPassage = true;
		}
		this.allowsBeaconBeamPassage = allowsBeaconBeamPassage;
		
		int mcColor = 0;
		if (this.blockState != null)
		{
			mcColor = this.blockState.getMaterial().getMaterialMapColor().colorValue;
			this.mapColor = ColorUtil.toColorObjRGB(mcColor);
		}
		else
		{
			this.mapColor = new Color(0, 0, 0, 0);
		}
		
		//LOGGER.trace("Created BlockStateWrapper ["+this.serialString+"] for ["+blockState+"] with material ID ["+this.EDhApiBlockMaterialId+"]");
	}
	
	
	
	//====================//
	// LodBuilder methods //
	//====================//
	
	/**
	 * Requires a {@link ILevelWrapper} since {@link BlockStateWrapper#deserialize(String, ILevelWrapper)} also requires one.
	 * This way the method won't accidentally be called before the deserialization can be completed.
	 */
	public static HashSet<IBlockStateWrapper> getRendererIgnoredBlocks(ILevelWrapper levelWrapper)
	{
		// use the cached version if possible
		if (rendererIgnoredBlocks != null)
		{
			return rendererIgnoredBlocks;
		}
		
		HashSet<String> baseIgnoredBlock = new HashSet<>();
		baseIgnoredBlock.add(AIR_STRING);
		rendererIgnoredBlocks = getBlockWrappers(Config.Client.Advanced.Graphics.Culling.ignoredRenderBlockCsv, baseIgnoredBlock, levelWrapper);
		return rendererIgnoredBlocks;
	}
	/**
	 * Requires a {@link ILevelWrapper} since {@link BlockStateWrapper#deserialize(String, ILevelWrapper)} also requires one.
	 * This way the method won't accidentally be called before the deserialization can be completed.
	 */
	public static HashSet<IBlockStateWrapper> getRendererIgnoredCaveBlocks(ILevelWrapper levelWrapper)
	{
		// use the cached version if possible
		if (rendererIgnoredCaveBlocks != null)
		{
			return rendererIgnoredCaveBlocks;
		}
		
		HashSet<String> baseIgnoredBlock = new HashSet<>();
		baseIgnoredBlock.add(AIR_STRING);
		rendererIgnoredCaveBlocks = getBlockWrappers(Config.Client.Advanced.Graphics.Culling.ignoredRenderCaveBlockCsv, baseIgnoredBlock, levelWrapper);
		return rendererIgnoredCaveBlocks;
	}
	
	public static HashSet<String> blockResourceLocationsColorBelow()
	{
		// use the cached version if possible
		if (blockResourceLocationsColorBelow != null)
		{
			return blockResourceLocationsColorBelow;
		}
		
		HashSet<String> blockResourceLocationColorBelow = new HashSet<>();
		checkForModCompat(blockResourceLocationColorBelow);
		//blockResourceLocationsColorBelow = getBlockStrings(Config.Client.Advanced.Modded.blockResourceLocationsColorBelow, blockResourceLocationColorBelow);
		return blockResourceLocationsColorBelow;
	}
	
	private static void checkForModCompat(HashSet<String> blockResourceLocationColorBelow)
	{
		if (ForgeMain.IS_FURENIKUSROADS_LOADED)
		{
			blockResourceLocationColorBelow.addAll(FurenikusRoads.BLOCKS_TINT_BELOW);
		}
		if (ForgeMain.IS_BOTANIA_LOADED)
		{
			blockResourceLocationColorBelow.addAll(Botania.BLOCKS_TINT_BELOW);
		}
	}
	
	public static void clearRendererIgnoredBlocks() { rendererIgnoredBlocks = null; }
	public static void clearRendererIgnoredCaveBlocks() { rendererIgnoredCaveBlocks = null; }
	public static void clearBlockResourceLocationsColorBelow() { blockResourceLocationsColorBelow = null; }
	
	// lod builder helpers //
	
	private static HashSet<IBlockStateWrapper> getBlockWrappers(ConfigEntry<String> config, HashSet<String> baseResourceLocations, ILevelWrapper levelWrapper)
	{
		// get the base blocks
		HashSet<String> blockStringList = new HashSet<>();
		if (baseResourceLocations != null)
		{
			blockStringList.addAll(baseResourceLocations);
		}
		
		// get the config blocks
		String ignoreBlockCsv = config.get();
		if (ignoreBlockCsv != null)
		{
			blockStringList.addAll(Arrays.asList(ignoreBlockCsv.split(",")));
		}
		
		return getBlockWrappers(blockStringList, levelWrapper);
	}
	
	private static HashSet<String> getBlockStrings(ConfigEntry<String> config, HashSet<String> baseResourceLocations)
	{
		// get the base blocks
		HashSet<String> blockStringList = new HashSet<>();
		if (baseResourceLocations != null)
		{
			blockStringList.addAll(baseResourceLocations);
		}
		
		// get the config blocks
		String blockStringConfig = config.get();
		if (blockStringConfig != null && !blockStringConfig.isEmpty())
		{
			for (String s : blockStringConfig.split(","))
			{
				String trimmed = s.trim();
				if (!trimmed.isEmpty())
				{
					blockStringList.add(trimmed);
				}
			}
		}
		
		return blockStringList;
	}
	private static HashSet<IBlockStateWrapper> getBlockWrappers(HashSet<String> blockResourceLocationSet, ILevelWrapper levelWrapper)
	{
		// deserialize each of the given resource locations
		HashSet<IBlockStateWrapper> blockStateWrappers = new HashSet<>();
		for (String blockResourceLocation : blockResourceLocationSet)
		{
			try
			{
				if (blockResourceLocation == null)
				{
					// shouldn't happen, but just in case
					continue;
				}
				String cleanedResourceLocation = blockResourceLocation.trim();
				if (cleanedResourceLocation.length() == 0)
				{
					continue;
				}
				
				
				BlockStateWrapper defaultBlockStateToIgnore = (BlockStateWrapper) deserialize(cleanedResourceLocation, levelWrapper);
				blockStateWrappers.add(defaultBlockStateToIgnore);
				
				if (defaultBlockStateToIgnore != AIR)
				{
					BlockStateWrapper newBlockToIgnore = BlockStateWrapper.fromBlockState(defaultBlockStateToIgnore.blockState, levelWrapper);
					blockStateWrappers.add(newBlockToIgnore);
				}
				else
				{
					// air is a special case so it must be handled separately
					blockStateWrappers.add(AIR);
				}
			}
			catch (IOException e)
			{
				LOGGER.warn("Unable to deserialize block with the resource location: [" + blockResourceLocation + "]. Error: " + e.getMessage(), e);
			}
			catch (Exception e)
			{
				LOGGER.warn("Unexpected error deserializing block with the resource location: [" + blockResourceLocation + "]. Error: " + e.getMessage(), e);
			}
		}
		
		return blockStateWrappers;
	}
	
	
	
	//=================//
	// wrapper methods //
	//=================//
	
	@Override
	public int getOpacity()
	{
		// use the cached opacity value if possible
		if (this.opacity != -1)
		{
			return this.opacity;
		}
		
		
		// get block properties (default to the values used by air)
		boolean canOcclude = this.getCanOcclude();
		boolean propagatesSkyLightDown = this.getPropagatesSkyLightDown();
		
		// this method isn't perfect, but works well enough for our use case
		int opacity;
		if (this.isAir())
		{
			opacity = LodUtil.BLOCK_FULLY_TRANSPARENT;
		}
		else if (this.isLiquid() && !canOcclude)
		{
			// probably not a waterlogged block (which should block light entirely)
			
			// +1 to indicate that the block is translucent (in between transparent and opaque)
			opacity = LodUtil.BLOCK_FULLY_TRANSPARENT + 1;
		}
		else if (propagatesSkyLightDown && !canOcclude)
		{
			// probably glass or some other fully transparent block
			
			// !canOcclude is required to ignore stairs and slabs since
			// propagateSkyLightDown is true for them, but they're solid and don't actually let light through
			
			opacity = LodUtil.BLOCK_FULLY_TRANSPARENT;
		}
		else
		{
			// default for all other blocks
			opacity = LodUtil.BLOCK_FULLY_OPAQUE;
		}
		
		
		this.opacity = opacity;
		return this.opacity;
	}
	
	private boolean getCanOcclude()
	{
		// defaults to the value used by air
		boolean canOcclude = false;
		if (this.blockState != null)
		{
			canOcclude = this.blockState.getMaterial().isSolid();
		}
		return canOcclude;
	}
	private boolean getPropagatesSkyLightDown()
	{
		// defaults to the value used by air
		boolean propagatesSkyLightDown = false;
		if (this.blockState != null)
		{
			propagatesSkyLightDown = this.blockState.getLightOpacity() == 0;
		}
		return propagatesSkyLightDown;
	}
	
	
	@Override
	public int getLightEmission() { return (this.blockState != null) ? this.blockState.getLightValue() : 0; }
	
	@Override
	public String getSerialString() { return this.serialString; }
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null || this.getClass() != obj.getClass())
		{
			return false;
		}
		
		BlockStateWrapper that = (BlockStateWrapper) obj;
		// the serialized value is used so we can test the contents instead of the references
		return Objects.equals(this.getSerialString(), that.getSerialString());
	}
	
	@Override
	public int hashCode() { return this.hashCode; }
	
	
	@Override
	public Object getWrappedMcObject() { return this.blockState; }
	
	@Override
	public boolean isAir() { return this.isAir(this.blockState); }
	public boolean isAir(IBlockState blockState) { return blockState == null || blockState.getBlock() == Blocks.AIR; }
	
	@Override
	public boolean isSolid()
	{
		if (this.isAir())
		{
			return false;
		}
		if (this.blockState != null)
		{
			return this.blockState.getMaterial().isSolid();
		}
		return false;
	}
	
	@Override
	public boolean isLiquid()
	{
		if (this.isAir())
		{
			return false;
		}
		
		if (this.blockState != null)
		{
			return this.blockState.getMaterial().isLiquid();
		}
		return false;
	}
	
	@Override
	public boolean isBeaconBlock() { return this.isBeaconBlock; }
	@Override
	public boolean isBeaconBaseBlock() { return this.isBeaconBaseBlock; }
	@Override
	public boolean isBeaconTintBlock() { return this.beaconTintColor != null; }
	@Override
	public boolean allowsBeaconBeamPassage()
	{
		return false;
	}
	
	@Override
	public Color getMapColor() { return this.mapColor; }
	@Override
	public Color getBeaconTintColor() { return this.beaconTintColor; }
	
	@Override
	public byte getMaterialId() { return this.blockMaterialId; }
	
	@Override
	public String toString() { return this.getSerialString(); }
	
	
	
	//=======================//
	// serialization methods //
	//=======================//
	
	private String serialize(ILevelWrapper levelWrapper)
	{
		if (this.blockState == null)
		{
			return AIR_STRING;
		}
		
		ResourceLocation resourceLocation = Block.REGISTRY.getNameForObject(this.blockState.getBlock());
		
		this.serialString = resourceLocation.getNamespace() + RESOURCE_LOCATION_SEPARATOR + resourceLocation.getPath() + STATE_STRING_SEPARATOR + serializeBlockStateProperties(this.blockState);
		
		return this.serialString;
	}
	
	
	/** will only work if a level is currently loaded */
	public static IBlockStateWrapper deserialize(String resourceStateString, ILevelWrapper levelWrapper) throws IOException
	{
		// we need the final string for the concurrent hash map later
		final String finalResourceStateString = resourceStateString;
		
		if (finalResourceStateString.equals(AIR_STRING) || finalResourceStateString.equals("")) // the empty string shouldn't normally happen, but just in case
		{
			return AIR;
		}
		
		// attempt to use the existing wrapper
		if (WRAPPER_BY_RESOURCE_LOCATION.containsKey(finalResourceStateString))
		{
			return WRAPPER_BY_RESOURCE_LOCATION.get(finalResourceStateString);
		}
		
		
		
		// if no wrapper is found, default to air
		BlockStateWrapper foundWrapper = AIR;
		try
		{
			// try to parse out the BlockState
			String blockStatePropertiesString = null; // will be null if no properties were included
			int stateSeparatorIndex = resourceStateString.indexOf(STATE_STRING_SEPARATOR);
			if (stateSeparatorIndex != -1)
			{
				// blockstate properties found
				blockStatePropertiesString = resourceStateString.substring(stateSeparatorIndex + STATE_STRING_SEPARATOR.length());
				resourceStateString = resourceStateString.substring(0, stateSeparatorIndex);
			}
			
			int separatorIndex = resourceStateString.indexOf(RESOURCE_LOCATION_SEPARATOR);
			if (separatorIndex == -1)
			{
				throw new IOException("Unable to parse Resource Location out of string: [" + resourceStateString + "].");
			}
			
			ResourceLocation resourceLocation;
			try
			{
				resourceLocation = new ResourceLocation(resourceStateString.substring(0, separatorIndex), resourceStateString.substring(separatorIndex + 1));
			}
			catch (Exception e)
			{
				throw new IOException("No Resource Location found for the string: [" + resourceStateString + "] Error: [" + e.getMessage() + "].");
			}
			
			// attempt to get the BlockState from all possible BlockStates
			try
			{
				Block block = Block.REGISTRY.getObject(resourceLocation);
				
				IBlockState foundState = null;
				if (blockStatePropertiesString != null)
				{
					List<IBlockState> possibleStateList = block.getBlockState().getValidStates();
					for (IBlockState possibleState : possibleStateList)
					{
						String possibleStatePropertiesString = serializeBlockStateProperties(possibleState);
						if (possibleStatePropertiesString.equals(blockStatePropertiesString))
						{
							foundState = possibleState;
							break;
						}
					}
				}
				
				// use the default if no state was found or given
				if (foundState == null)
				{
					if (blockStatePropertiesString != null)
					{
						// we should have found a blockstate, but didn't
						if (!BROKEN_RESOURCE_LOCATIONS.contains(resourceLocation.toString()))
						{
							BROKEN_RESOURCE_LOCATIONS.add(resourceLocation.toString());
							LOGGER.warn("Unable to find BlockState for Block [" + resourceLocation + "] with properties: [" + blockStatePropertiesString + "]. Using the default block state.");
						}
					}
					
					foundState = block.getBlockState().getBaseState();
				}
				
				foundWrapper = new BlockStateWrapper(foundState, levelWrapper);
				return foundWrapper;
			}
			catch (Exception e)
			{
				throw new IOException("Failed to deserialize the string [" + finalResourceStateString + "] into a BlockStateWrapper: " + e.getMessage(), e);
			}
		}
		finally
		{
			// put if absent in case two threads deserialize at the same time
			// unfortunately we can't put everything in a computeIfAbsent() since we also throw exceptions
			WRAPPER_BY_RESOURCE_LOCATION.putIfAbsent(finalResourceStateString, foundWrapper);
		}
	}
	
	/** used to compare and save BlockStates based on their properties */
	private static String serializeBlockStateProperties(IBlockState blockState)
	{
		// get the properties keys for this block state
		java.util.Collection<IProperty<?>> blockPropertyCollection = blockState.getPropertyKeys();
		
		// Alphabetically sort the list so they're always in the same order
		List<IProperty<?>> sortedBlockPropertyList = new ArrayList<>(blockPropertyCollection);
		sortedBlockPropertyList.sort((a, b) -> a.getName().compareTo(b.getName()));
		
		StringBuilder stringBuilder = new StringBuilder();
		
		for (IProperty<?> property : sortedBlockPropertyList)
		{
			String propertyName = property.getName();
			
			// No need to check hasProperty because the property is from getPropertyKeys()
			String value = blockState.getValue(property).toString();
			
			stringBuilder.append("{")
					.append(propertyName)
					.append(RESOURCE_LOCATION_SEPARATOR)
					.append(value)
					.append("}");
		}
		
		return stringBuilder.toString();
	}
	
	
	//==============//
	// Iris methods //
	//==============//
	
	private EDhApiBlockMaterial calculateEDhApiBlockMaterialId()
	{
		if (this.blockState == null)
		{
			return EDhApiBlockMaterial.AIR;
		}
		
		String serialString = this.getSerialString().toLowerCase();
		
		if (this.blockState.getBlock() instanceof BlockLeaves
				|| serialString.contains("bamboo")
				|| serialString.contains("cactus")
				|| serialString.contains("chorus_flower")
				|| serialString.contains("mushroom")
		)
		{
			return EDhApiBlockMaterial.LEAVES;
		}
		else if (this.blockState.getBlock() == Blocks.LAVA || this.blockState.getBlock() == Blocks.FLOWING_LAVA)
		{
			return EDhApiBlockMaterial.LAVA;
		}
		else if (this.isLiquid() || this.blockState.getBlock() == Blocks.WATER || this.blockState.getBlock() == Blocks.FLOWING_WATER)
		{
			return EDhApiBlockMaterial.WATER;
		}
		else if (this.blockState.getBlock().getSoundType() == SoundType.WOOD
				|| serialString.contains("root")
		)
		{
			return EDhApiBlockMaterial.WOOD;
		}
		else if (this.blockState.getBlock().getSoundType() == SoundType.METAL
		)
		{
			return EDhApiBlockMaterial.METAL;
		}
		else if (this.blockState.getBlock() instanceof BlockGrass)
		{
			return EDhApiBlockMaterial.GRASS;
		}
		else if (
				serialString.contains("dirt")
						|| serialString.contains("gravel")
						|| serialString.contains("mud")
						|| serialString.contains("podzol")
						|| serialString.contains("mycelium")
		)
		{
			return EDhApiBlockMaterial.DIRT;
		}
		else if (this.serialString.contains("snow"))
		{
			return EDhApiBlockMaterial.SNOW;
		}
		else if (serialString.contains("sand"))
		{
			return EDhApiBlockMaterial.SAND;
		}
		else if (serialString.contains("terracotta"))
		{
			return EDhApiBlockMaterial.TERRACOTTA;
		}
		else if (this.blockState.getBlock() == Blocks.NETHERRACK || this.blockState.getBlock() == Blocks.NETHER_BRICK)
		{
			return EDhApiBlockMaterial.NETHER_STONE;
		}
		else if (serialString.contains("stone")
				|| serialString.contains("ore"))
		{
			return EDhApiBlockMaterial.STONE;
		}
		else if (this.blockState.getLightValue() > 0)
		{
			return EDhApiBlockMaterial.ILLUMINATED;
		}
		else
		{
			return EDhApiBlockMaterial.UNKNOWN;
		}
	}
	
}
