package pr.lofe.crp.mfio;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pr.lofe.crp.mfio.commands.ChangeFIOCommand;
import pr.lofe.crp.mfio.commands.MFIOCommand;

public final class MFIO extends JavaPlugin {

    private static MFIO instance;
    private Config data;
    private MFIOListener listener;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        data = new Config("data.yml", getDataFolder());
        instance = this;

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new MFIOExpansion().register();
        else getLogger().warning("PlaceholderAPI not found.");

        getCommand("mfio").setExecutor(new MFIOCommand());
        getCommand("mfio").setTabCompleter(new MFIOCommand());

        getCommand("changefio").setExecutor(new ChangeFIOCommand());
        getCommand("changefio").setTabCompleter(new ChangeFIOCommand());

        listener = new MFIOListener();
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new MFIOExpansion().unregister();
        else getLogger().warning("Placeholders not loaded, skipping unload...");
    }
    public static FileConfiguration getData() {
        return instance.data.getConfig();
    }
    public static void saveData() {
        instance.data.save();
    }

    public static MFIO getInstance() {
        return instance;
    }
    public static MFIOListener getListener() { return instance.listener; }
}
