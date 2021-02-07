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
    public static final String VERSION = "1.1.0";


    /*public static EnderQuarry enderQuarry;
    public static EnderMarker enderMarker;
    public static UpgradeBlock upgradeVoid;
    public static UpgradeBlock upgradeSilk;
    public static UpgradeBlock upgradeFortune1;
    public static UpgradeBlock upgradeFortune2;
    public static UpgradeBlock upgradeFortune3;
    public static UpgradeBlock upgradeSpeed1;
    public static UpgradeBlock upgradeSpeed2;
    public static UpgradeBlock upgradeSpeed3;
    public static UpgradeBlock upgradePump;
    
    public static ItemBlock itemEnderQuarry;
    public static ItemBlock itemEnderMarker;
    public static ItemBlock itemUpgradeVoid;
    public static ItemBlock itemUpgradeSilk;
    public static ItemBlock itemUpgradeFortune1;
    public static ItemBlock itemUpgradeFortune2;
    public static ItemBlock itemUpgradeFortune3;
    public static ItemBlock itemUpgradeSpeed1;
    public static ItemBlock itemUpgradeSpeed2;
    public static ItemBlock itemUpgradeSpeed3;
    public static ItemBlock itemUpgradePump;
    
    private void registerUpgrade(UpgradeBlock block, ItemBlock itemblock, String name, int type)
    {
    	block = new UpgradeBlock(name, type);
        ForgeRegistries.BLOCKS.register(block);
        itemblock = new ItemBlock(block);
        itemblock.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(itemblock);
        ModelLoader.setCustomModelResourceLocation(itemblock, 0, new ModelResourceLocation(itemblock.getRegistryName(), "inventory"));
    }
	*/
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	InitBlocks.init();
    	/*enderQuarry = new EnderQuarry();
        ForgeRegistries.BLOCKS.register(enderQuarry);
        
        itemEnderQuarry = new ItemBlock(enderQuarry);
        itemEnderQuarry.setRegistryName(enderQuarry.getRegistryName());
        ForgeRegistries.ITEMS.register(itemEnderQuarry);
        ModelLoader.setCustomModelResourceLocation(itemEnderQuarry, 0, new ModelResourceLocation(itemEnderQuarry.getRegistryName(), "inventory"));
        
        GameRegistry.registerTileEntity(TileEnderQuarry.class, enderQuarry.getRegistryName().toString());
        
        enderMarker = new EnderMarker();
        ForgeRegistries.BLOCKS.register(enderMarker);
        itemEnderMarker = new ItemBlock(enderMarker);
        itemEnderMarker.setRegistryName(enderMarker.getRegistryName());
        ForgeRegistries.ITEMS.register(itemEnderMarker);
        //ModelLoader.setCustomModelResourceLocation(itemEnderMarker, 0, new ModelResourceLocation(itemEnderMarker.getRegistryName(), "inventory"));
        
        GameRegistry.registerTileEntity(TileEnderMarker.class, enderMarker.getRegistryName().toString());
        
        /*registerUpgrade(upgradeVoid, itemUpgradeVoid, "upgradevoid", 1);
        registerUpgrade(upgradeSilk, itemUpgradeSilk, "upgradesilk", 2);
        registerUpgrade(upgradeFortune1, itemUpgradeFortune1, "upgradefortunei", 3);
        registerUpgrade(upgradeFortune2, itemUpgradeFortune2, "upgradefortuneii", 4);
        registerUpgrade(upgradeFortune3, itemUpgradeFortune3, "upgradefortuneiii", 5);
        registerUpgrade(upgradeSpeed1, itemUpgradeSpeed1, "upgradespeedi", 6);
        registerUpgrade(upgradeSpeed2, itemUpgradeSpeed2, "upgradespeedii", 7);
        registerUpgrade(upgradeSpeed3, itemUpgradeSpeed3, "upgradespeediii", 8);
        registerUpgrade(upgradePump, itemUpgradePump, "upgradepump", 9);
		*/
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