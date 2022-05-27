package com.myuki69.enderquarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class TileEnderQuarry 
		extends TileEntity 
		implements  ITickable, IItemHandler
{
	private static final Random rand = new Random();
	public static int baseDrain = 1800;
	public static float hardnessDrain = 200.0F;
	
	public ArrayList<ItemStack> items = new ArrayList();
	public FluidTank tank = new FluidTank(32000);
	public EnergyStorage energy = new EnergyStorage(10000000);
	public int neededEnergy = -1;
	public int config = -1;
	
	public long progress = 0L;
	public boolean started = false;
	public boolean finished = false;
	
	
	public int dx = 1;
	public int dy = 0;
	public int dz = 0;
	
	int chunk_x = 0;
	int chunk_z = 0;
	int chunk_y = 0;
	
	int xCoord;
	int yCoord;
	int zCoord;
	
	int min_x;
	int max_x;
	int min_z;
	int max_z;
	
	private ForgeChunkManager.Ticket chunkTicket;
	private EntityPlayer owner;
	
	public static double[] powerMultipliers = { 1.0D, 1.0D, 1.5D, 5.0D, 20.0D, 80.0D, 1.0D, 1.5D, 2.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
	public boolean[] upgrades = new boolean[16];
	public static final int UPGRADE_BLANK = 0;
	public static final int UPGRADE_VOID = 1;
	public static final int UPGRADE_SILK = 2;
	public static final int UPGRADE_FORTUNE1 = 3;
	public static final int UPGRADE_FORTUNE2 = 4;
	public static final int UPGRADE_FORTUNE3 = 5;
	public static final int UPGRADE_SPEED1 = 6;
	public static final int UPGRADE_SPEED2 = 7;
	public static final int UPGRADE_SPEED3 = 8;
	public static final int UPGRADE_FLUID = 9;
	
	public TileEnderQuarry()
	{

		tank.setCanFill(false);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tags)
	{
	    this.energy.receiveEnergy(tags.getInteger("energy"), false);
	    int n = tags.getInteger("item_no");
	    this.items.clear();
	    for (int i = 0; i < n; i++)
	    {
	    	NBTTagCompound t = tags.getCompoundTag("item_" + i);
	    	this.items.add(new ItemStack(t));
	    }
	    if (tags.hasKey("fluid"))
	    	tank.fillInternal(FluidStack.loadFluidStackFromNBT(tags.getCompoundTag("fluid")), true);
	    
	    this.finished = tags.getBoolean("finished");
	    if (this.finished) 
	    	return;
	    
	    this.started = tags.getBoolean("started");
	    if (!this.started)
	    	return;
	    
	    min_x = tags.getInteger("min_x");
	    min_z = tags.getInteger("min_z");
	    max_x = tags.getInteger("max_x");
	    max_z = tags.getInteger("max_z");
	    chunk_x = tags.getInteger("chunk_x");
	    chunk_y = tags.getInteger("chunk_y");
	    chunk_z = tags.getInteger("chunk_z");
	    dx = tags.getInteger("dx");
	    dy = tags.getInteger("dy");
	    dz = tags.getInteger("dz");
	    progress = tags.getLong("progress");
	    super.readFromNBT(tags);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tags)
	{
	    tags.setInteger("energy", energy.getEnergyStored());
	    for (int i = 0; i < items.size(); i++)
	    {
	    	while (i < items.size() && items.get(i) == null)
	    		items.remove(i);
	      
	    	if (i < items.size())
	    	{
	    		NBTTagCompound t = new NBTTagCompound();
	    		((ItemStack)items.get(i)).writeToNBT(t);
	    		tags.setTag("item_" + i, t);
	    	}
	    }
	    tags.setInteger("item_no", items.size());
	    if (tank.getFluid() != null)
	    {
	    	NBTTagCompound t = new NBTTagCompound();
	    	tank.getFluid().writeToNBT(t);
	    	tags.setTag("fluid", t);
	    }
	    if (finished)
	    {
	    	tags.setBoolean("finished", true);
	    }
	    else if (started)
	    {
		    tags.setBoolean("started", true);
		    tags.setInteger("min_x", min_x);
		    tags.setInteger("max_x", max_x);
		    tags.setInteger("min_z", min_z);
		    tags.setInteger("max_z", max_z);
		    tags.setInteger("chunk_x", chunk_x);
		    tags.setInteger("chunk_y", chunk_y);
		    tags.setInteger("chunk_z", chunk_z);
		    tags.setInteger("dx", dx);
		    tags.setInteger("dy", dy);
		    tags.setInteger("dz", dz);
		    tags.setLong("progress", progress);
	    }
	    return super.writeToNBT(tags);
	}
	
	public boolean checkForMarkers(EntityPlayer player)
	{
		BlockPos thisBlockPos = this.getPos();
		System.out.println("Quarry checking at: " + thisBlockPos.getX() + " " + thisBlockPos.getY() + " " + thisBlockPos.getZ());
		for (EnumFacing face : EnumFacing.HORIZONTALS)
		{
			BlockPos vecin = thisBlockPos.offset(face);
			int offsetX = vecin.getX() - thisBlockPos.getX();
			int offsetZ = vecin.getZ() - thisBlockPos.getZ();
			
			int[] test = { this.world.provider.getDimension(), vecin.getX(), vecin.getY(), vecin.getZ() };
			int[] test_forward = null;
			int[] test_side = null;
	      
			boolean foundAttached = false;
			for (int[] a : TileEnderMarker.markers) 
			{
		        if (isIntEqual(a, test))
		        {
		        	foundAttached = true;
		        	break;
		        }
			}
			if (foundAttached)
			{
		        player.sendMessage(new TextComponentString("Found attached ender-marker"));
		        for (int[] a : TileEnderMarker.markers) 
		        {
		        	if ((a[0] == test[0]) && (a[2] == test[2]) && ((a[1] != test[1]) || (a[3] != test[3])))
			        {
			            if ((sign(a[1] - test[1]) == offsetX) && (sign(a[3] - test[3]) == offsetZ)) 
			            {
			            	if (test_forward == null)
			            		test_forward = a;
			            	else if (!isIntEqual(a, test_forward))
			            		player.sendMessage(new TextComponentString("Quarry marker square is ambiguous - multiple markers found at (" + a[1] + "," + a[3] + ") and (" + test_forward[1] + "," + test_forward[3] + ")"));
			            }
			            if (((offsetX == 0) && (a[3] == test[3])) || ((offsetZ == 0) && (a[1] == test[1]))) 
			            {
			            	if (test_side == null) 
			            		test_side = a;
			            	else if (!isIntEqual(a, test_side))
			            		player.sendMessage(new TextComponentString("Quarry marker square is ambiguous - multiple markers found at (" + a[1] + "," + a[3] + ") and (" + test_side[1] + "," + test_side[3] + ")"));
			            }
			        }
		        }
		        if (test_forward == null)
		        {
		        	player.sendMessage(new TextComponentString("Quarry marker square is incomplete"));
		        	return false;
		        }
		        if (test_side == null)
		        {
		        	player.sendMessage(new TextComponentString("Quarry marker square is incomplete"));
		        	return false;
		        }
		        int amin_x = Math.min(Math.min(test[1], test_forward[1]), test_side[1]);
		        int amax_x = Math.max(Math.max(test[1], test_forward[1]), test_side[1]);
		        int amin_z = Math.min(Math.min(test[3], test_forward[3]), test_side[3]);
		        int amax_z = Math.max(Math.max(test[3], test_forward[3]), test_side[3]);
		        if ((amax_x - amin_x <= 2) || (amax_z - amin_z <= 2))
		        {
		        	player.sendMessage(new TextComponentString("Region created by ender markers is too small"));
		        	return false;
		        }
		        player.sendMessage(new TextComponentString("Sucessfully established boundary"));
	
		        chunk_y = this.getPos().getY();
		        min_x = amin_x;
		        max_x = amax_x;
		        min_z = amin_z;
		        max_z = amax_z;
		        
		        startDig();
		        
		        return true;
			}
	    }
	    return false;
	}
	
	public static boolean isIntEqual(int[] a, int[] b)
	{
	    if (a == b)
	    	return true;
	    for (int i = 0; i < 4; i++) 
	    {
	    	if (a[i] != b[i])
	    		return false;
	    }
	    return true;
	}
	
	public static int sign(int d)
	{
	    if (d == 0)
	    	return 0;
	    if (d > 0) 
	    	return 1;
	    return -1;
	}
	
	public void startDig()
	{	
		System.out.println("Ender quarry started digging at (" + min_x + ", " + min_z + "), (" + max_x + ", " + max_z + ")");
		
	    this.started = true;
	    //this.chunk_y = yCoord;
	    this.chunk_x = (this.min_x + 1 >> 4);
	    this.chunk_z = (this.min_z + 1 >> 4);
	    this.dx = Math.max(0, this.min_x + 1 - (this.chunk_x << 4));
	    this.dy = this.chunk_y;
	    this.dz = Math.max(0, this.min_z + 1 - (this.chunk_z << 4));
	    if (!stopHere()) 
	    	nextBlock();
	    
	}
	
	@Override
	public void update()
	{
		xCoord = this.getPos().getX();
		yCoord = this.getPos().getY();
		zCoord = this.getPos().getZ();
		
		if(this.world.isRemote)
			return;
		if(config == -1)
			checkSurroundings();
		
		if (!started || finished) 
		      return;
		
		if (chunkTicket == null)
	    {
			chunkTicket = ForgeChunkManager.requestTicket(EnderQuarryMod.instance, this.world, ForgeChunkManager.Type.NORMAL);
		    if (chunkTicket == null)
		    {
		    	if (owner != null)
		    		owner.sendMessage(new TextComponentString("Problem registering chunk-preserving method"));
		        this.finished = true;
		        return;
		    }
		    chunkTicket.getModData().setString("id", "quarry");
		    chunkTicket.getModData().setInteger("x", xCoord);
		    chunkTicket.getModData().setInteger("y", yCoord);
		    chunkTicket.getModData().setInteger("z", zCoord);

	        ForgeChunkManager.forceChunk(chunkTicket, new ChunkPos(xCoord, zCoord));
	      
	        loadChunk();
	    }
		
		int nr = getSpeedStack();	//operations per tick
		for (int k = 0; k < nr; k++)
	    {
			if (hasRedstoneSignal() || (!items.isEmpty()) || (tank.getFluid() != null && tank.getFluidAmount() > 31000))
			{
			}
		
			else if (energy.getEnergyStored() >= neededEnergy && energy.extractEnergy(baseDrain, true) == baseDrain)
			{
				int x = (chunk_x << 4) + dx;
				int z = (chunk_z << 4) + dz;
				int y = dy;
				if (y >= 0)	
				{					
					if (mineBlock(x, y, z, upgrades[1] == false))
					{
						this.neededEnergy = -1;
						nextBlock();
					}
				}
				else
				{
					nextBlock();
				}
			}
			
			if (!items.isEmpty() && config > 0) 
			{
				int side_id = 0;
				BlockPos thisBlockPos = this.getPos();
				for (EnumFacing face : EnumFacing.values())
				{
					BlockPos vecin = thisBlockPos.offset(face);
					TileEntity tile = this.world.getTileEntity(vecin);
					if(tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite()))	//am gasit un cufar sau cv
				    {
						//System.out.println("Trying to put item at: " + vecin.getX() + ", " + vecin.getY() + ", " + vecin.getZ());
						IItemHandler itemhandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
						for(int i = 0; i < items.size(); ++i)
						{
							ItemStack remainder = ItemHandlerHelper.insertItemStacked(itemhandler, (ItemStack)items.get(i), false);
							if(remainder.isEmpty())
							{
								items.remove(i);
								--i;
							}
						}	 
				    }	
				}
			}
			if (tank.getFluid() != null && tank.getFluidAmount() > 0 && config > 0)
			{
				BlockPos thisBlockPos = this.getPos();
				for (EnumFacing face : EnumFacing.values())
				{
					BlockPos vecin = thisBlockPos.offset(face);
					TileEntity tile = this.world.getTileEntity(vecin);
					if(tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()))
					{
						IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite());
						int transfer = tank.getFluidAmount();

	                    tank.drain(handler.fill(tank.getFluid(), true), true);
					}

				}
	        }
	    }
	}

	public boolean mineBlock(int x, int y, int z, boolean replaceWithDirt)
	{
		BlockPos miningPos = new BlockPos (x, y, z);
		IFluidHandler handler = null;
		
	    IBlockState toBeMinedState = this.world.getBlockState(miningPos);
	    Block toBeMined = toBeMinedState.getBlock();
	    
	    if (this.world.isAirBlock(miningPos))
	      return true;
	    
        if (toBeMined instanceof BlockLiquid) 
            handler = new BlockLiquidWrapper((BlockLiquid) toBeMined, this.world, miningPos);
        else if (toBeMined instanceof IFluidBlock) {
            handler = new FluidBlockWrapper((IFluidBlock) toBeMined, this.world, miningPos);
        }

        if (handler != null) 	//fluid block
        {
        	if(upgrades[9])
        	{
	        	FluidStack drained = handler.drain(16000, false);
	
	            if (drained != null && tank.fillInternal(drained, false) == drained.amount) 
	            {
	                tank.fillInternal(handler.drain(16000, true), true);
	                world.setBlockState(miningPos, Blocks.AIR.getDefaultState());
	                //System.out.println("Draining " + drained.amount + " from " + x + ", " + y + ", " + z);
	                return true;
	            }
	            //if(drained != null)
	            	//System.out.println("DEBUG: draining failed: " + toBeMined.getLocalizedName() + " " + drained.getLocalizedName() + " " + drained.amount);
	            //else
	            if(drained == null)
	            {
	            	System.out.println("WARNING: Draining block at " + x + ", " + y + ", " + z + " - " + toBeMined.getLocalizedName() + " returned null! Skipping!");
	            	return true;
	            }
	            return false;
        	}
        	else
        		return true;
        }
	    
	    if(toBeMined == Blocks.BEDROCK)
	    	return true;
	    
	    if (replaceWithDirt && (toBeMined.isLeaves(toBeMinedState, this.world, miningPos) || toBeMined.isFoliage(this.world, miningPos) || toBeMined.isWood(this.world, miningPos) || (toBeMined instanceof IPlantable) || (toBeMined instanceof IGrowable)))
	    {
			return true;
	    }

		//check for tile entities with inventory (chests etc) and extract their items

	    int meta = toBeMined.getMetaFromState(toBeMinedState);
	    float hardness = toBeMinedState.getBlockHardness(this.world, miningPos);
	    
	    
	    if (hardness < 0.0F)
	    {
	      return true;
	    }
	    
	    int amount = (int)Math.ceil(baseDrain + hardness * hardnessDrain * getPowerMultiplier());	//cat RF o sa consume pentru acest block
	    if (amount > this.energy.getMaxEnergyStored()) 
	    {
	    	amount = this.energy.getMaxEnergyStored();
	    }

	    if (this.energy.extractEnergy(amount, true) < amount)
	    {
	      this.neededEnergy = amount;
	      return false;
	    }
	    this.energy.extractEnergy(amount, false);
	    
	    if(toBeMined == Blocks.GRASS && this.world.canSeeSky(miningPos))
	    {
	    	return true;
	    }
	    
	    return harvestBlock(toBeMinedState, miningPos, meta, replaceWithDirt, getDigType());
	}
	  
	public boolean harvestBlock(IBlockState toBeMinedState, BlockPos miningPos, int meta, boolean replaceWithDirt, DigType digType) 
	{
		Block toBeMined = toBeMinedState.getBlock();
		boolean isOpaque = toBeMinedState.isOpaqueCube();
	    FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer) this.world);
	    fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, digType.newStack(Items.DIAMOND_PICKAXE));
	    try
	    {
	    	boolean flag;
	        
	        
	        Object i = new ArrayList();
	        if ((digType.isSilkTouch()) && (toBeMined.canSilkHarvest(this.world, miningPos, toBeMinedState, fakePlayer)))
	        {
	        	int j = 0;
	        	Item item = Item.getItemFromBlock(toBeMined);
	        	if (item != null)
	        	{
	        		if (item.getHasSubtypes()) 
	        			j = meta;
	        		ItemStack itemstack = new ItemStack(item, 1, j);
	        		((ArrayList)i).add(itemstack);
	        	}
	        }
	        else
	        {
	        	((ArrayList)i).addAll(toBeMined.getDrops(this.world, miningPos, toBeMinedState, digType.getFortuneModifier()));

	        }
	        float p = ForgeEventFactory.fireBlockHarvesting((ArrayList)i, this.world, miningPos, toBeMinedState, digType.getFortuneModifier(), 1.0F, digType.isSilkTouch(), fakePlayer);
	        if ((p > 0.0F) && (!((ArrayList)i).isEmpty()) && ((p == 1.0F) || (rand.nextFloat() < p)))
	        	this.items.addAll((Collection)i);
	        
	        if(replaceWithDirt)
	        {
	        	if(toBeMined != Blocks.DIRT)
	        		flag = this.world.setBlockState(miningPos, Blocks.DIRT.getDefaultState());
	        	else
	        		return true;
	        }
	        else
	        	flag = this.world.setBlockState(miningPos, Blocks.AIR.getDefaultState());
	        
	        if (!flag)
	        {
	        	System.out.println("Error in setting block at mining pos!");
	        	return false;
	        }
	        
	      return true;
	    }
	    finally
	    {
	    	fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Blocks.DIRT));
	    }
	  
	}

	private DigType getDigType() 
	{
		if (this.upgrades[2] == true)
		    return DigType.SILK;
		    
		if (this.upgrades[3] == true) 
		    return DigType.FORTUNE;
		    
		if (this.upgrades[4] == true)
		    return DigType.FORTUNE2;
		
		if (this.upgrades[5] == true)
		    return DigType.FORTUNE3;
		    
		return DigType.NORMAL;
	}
	
	public void nextBlock() 
	{
		nextSubBlock();
	    while (!stopHere())
	    	nextSubBlock();
	}
	  
	public void nextSubBlock()
	{
	    this.progress += 1L;
	    this.dy -= 1;
	    if (this.dy <= 0)
	    {
	    	this.dx += 1;
	    	if ((this.dx >= 16) || ((this.chunk_x << 4) + this.dx >= this.max_x))
	    	{
	    		this.dx = Math.max(0, this.min_x + 1 - (this.chunk_x << 4));
	    		this.dz += 1;
	    		if ((this.dz >= 16) || ((this.chunk_z << 4) + this.dz >= this.max_z))
	    		{
	    			nextChunk();
	    			this.dx = Math.max(0, this.min_x + 1 - (this.chunk_x << 4));
	    			this.dz = Math.max(0, this.min_z + 1 - (this.chunk_z << 4));
	    		}
	    	}
	    	this.dy = this.chunk_y;
	    }
	}
	  
	public void nextChunk()
	  {
	    unloadChunk();
	    this.chunk_x += 1;
	    if (this.chunk_x << 4 >= this.max_x)
	    {
	      this.chunk_x = (this.min_x + 1 >> 4);
	      this.chunk_z += 1;
	      if (this.chunk_z << 4 >= this.max_z)
	      {
	        this.finished = true;
	        //this.world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 2);
	        ForgeChunkManager.releaseTicket(this.chunkTicket);
	        return;
	      }
	    }
	    this.dy = this.chunk_y;
	    loadChunk();
	  }
	  
	public boolean stopHere()
	  {
		  return (this.finished) || (isValid((this.chunk_x << 4) + this.dx, (this.chunk_z << 4) + this.dz));
	  }
	  
	private boolean isValid(int x, int z)
	  {
	    return (this.min_x < x) && (x < this.max_x) && (this.min_z < z) && (z < this.max_z);
	  }
	  
	private double getPowerMultiplier()
	{
		double multiplier = 1.0D;
		for (int i = 0; i < 16; i++)
		{
		    if (upgrades[i])
		        multiplier *= powerMultipliers[i];
		}
		return multiplier;
	}
	
	private int getSpeedStack()
	{
	    if (upgrades[6])
	    	return 1;
	    
	    if (upgrades[7])
	    	return 3;
	    
	    if (upgrades[8])
	    	return 9;
	    
	    return 1;
	}
	
	private void loadChunk()
	{
		
		ChunkPos base = new ChunkPos(xCoord, zCoord);
		if(base.x != chunk_x || base.z != chunk_z)
			ForgeChunkManager.forceChunk(chunkTicket, new ChunkPos(chunk_x, chunk_z));
		    
	}
	  
	private void unloadChunk()
	{
		ChunkPos base = new ChunkPos(xCoord, zCoord);
		if(base.x != chunk_x || base.z != chunk_z)
			ForgeChunkManager.unforceChunk(chunkTicket, new ChunkPos(chunk_x, chunk_z));	//ne asiguram ca nu stergem din ram chunkul in care e ender quarry
	}
	
	public void checkSurroundings()
	{
		config = 1;
		for(int i = 0; i <= 10; ++i)
			upgrades[i] = false;
		
		BlockPos thisBlockPos = this.getPos();
		for (EnumFacing face : EnumFacing.values())
	    {
			BlockPos vecin = thisBlockPos.offset(face);
			if (world.getBlockState(vecin).getBlock() instanceof UpgradeBlock) 
			{
		        upgrades[((UpgradeBlock)world.getBlockState(vecin).getBlock()).upgradeType] = true;
		        //System.out.println("Found an upgrade of type " + ((UpgradeBlock)world.getBlockState(vecin).getBlock()).upgradeType);
		    }
		}
	}
	
	public boolean hasRedstoneSignal()
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (world.isSidePowered(pos.offset(enumfacing), enumfacing))
            {
                return true;
            }
        }

        if (world.isSidePowered(pos, EnumFacing.DOWN))
        {
            return true;
        }
        else
        {
            BlockPos blockpos = pos.up();

            for (EnumFacing enumfacing1 : EnumFacing.values())
            {
                if (enumfacing1 != EnumFacing.DOWN && world.isSidePowered(blockpos.offset(enumfacing1), enumfacing1))
                {
                    return true;
                }
            }

            return false;
        }
    }
	
	public int invInsert(IItemHandler itemhandler, ItemStack toBeInserted)
	{	  
		for(int i = 0; i < itemhandler.getSlots(); ++i)
		{
			ItemStack cur = itemhandler.getStackInSlot(i);
			if(cur.isEmpty())
			{
				itemhandler.insertItem(i, toBeInserted, false);
				return 1;
    		}
			else
			{
				if(ItemStack.areItemsEqual(toBeInserted, cur))
				{
					if(cur.getCount() == cur.getMaxStackSize())
						continue;
					if(cur.getCount() + toBeInserted.getCount() <= cur.getMaxStackSize())
					{
						itemhandler.insertItem(i, toBeInserted, false);
						return 1;
					}
					else
					{
						int ammount = cur.getMaxStackSize() - cur.getCount();
						ItemStack remainder = toBeInserted;
						remainder.shrink(ammount);
						toBeInserted.setCount(ammount);
						items.add(remainder);
					}
    			}
    		}
    	}
		return 0;
	}
	
	public static enum DigType
	{
	    NORMAL(null, 0),  SILK(Enchantments.SILK_TOUCH, 1),  FORTUNE(Enchantments.FORTUNE, 1),  FORTUNE2(Enchantments.FORTUNE, 2),  FORTUNE3(Enchantments.FORTUNE, 3),  SPEED(Enchantments.EFFICIENCY, 1),  SPEED2(Enchantments.EFFICIENCY, 3),  SPEED3(Enchantments.EFFICIENCY, 5);
	    
	    public Enchantment ench;
	    public int level;
	    
	    private DigType(Enchantment ench, int level)
	    {
	    	this.ench = ench;
	    	this.level = level;
	    }
	    
	    public int getFortuneModifier()
	    {
	    	if (this.ench == Enchantments.FORTUNE)
	    		return this.level;
	    	return 0;
	    }
	    
	    public ItemStack newStack(Item pick)
	    {
	    	ItemStack stack = new ItemStack(pick);
	    	if (ench != null)
	    		stack.addEnchantment(this.ench, this.level);
	    	return stack;
	    }
	    
	    public boolean isSilkTouch()
	    {
	      return this.ench == Enchantments.SILK_TOUCH;
	    }
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityEnergy.ENERGY)
			return true;
	  return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) this;
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		if (capability == CapabilityEnergy.ENERGY) 
			return (T) energy;
	  
		return super.getCapability(capability, facing);
	}
	
	//useless stuff so pipes connect to quarry
	@Override
	public int getSlots() 
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		if(slot != 0)
			return ItemStack.EMPTY;
		
		for(int i = 0; i < items.size(); ++i)
			return (ItemStack)items.get(i);
		
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		ItemStack eject;
		for(int i = 0; i < items.size(); ++i)
		{	
			eject = (ItemStack)items.get(i);
			if(simulate == false)
			{	
				items.remove(i);
				--i;
			}
			return eject;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) 
	{
		ItemStack firstStack;
		for(int i = 0; i < items.size(); ++i)
		{
			firstStack = (ItemStack)items.get(i);
			return firstStack.getMaxStackSize();
		}
		return 64;
	}
	
}
