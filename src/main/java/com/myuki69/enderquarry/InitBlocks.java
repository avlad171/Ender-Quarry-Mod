package com.myuki69.enderquarry;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = EnderQuarryMod.MODID)
public class InitBlocks {

	public static EnderQuarry enderQuarry;
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
	
    private static void init_upgrades()
    {
    	upgradeVoid = new UpgradeBlock("upgradevoid", 1);
        ForgeRegistries.BLOCKS.register(upgradeVoid);
        upgradeSilk = new UpgradeBlock("upgradesilk", 2);
        ForgeRegistries.BLOCKS.register(upgradeSilk);
        upgradeFortune1 = new UpgradeBlock("upgradefortunei", 3);
        ForgeRegistries.BLOCKS.register(upgradeFortune1);
        upgradeFortune2 = new UpgradeBlock("upgradefortuneii", 4);
        ForgeRegistries.BLOCKS.register(upgradeFortune2);
        upgradeFortune3 = new UpgradeBlock("upgradefortuneiii", 5);
        ForgeRegistries.BLOCKS.register(upgradeFortune3);
        upgradeSpeed1 = new UpgradeBlock("upgradespeedi", 6);
        ForgeRegistries.BLOCKS.register(upgradeSpeed1);
        upgradeSpeed2 = new UpgradeBlock("upgradespeedii", 7);
        ForgeRegistries.BLOCKS.register(upgradeSpeed2);
        upgradeSpeed3 = new UpgradeBlock("upgradespeediii", 8);
        ForgeRegistries.BLOCKS.register(upgradeSpeed3);
        upgradePump = new UpgradeBlock("upgradepump", 9);
        ForgeRegistries.BLOCKS.register(upgradePump);
        
        itemUpgradeVoid = new ItemBlock(upgradeVoid);
        itemUpgradeVoid.setRegistryName(upgradeVoid.getRegistryName());
        itemUpgradeSilk = new ItemBlock(upgradeSilk);
        itemUpgradeSilk.setRegistryName(upgradeSilk.getRegistryName());
        itemUpgradeFortune1 = new ItemBlock(upgradeFortune1);
        itemUpgradeFortune1.setRegistryName(upgradeFortune1.getRegistryName());
        itemUpgradeFortune2 = new ItemBlock(upgradeFortune2);
        itemUpgradeFortune2.setRegistryName(upgradeFortune2.getRegistryName());
        itemUpgradeFortune3 = new ItemBlock(upgradeFortune3);
        itemUpgradeFortune3.setRegistryName(upgradeFortune3.getRegistryName());
        itemUpgradeSpeed1 = new ItemBlock(upgradeSpeed1);
        itemUpgradeSpeed1.setRegistryName(upgradeSpeed1.getRegistryName());
        itemUpgradeSpeed2 = new ItemBlock(upgradeSpeed2);
        itemUpgradeSpeed2.setRegistryName(upgradeSpeed2.getRegistryName());
        itemUpgradeSpeed3 = new ItemBlock(upgradeSpeed3);
        itemUpgradeSpeed3.setRegistryName(upgradeSpeed3.getRegistryName());
        itemUpgradePump = new ItemBlock(upgradePump);
        itemUpgradePump.setRegistryName(upgradePump.getRegistryName());
    }
    
	public static void init() 
	{
		enderQuarry = new EnderQuarry();
		
		itemEnderQuarry = new ItemBlock(enderQuarry);
        itemEnderQuarry.setRegistryName(enderQuarry.getRegistryName());
        
        enderMarker = new EnderMarker();
        
        itemEnderMarker = new ItemBlock(enderMarker);
        itemEnderMarker.setRegistryName(enderMarker.getRegistryName());
        
        GameRegistry.registerTileEntity(TileEnderQuarry.class, enderQuarry.getRegistryName().toString());
        GameRegistry.registerTileEntity(TileEnderMarker.class, enderMarker.getRegistryName().toString());
        
        init_upgrades();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) 
	{
		//event.getRegistry().registerAll(tutorialBlock);
		ForgeRegistries.BLOCKS.register(enderQuarry);
		ForgeRegistries.BLOCKS.register(enderMarker);
		ForgeRegistries.BLOCKS.register(upgradeVoid);
		ForgeRegistries.BLOCKS.register(upgradeSilk);
		ForgeRegistries.BLOCKS.register(upgradeFortune1);
		ForgeRegistries.BLOCKS.register(upgradeFortune2);
		ForgeRegistries.BLOCKS.register(upgradeFortune3);
		ForgeRegistries.BLOCKS.register(upgradeSpeed1);
		ForgeRegistries.BLOCKS.register(upgradeSpeed2);
		ForgeRegistries.BLOCKS.register(upgradeSpeed3);
		ForgeRegistries.BLOCKS.register(upgradePump);
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		//event.getRegistry().registerAll(new ItemBlock(tutorialBlock).setRegistryName(tutorialBlock.getRegistryName()));
		ForgeRegistries.ITEMS.register(itemEnderQuarry);
		ForgeRegistries.ITEMS.register(itemEnderMarker);
		ForgeRegistries.ITEMS.register(itemUpgradeVoid);
		ForgeRegistries.ITEMS.register(itemUpgradeSilk);
		ForgeRegistries.ITEMS.register(itemUpgradeFortune1);
		ForgeRegistries.ITEMS.register(itemUpgradeFortune2);
		ForgeRegistries.ITEMS.register(itemUpgradeFortune3);
		ForgeRegistries.ITEMS.register(itemUpgradeSpeed1);
		ForgeRegistries.ITEMS.register(itemUpgradeSpeed2);
		ForgeRegistries.ITEMS.register(itemUpgradeSpeed3);
		ForgeRegistries.ITEMS.register(itemUpgradePump);
	}
	
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		registerRender(itemEnderQuarry);
		registerRender(itemEnderMarker);
		registerRender(itemUpgradeVoid);
		registerRender(itemUpgradeSilk);
		registerRender(itemUpgradeFortune1);
		registerRender(itemUpgradeFortune2);
		registerRender(itemUpgradeFortune3);
		registerRender(itemUpgradeSpeed1);
		registerRender(itemUpgradeSpeed2);
		registerRender(itemUpgradeSpeed3);
		registerRender(itemUpgradePump);
	}
	
	public static void registerRender(Item item) 
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}