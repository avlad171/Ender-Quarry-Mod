package com.myuki69.enderquarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpgradeBlock extends Block 
{
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    protected static final AxisAlignedBB CUSTOM_AABB = new AxisAlignedBB(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
    
    public int upgradeType = 0;
	public UpgradeBlock(String name, int type) 
	{
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setHardness(4.0F);
		this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.upgradeType = type;
        
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
    }
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		return CUSTOM_AABB;
    }
	
	public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if(block instanceof EnderQuarry)
	        return true;
        return false;
    }
	
	@Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        return canConnectTo(world, pos.offset(facing), facing.getOpposite());
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(NORTH, canBeConnectedTo(worldIn, pos, EnumFacing.NORTH))
                    .withProperty(EAST,  canBeConnectedTo(worldIn, pos, EnumFacing.EAST))
                    .withProperty(SOUTH, canBeConnectedTo(worldIn, pos, EnumFacing.SOUTH))
                    .withProperty(WEST,  canBeConnectedTo(worldIn, pos, EnumFacing.WEST))
                    .withProperty(UP,  canBeConnectedTo(worldIn, pos, EnumFacing.UP))
                    .withProperty(DOWN, canBeConnectedTo(worldIn, pos, EnumFacing.DOWN));
    }
	
	@Override
    protected BlockStateContainer createBlockState () 
	{
        return new BlockStateContainer(this, new IProperty[] { DOWN, UP, NORTH, SOUTH, WEST, EAST });
    }
	
	@Override
    public int getMetaFromState (IBlockState state) 
	{    
        return 0;
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
