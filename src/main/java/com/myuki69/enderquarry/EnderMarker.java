package com.myuki69.enderquarry;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnderMarker extends Block
{
	public static int[] dx = { 0, 0, 1, -1 };
	public static int[] dz = { 1, -1, 0, 0 };
	
    public static final PropertyInteger META = PropertyInteger.create("meta", 0, 32);
    protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.8D, 0.6000000238418579D);
    
	private static final String UNLOCALIZED_NAME = "endermarker";
	
	public int meta;
	
	public EnderMarker()
	{
		super(Material.ROCK);

        this.setUnlocalizedName(UNLOCALIZED_NAME);
        this.setRegistryName(UNLOCALIZED_NAME);
        this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setHardness(2.0F);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		return STANDING_AABB;
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEnderMarker();
	}
	
	@Override
    protected BlockStateContainer createBlockState () 
	{
        return new BlockStateContainer(this, new IProperty[] { META });
    }
	
	@Override
    public int getMetaFromState (IBlockState state) 
	{    
        return 0;
    }
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
		meta = state.getValue(META);
		//System.out.println("Meta = " + meta);
		for (int i = 0; i < 4; i++) 
	    {
	    	if ((meta & 1 << i) != 0) 
	    	{
	    		for (int l = 0; l < 3; l++) 
	    			world.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5D + dx[i] * rand.nextDouble() * rand.nextDouble(), pos.getY() + 0.5D, pos.getZ() + 0.5D + dz[i] * rand.nextDouble() * rand.nextDouble(), 0.501D, 0.0D, 1.0D);

	    	}
	    }
    }
	
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState iState)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState iState)
	{
		return false;
	}
}
