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
        String fioRaw = MFIO.getData().getString(player.getUniqueId() + ".name", "Имя Фамилия");
        String[] rawArgs = fioRaw.split(" ");
        String name = "", surname = "";
        if(rawArgs.length == 2) {
            name = rawArgs[0];
            surname = rawArgs[1];
        }

        switch (params.toLowerCase()) {
            case "fio" -> {
                return fioRaw;
            }
            case "surnamefio" -> {
                return surname.isEmpty() ? "Имя Ф" : name + " " + surname.charAt(0);
            }
            case "surname" -> {
                return surname;
            }
            case "name" -> {
                return name;
            }
            case "sex" -> {
                String sex = MFIO.getData().getString(player.getUniqueId() + ".sex", "");
                return MFIO.getInstance().getConfig().getString("sex." + sex.toLowerCase() + ".placeholder", MFIO.getInstance().getConfig().getString("sex.empty.placeholder", ""));
            }
            default -> {
                return "";
            }
        }
    }
}
