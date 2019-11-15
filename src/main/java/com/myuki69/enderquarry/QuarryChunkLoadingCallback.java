package com.myuki69.enderquarry;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import com.google.common.collect.Lists;


public class QuarryChunkLoadingCallback implements ForgeChunkManager.OrderedLoadingCallback
{	
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) 
	{
		
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) 
	{
		List<Ticket> validTickets = Lists.newArrayList();
		for(Ticket ticket: tickets)
		{
			String type = ticket.getModData().getString("id");
			if(type == "quarry")
			{
				int blockX = ticket.getModData().getInteger("x");
				int blockY = ticket.getModData().getInteger("y");
				int blockZ = ticket.getModData().getInteger("z");
				
				BlockPos quarryPos = new BlockPos(blockX, blockY, blockZ);
				TileEntity te1 = world.getTileEntity(quarryPos);
				if(te1 instanceof TileEnderQuarry)
					validTickets.add(ticket);
			}
		}
		return validTickets;
	}
}