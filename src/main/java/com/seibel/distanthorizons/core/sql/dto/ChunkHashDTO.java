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

package com.seibel.distanthorizons.core.sql.dto;

import com.seibel.distanthorizons.core.dataObjects.fullData.sources.FullDataSourceV2;
import com.seibel.distanthorizons.core.pos.DhChunkPos;

/** handles storing {@link FullDataSourceV2}'s in the database. */
public class ChunkHashDTO implements IBaseDTO<DhChunkPos>
{
	public DhChunkPos pos;
	public int chunkHash;
	
	
	
	//=============//
	// constructor //
	//=============//
	
	public ChunkHashDTO(DhChunkPos pos, int chunkHash)
	{
		this.pos = pos;
		this.chunkHash = chunkHash;
	}
	
	
	
	//===========//
	// overrides //
	//===========//
	
	@Override 
	public DhChunkPos getKey() { return this.pos; }
	
	@Override
	public void close()
	{ /* no closing needed */ }
	
	
	
}
