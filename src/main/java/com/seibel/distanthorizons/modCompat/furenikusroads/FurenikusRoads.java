package com.seibel.distanthorizons.modCompat.furenikusroads;

import com.seibel.distanthorizons.common.wrappers.block.ClientBlockStateColorCache;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class FurenikusRoads
{
	public static final List<String> BLOCKS_TINT_BELOW = Arrays.asList(
			//Line paints
			//White Lines
			"furenikusroads:line_white_straight_full",
			"furenikusroads:line_white_straight_thick",
			"furenikusroads:line_white_straight_double",
			"furenikusroads:line_white_straight_double_thick",
			"furenikusroads:line_white_side_double",
			"furenikusroads:line_white_side_double_thick",
			"furenikusroads:line_white_side_single",
			"furenikusroads:line_white_side_single_thick",
			"furenikusroads:line_white_far_side",
			"furenikusroads:line_white_far_side_thick",
			"furenikusroads:line_white_middle_half_double",
			"furenikusroads:line_white_middle_dash_double",
			"furenikusroads:line_white_middle_short",
			"furenikusroads:line_white_middle_filter_lane",
			"furenikusroads:line_white_side_short",
			"furenikusroads:line_white_thin_crossing",
			//Yellow Lines
			"furenikusroads:line_yellow_straight_full",
			"furenikusroads:line_yellow_straight_thick",
			"furenikusroads:line_yellow_straight_double",
			"furenikusroads:line_yellow_straight_double_thick",
			"furenikusroads:line_yellow_side_double",
			"furenikusroads:line_yellow_side_double_thick",
			"furenikusroads:line_yellow_side_single",
			"furenikusroads:line_yellow_side_single_thick",
			"furenikusroads:line_yellow_far_side",
			"furenikusroads:line_yellow_far_side_thick",
			"furenikusroads:line_yellow_middle_half_double",
			"furenikusroads:line_yellow_middle_dash_double",
			"furenikusroads:line_yellow_middle_short",
			"furenikusroads:line_yellow_middle_filter_lane",
			"furenikusroads:line_yellow_side_short",
			"furenikusroads:line_yellow_thin_crossing",
			//Red Lines
			"furenikusroads:line_red_straight_full",
			"furenikusroads:line_red_straight_thick",
			"furenikusroads:line_red_straight_double",
			"furenikusroads:line_red_straight_double_thick",
			"furenikusroads:line_red_side_double",
			"furenikusroads:line_red_side_double_thick",
			"furenikusroads:line_red_side_single",
			"furenikusroads:line_red_side_single_thick",
			"furenikusroads:line_red_far_side",
			"furenikusroads:line_red_far_side_thick",
			"furenikusroads:line_red_middle_half_double",
			"furenikusroads:line_red_middle_dash_double",
			"furenikusroads:line_red_middle_short",
			"furenikusroads:line_red_middle_filter_lane",
			"furenikusroads:line_red_side_short",
			"furenikusroads:line_red_thin_crossing",
			
 			
 			//Icon paints
			//White Icons
			"furenikusroads:white_wheelchair_icon",
			"furenikusroads:white_chevron",
			"furenikusroads:white_pedestrian",
			"furenikusroads:white_merge_arrow",
			"furenikusroads:white_give_way",
			"furenikusroads:hatch_box_white",
			"furenikusroads:line_white_crossing_diagonal",
			"furenikusroads:white_crossing_paint",
			"furenikusroads:white_arrow",
			"furenikusroads:white_arrow_line",
			"furenikusroads:white_arrow_diagonal",
			//Yellow Icons
			"furenikusroads:yellow_wheelchair_icon",
			"furenikusroads:yellow_chevron",
			"furenikusroads:yellow_pedestrian",
			"furenikusroads:yellow_merge_arrow",
			"furenikusroads:yellow_give_way",
			"furenikusroads:hatch_box_yellow",
			"furenikusroads:line_yellow_crossing_diagonal",
			"furenikusroads:yellow_crossing_paint",
			"furenikusroads:yellow_arrow",
			"furenikusroads:yellow_arrow_line",
			"furenikusroads:yellow_arrow_diagonal",
			//Red Icons
			"furenikusroads:red_wheelchair_icon",
			"furenikusroads:red_chevron",
			"furenikusroads:red_pedestrian",
			"furenikusroads:red_merge_arrow",
			"furenikusroads:red_give_way",
			"furenikusroads:hatch_box_red",
			"furenikusroads:line_red_crossing_diagonal",
			"furenikusroads:red_crossing_paint",
			"furenikusroads:red_arrow",
			"furenikusroads:red_arrow_line",
			"furenikusroads:red_arrow_diagonal",
			
			
			//Letter paints
			//White Letters
			"furenikusroads:paint_letter_white_ab",
			"furenikusroads:paint_letter_white_cd",
			"furenikusroads:paint_letter_white_ef",
			"furenikusroads:paint_letter_white_gh",
			"furenikusroads:paint_letter_white_ij",
			"furenikusroads:paint_letter_white_kl",
			"furenikusroads:paint_letter_white_mn",
			"furenikusroads:paint_letter_white_op",
			"furenikusroads:paint_letter_white_qr",
			"furenikusroads:paint_letter_white_st",
			"furenikusroads:paint_letter_white_uv",
			"furenikusroads:paint_letter_white_wx",
			"furenikusroads:paint_letter_white_yz",
			"furenikusroads:paint_letter_white_01",
			"furenikusroads:paint_letter_white_23",
			"furenikusroads:paint_letter_white_45",
			"furenikusroads:paint_letter_white_67",
			"furenikusroads:paint_letter_white_89",
			"furenikusroads:paint_letter_white_punct_question_exclamation",
			"furenikusroads:paint_letter_white_punct_hash_slash",
			//Yellow Letters
			"furenikusroads:paint_letter_yellow_ab",
			"furenikusroads:paint_letter_yellow_cd",
			"furenikusroads:paint_letter_yellow_ef",
			"furenikusroads:paint_letter_yellow_gh",
			"furenikusroads:paint_letter_yellow_ij",
			"furenikusroads:paint_letter_yellow_kl",
			"furenikusroads:paint_letter_yellow_mn",
			"furenikusroads:paint_letter_yellow_op",
			"furenikusroads:paint_letter_yellow_qr",
			"furenikusroads:paint_letter_yellow_st",
			"furenikusroads:paint_letter_yellow_uv",
			"furenikusroads:paint_letter_yellow_wx",
			"furenikusroads:paint_letter_yellow_yz",
			"furenikusroads:paint_letter_yellow_01",
			"furenikusroads:paint_letter_yellow_23",
			"furenikusroads:paint_letter_yellow_45",
			"furenikusroads:paint_letter_yellow_67",
			"furenikusroads:paint_letter_yellow_89",
			"furenikusroads:paint_letter_yellow_punct_question_exclamation",
			"furenikusroads:paint_letter_yellow_punct_hash_slash",
			//Red Letters
			"furenikusroads:paint_letter_red_ab",
			"furenikusroads:paint_letter_red_cd",
			"furenikusroads:paint_letter_red_ef",
			"furenikusroads:paint_letter_red_gh",
			"furenikusroads:paint_letter_red_ij",
			"furenikusroads:paint_letter_red_kl",
			"furenikusroads:paint_letter_red_mn",
			"furenikusroads:paint_letter_red_op",
			"furenikusroads:paint_letter_red_qr",
			"furenikusroads:paint_letter_red_st",
			"furenikusroads:paint_letter_red_uv",
			"furenikusroads:paint_letter_red_wx",
			"furenikusroads:paint_letter_red_yz",
			"furenikusroads:paint_letter_red_01",
			"furenikusroads:paint_letter_red_23",
			"furenikusroads:paint_letter_red_45",
			"furenikusroads:paint_letter_red_67",
			"furenikusroads:paint_letter_red_89",
			"furenikusroads:paint_letter_red_punct_question_exclamation",
			"furenikusroads:paint_letter_red_punct_hash_slash",
			
			//Text Paints
			//White Texts
			"furenikusroads:white_slow",
			"furenikusroads:white_stop",
			"furenikusroads:white_bike",
			"furenikusroads:white_bus",
			"furenikusroads:white_taxi",
			"furenikusroads:white_lane",
			"furenikusroads:white_keep",
			"furenikusroads:white_clear",
			"furenikusroads:white_turn",
			"furenikusroads:white_left",
			"furenikusroads:white_right",
			"furenikusroads:white_only",
			"furenikusroads:white_no",
			"furenikusroads:white_entry",
			"furenikusroads:white_bike_icon",
			"furenikusroads:white_town",
			"furenikusroads:white_city",
			"furenikusroads:white_ctre",
			//Yellow Texts
			"furenikusroads:yellow_slow",
			"furenikusroads:yellow_stop",
			"furenikusroads:yellow_bike",
			"furenikusroads:yellow_bus",
			"furenikusroads:yellow_taxi",
			"furenikusroads:yellow_lane",
			"furenikusroads:yellow_keep",
			"furenikusroads:yellow_clear",
			"furenikusroads:yellow_turn",
			"furenikusroads:yellow_left",
			"furenikusroads:yellow_right",
			"furenikusroads:yellow_only",
			"furenikusroads:yellow_no",
			"furenikusroads:yellow_entry",
			"furenikusroads:yellow_bike_icon",
			"furenikusroads:yellow_town",
			"furenikusroads:yellow_city",
			"furenikusroads:yellow_ctre",
			//White Texts
			"furenikusroads:red_slow",
			"furenikusroads:red_stop",
			"furenikusroads:red_bike",
			"furenikusroads:red_bus",
			"furenikusroads:red_taxi",
			"furenikusroads:red_lane",
			"furenikusroads:red_keep",
			"furenikusroads:red_clear",
			"furenikusroads:red_turn",
			"furenikusroads:red_left",
			"furenikusroads:red_right",
			"furenikusroads:red_only",
			"furenikusroads:red_no",
			"furenikusroads:red_entry",
			"furenikusroads:red_bike_icon",
			"furenikusroads:red_town",
			"furenikusroads:red_city",
			"furenikusroads:red_ctre",
			
			
			//Junction Paints
			//White Junctions
			"furenikusroads:white_junction_filter_left",
			"furenikusroads:white_junction_filter_left_thin",
			"furenikusroads:white_junction_filter_left_empty",
			"furenikusroads:white_junction_filter_right",
			"furenikusroads:white_junction_filter_right_thin",
			"furenikusroads:white_junction_filter_right_empty",
			"furenikusroads:white_junction_fork_mid",
			"furenikusroads:white_junction_fork_mid_thin",
			"furenikusroads:white_junction_fork_chevron_mid",
			"furenikusroads:white_junction_fork_chevron_mid_thin",
			"furenikusroads:white_chevron_left_a",
			"furenikusroads:white_chevron_left_a_thin",
			"furenikusroads:white_chevron_right_a",
			"furenikusroads:white_chevron_right_a_thin",
			"furenikusroads:white_junction_side_line_connection",
			"furenikusroads:white_junction_side_line_connection_thin",
			"furenikusroads:white_junction_side_line_connection_thick_thick",
			"furenikusroads:white_junction_mid_line_connection",
			"furenikusroads:white_junction_a",
			"furenikusroads:white_junction_b",
			"furenikusroads:white_chevron_mid",
			"furenikusroads:white_chevron_mid_right",
			"furenikusroads:white_chevron_side_line",
			//Yellow Junctions
			"furenikusroads:yellow_junction_filter_left",
			"furenikusroads:yellow_junction_filter_left_thin",
			"furenikusroads:yellow_junction_filter_left_empty",
			"furenikusroads:yellow_junction_filter_right",
			"furenikusroads:yellow_junction_filter_right_thin",
			"furenikusroads:yellow_junction_filter_right_empty",
			"furenikusroads:yellow_junction_fork_mid",
			"furenikusroads:yellow_junction_fork_mid_thin",
			"furenikusroads:yellow_junction_fork_chevron_mid",
			"furenikusroads:yellow_junction_fork_chevron_mid_thin",
			"furenikusroads:yellow_chevron_left_a",
			"furenikusroads:yellow_chevron_left_a_thin",
			"furenikusroads:yellow_chevron_right_a",
			"furenikusroads:yellow_chevron_right_a_thin",
			"furenikusroads:yellow_junction_side_line_connection",
			"furenikusroads:yellow_junction_side_line_connection_thin",
			"furenikusroads:yellow_junction_side_line_connection_thick_thick",
			"furenikusroads:yellow_junction_mid_line_connection",
			"furenikusroads:yellow_junction_a",
			"furenikusroads:yellow_junction_b",
			"furenikusroads:yellow_chevron_mid",
			"furenikusroads:yellow_chevron_mid_right",
			"furenikusroads:yellow_chevron_side_line",
			//White Junctions
			"furenikusroads:red_junction_filter_left",
			"furenikusroads:red_junction_filter_left_thin",
			"furenikusroads:red_junction_filter_left_empty",
			"furenikusroads:red_junction_filter_right",
			"furenikusroads:red_junction_filter_right_thin",
			"furenikusroads:red_junction_filter_right_empty",
			"furenikusroads:red_junction_fork_mid",
			"furenikusroads:red_junction_fork_mid_thin",
			"furenikusroads:red_junction_fork_chevron_mid",
			"furenikusroads:red_junction_fork_chevron_mid_thin",
			"furenikusroads:red_chevron_left_a",
			"furenikusroads:red_chevron_left_a_thin",
			"furenikusroads:red_chevron_right_a",
			"furenikusroads:red_chevron_right_a_thin",
			"furenikusroads:red_junction_side_line_connection",
			"furenikusroads:red_junction_side_line_connection_thin",
			"furenikusroads:red_junction_side_line_connection_thick_thick",
			"furenikusroads:red_junction_mid_line_connection",
			"furenikusroads:red_junction_a",
			"furenikusroads:red_junction_b",
			"furenikusroads:red_chevron_mid",
			"furenikusroads:red_chevron_mid_right",
			"furenikusroads:red_chevron_side_line"
	);
	
	public static boolean checkFurenikusRoadsBlocks(ClientBlockStateColorCache instance, IBlockState blockState, ClientBlockStateColorCache.BlockColorInfo blockColorInfo) {
		String blockName = blockState.toString();
		if (!blockName.contains("furenikusroads")) return false;
		
		String color = null;
		if (blockName.contains("red")) {
			color = "red";
		} else if (blockName.contains("yellow")) {
			color = "yellow";
		} else if (blockName.contains("white")) {
			color = "white";
		} else {
			return false;
		}
		
		blockColorInfo.needPostTinting = true;
		blockColorInfo.needShade = false;
		blockColorInfo.tintIndex = 0;
		
		ResourceLocation blockId = new ResourceLocation("furenikusroads:road_block_" + color);
		Block block = ForgeRegistries.BLOCKS.getValue(blockId);
		
		if (block != null) {
			IBlockState state = block.getDefaultState();
			blockColorInfo.baseColor = instance.getParticleIconColor(state, ClientBlockStateColorCache.ColorMode.Flower);
		}
		
		return true;
	}
}
