package com.myuki69.enderquarry;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = EnderQuarryMod.MODID, name = EnderQuarryMod.NAME, version = EnderQuarryMod.VERSION)
public class EnderQuarryMod 
{
	@Mod.Instance("enderquarrymod")
	public static EnderQuarryMod instance;
    public static final String MODID = "enderquarrymod";
    public static final String NAME = "Ender Quarry Mod";
    public static final String VERSION = "1.1.1";


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	InitBlocks.init();
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	ForgeChunkManager.setForcedChunkLoadingCallback(instance, new QuarryChunkLoadingCallback());
    }

}