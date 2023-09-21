package pr.lofe.crp.mfio;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MFIOExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "mfio";
    }

    @Override
    public @NotNull String getAuthor() {
        return "just_l0fe";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Lists.newArrayList("fio", "surnamefio", "surname", "name");
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String raw = MFIO.getData().getString(player.getUniqueId().toString());
        String[] rawArgs = raw.split(" ");
        String name = "", surname = "";
        if(rawArgs.length == 2) {
            name = rawArgs[0];
            surname = rawArgs[1];
        }

        if(params.equalsIgnoreCase("fio"))
            return name + " " + surname;
        else if (params.equalsIgnoreCase("surnamefio")) {
            if(!surname.equals(""))
                return name + " " + surname.charAt(0);
        }
        else if (params.equalsIgnoreCase("surname"))
            return surname;
        else if (params.equalsIgnoreCase("name"))
            return name;
        return null;
    }
}
