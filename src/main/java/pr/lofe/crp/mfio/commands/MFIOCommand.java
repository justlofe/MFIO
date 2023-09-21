package pr.lofe.crp.mfio.commands;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
import java.util.Objects;

public class MFIOCommand implements CommandExecutor, TabCompleter {

    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender.hasPermission("mfio.admin")) {

            if(strings.length == 0) {
                String message = MFIO.getInstance().getConfig().getString("messages.few_arguments");
                if(message != null && !message.equals(""))
                    commandSender.sendMessage(mm.deserialize(message));
            }
            else if(strings[0].equalsIgnoreCase("reload")) {
                String message = MFIO.getInstance().getConfig().getString("messages.reloaded");
                if(message != null && !message.equals(""))
                    commandSender.sendMessage(mm.deserialize(message));

                MFIO.getInstance().reloadConfig();

                if(commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 2);
                }
            }
            else {

                String message = MFIO.getInstance().getConfig().getString("messages.unknown_argument");
                if(message != null && !message.equals(""))
                    commandSender.sendMessage(mm.deserialize(message));

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
        if(commandSender.hasPermission("mfio.admin"))
            if (strings.length == 1) returnArgs.add("reload");
        else returnArgs.add("");

        return returnArgs;
    }
}
