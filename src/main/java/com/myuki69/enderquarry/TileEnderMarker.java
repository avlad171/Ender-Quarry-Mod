package com.myuki69.enderquarry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class TileEnderMarker extends TileEntity implements ITickable
{
	public static List<int[]> markers = new ArrayList();
	public boolean init = false;
	private int cnt = 0;
	public static int[] getCoord(TileEntity tile)
	{
		BlockPos thisBlockPos = tile.getPos();
		return new int[] { tile.getWorld().provider.getDimension(), thisBlockPos.getX(), thisBlockPos.getY(), thisBlockPos.getZ() };
	}
	
	public int[] getCoord()
	{
		return getCoord(this);
	}
	  
  	@Override
  	public void update()
  	{
  		if (!init)
  			onChunkLoad();
  	}
  	
  	@Override
  	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  	{
  		return oldState.getBlock() != newSate.getBlock();
  	}
  	
  	@Override
  	public void invalidate()
	{
  		super.invalidate();
	    if (world.isRemote)
	    	return;
	    
	    //System.out.println("Invalidated tile at: " + this.getPos().getX() + " " + this.getPos().getZ());
	    
	    int[] myCoord = getCoord();
	    
	    List<int[]> toUpdate = new ArrayList();
	    for (int i = 0; i < markers.size(); i++)
	    {
	    	int[] coord = (int[])markers.get(i);
	    	if ((myCoord[0] == coord[0]) && (myCoord[2] == coord[2])) 
	    	{
	    		if ((myCoord[3] == coord[3]) && (myCoord[1] == coord[1]))
	    		{
	    			markers.remove(i);
	    			i--;
	    		}
	    		else if ((myCoord[3] == coord[3]) || (myCoord[1] == coord[1]))
	    		{
	    			toUpdate.add(coord);
	    		}
	    	}
	    }
		for (int[] coord : toUpdate)
		{
		  	TileEntity tile = world.getTileEntity(new BlockPos(coord[1], coord[2], coord[3]));
		   	if ((tile instanceof TileEnderMarker))
		   		((TileEnderMarker)tile).recheck();
		}

	}
  
  	public void recheck()	//this func updates block info for the game to know which particles to spawn later
	{
  		//System.out.println("Rechecking at " + this.getPos().getX() + " " + this.getPos().getZ());
	    int[] myCoord = getCoord();
	    int flag = 0;
	    for (int[] coord : markers) 
	    {
	    	if ((myCoord[0] == coord[0]) && (myCoord[2] == coord[2]) && ((myCoord[1] != coord[1]) || (myCoord[3] != coord[3]))) 
	    	{
	    		if (myCoord[1] == coord[1])
	    			flag |= (myCoord[3] < coord[3] ? 1 : 2);
	    		else if (myCoord[3] == coord[3]) 
	    			flag |= (myCoord[1] < coord[1] ? 4 : 8);
	    	}
	    }
	    
	    IBlockState state = world.getBlockState(this.getPos());
	    world.setBlockState(this.getPos(), state.withProperty(EnderMarker.META, flag));
	}
  
  	public void onChunkLoad()
  	{
	    if (init)
	    	return;
	    init = true;
	    if ((world == null) || (world.isRemote)) 
	    	return;
	    
	    int[] myCoord = getCoord();
	    List<int[]> toUpdate = new ArrayList();
	    for (int[] coord : markers) 
	    {
	    	if ((myCoord[0] == coord[0]) && (myCoord[2] == coord[2]))
	    	{
	    		if ((myCoord[3] == coord[3]) && (myCoord[1] == coord[1])) 
	    			return;
	    		if ((myCoord[3] == coord[3]) || (myCoord[1] == coord[1]))
	    			toUpdate.add(coord);
	    	}
	    }
	    markers.add(myCoord);
	    recheck();
	    for (int[] coord : toUpdate)
	    {
	    	TileEntity tile = world.getTileEntity(new BlockPos(coord[1], coord[2], coord[3]));
	    	if ((tile instanceof TileEnderMarker))
	    		((TileEnderMarker)tile).recheck();
	      
	    }
	}
}