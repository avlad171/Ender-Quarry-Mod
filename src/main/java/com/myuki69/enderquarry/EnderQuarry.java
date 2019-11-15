package com.myuki69.enderquarry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnderQuarry extends Block {
    private static final String UNLOCALIZED_NAME = "enderquarry";

    public EnderQuarry() {
        super(Material.ROCK);

        this.setUnlocalizedName(UNLOCALIZED_NAME);
        this.setRegistryName(UNLOCALIZED_NAME);
        this.setHardness(5.0F);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote) 
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEnderQuarry)
			{
				TileEnderQuarry quarry = (TileEnderQuarry) tile;
				
				if(quarry.finished)
				{
					player.sendMessage(new TextComponentString("Quarry has finished"));
				    return true;
				}
				if(quarry.started)
				{
					int x = (quarry.chunk_x << 4) + quarry.dx;
					int z = (quarry.chunk_z << 4) + quarry.dz;
					int y = quarry.dy;
					
					if(quarry.hasRedstoneSignal())
					{
						player.sendMessage(new TextComponentString("Quarry stopped due to redstone signal!"));
					}
					player.sendMessage(new TextComponentString("Stored energy: " + quarry.energy.getEnergyStored() + "/" + quarry.energy.getMaxEnergyStored()));
					player.sendMessage(new TextComponentString("Mining at: " + x + ", " + y + ", " + z));
					player.sendMessage(new TextComponentString("" + quarry.progress + " blocks scanned."));
					if(quarry.tank.getFluid() != null && quarry.tank.getFluidAmount() > 0)
					{
						player.sendMessage(new TextComponentString("Stored liquid: " + quarry.tank.getFluid().getLocalizedName() + ": " + quarry.tank.getFluidAmount() + "/" + quarry.tank.getCapacity()));
					}
					else
					{
						player.sendMessage(new TextComponentString("Tank empty"));
					}
					
				}
				else
				{
					player.sendMessage(new TextComponentString("Analyzing Fence boundary"));
					if(!quarry.checkForMarkers(player))
					{
						player.sendMessage(new TextComponentString("Failed to set up quarry"));
					}
				}
				
			}
				
		}
		return true;
	}
	
	@Override 
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
	    TileEntity tile;
	    if ((tile = worldIn.getTileEntity(pos)) instanceof TileEnderQuarry)
	    	((TileEnderQuarry)tile).checkSurroundings();    
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEnderQuarry();
	}

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
      return BlockRenderLayer.SOLID;
    }
}