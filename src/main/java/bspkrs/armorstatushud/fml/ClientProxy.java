package bspkrs.armorstatushud.fml;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.armorstatushud.ArmorStatusHUD;
import bspkrs.armorstatushud.CommandArmorStatus;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.ModVersionChecker;
import bspkrs.util.config.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ArmorStatusHUD.initConfig(event.getSuggestedConfigurationFile());
    }
    
    @Override
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new ASHGameTicker());
        FMLCommonHandler.instance().bus().register(new ASHRenderTicker());
        
        try
        {
            ClientCommandHandler.instance.registerCommand(new CommandArmorStatus());
        }
        catch (Throwable e)
        {}
        
        FMLCommonHandler.instance().bus().register(this);
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            ArmorStatusHUDMod.instance.versionChecker = new ModVersionChecker(Reference.MODID, ArmorStatusHUDMod.metadata.version, ArmorStatusHUDMod.instance.versionURL, ArmorStatusHUDMod.instance.mcfTopic);
            ArmorStatusHUDMod.instance.versionChecker.checkVersionWithLogging();
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MODID))
        {
            Reference.config.save();
            ArmorStatusHUD.syncConfig();
        }
    }
}
