package pr.lofe.crp.mfio.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pr.lofe.crp.mfio.MFIO;

import java.util.ArrayList;
import java.util.List;

public class ChangeFIOCommand implements CommandExecutor, TabCompleter {

    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender.hasPermission("mfio.changefio")) {

            if(strings.length < 3) {

                String message = MFIO.getInstance().getConfig().getString("messages.few_arguments");
                if(message != null && !message.equals(""))
                    commandSender.sendMessage(mm.deserialize(message));

            }
            else {
                Player player = Bukkit.getPlayer(strings[0]);
                if(player == null) {

                    String message = MFIO.getInstance().getConfig().getString("messages.player_not_found");
                    if(message != null && !message.equals(""))
                        commandSender.sendMessage(mm.deserialize(message.replaceAll("%PLAYER%", strings[0])));

                }
                else {
                    String raw = strings[1] + " " + strings[2];
                    if (raw.matches("^[А-ЯЁ][а-яё]* [А-ЯЁ][а-яё]*$")) {
                        String[] rawArgs = raw.split(" ");
                        if(rawArgs.length == 2) {

                            String message = MFIO.getInstance().getConfig().getString("messages.successful_fio_change");
                            if(message != null && !message.equals(""))
                                player.sendMessage(mm.deserialize(message));
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 2);

                            MFIO.getData().set(player.getUniqueId().toString(), raw);
                            MFIO.saveData();

                        }
                    }
                    else {

                        String message = MFIO.getInstance().getConfig().getString("messages.invalid_symbols");
                        if(message != null && !message.equals(""))
                            player.sendMessage(mm.deserialize(message));

                    }
                }
            }
        }
        else {

            String message = MFIO.getInstance().getConfig().getString("messages.no_permissions");
            if(message != null && !message.equals(""))
                commandSender.sendMessage(mm.deserialize(message));

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> returnArgs = new ArrayList<>();
        if(commandSender.hasPermission("mfio.changefio")) {
            if(strings.length == 0)
                return null;
            if (strings.length == 2)
                returnArgs.add("<Имя>");
            else if (strings.length == 3)
                returnArgs.add("<Фамилия>");
        }
        else returnArgs.add("");
        return returnArgs;
    }
}
