package pr.lofe.crp.mfio;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private File file;

    private FileConfiguration config;

    public Config(String name, File dataFolder) {
        this.file = new File(dataFolder, name);
        try {
            if (!this.file.exists() && !this.file.createNewFile())
                throw new IOException();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create " + name, e);
        }
        this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save " + this.file.getName(), e);
        }
    }
}