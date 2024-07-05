package pr.lofe.crp.mfio;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.*;

public class MFIOListener implements Listener {

    MiniMessage mm = MiniMessage.miniMessage();

    Collection<Player> inRegistrationProcess = new ArrayList<>();
    Collection<Player> inSexChoose = new ArrayList<>();

    HashMap<Player, String> tempFio = new HashMap<>();
    Collection<Player> inFioConfirm = new ArrayList<>();

    HashMap<Player, Sex> tempSex = new HashMap<>();
    Collection<Player> inSexConfirm = new ArrayList<>();

    @EventHandler public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String raw = mm.serialize(event.message());
        if(inRegistrationProcess.contains(player)) {
            event.setCancelled(true);
            processAction(player, raw);
        }
    }

    public void processAction(Player player, String raw) {
        if(inSexChoose.contains(player)) {

            if(inSexConfirm.contains(player)) {
                if(raw.equalsIgnoreCase("да")) {
                    MFIO.getData().set(player.getUniqueId()+ ".name", tempFio.get(player));
                    MFIO.getData().set(player.getUniqueId() + ".sex", tempSex.get(player).name());
                    MFIO.saveData();
                    String fio = tempFio.remove(player);
                    Sex sex = tempSex.remove(player);
                    inSexConfirm.remove(player);
                    inRegistrationProcess.remove(player);

                    MFIO.getInstance().getLogger().info("Player [" + player.getName() + "] registered with name: \"" + fio + "\".");


                    player.showTitle(Title.title(
                            mm.deserialize(PlaceholderAPI.setPlaceholders(player, MFIO.getInstance().getConfig().getString("messages.registered.title", ""))),
                            mm.deserialize(PlaceholderAPI.setPlaceholders(player, MFIO.getInstance().getConfig().getString("messages.registered.subtitle", "")))
                    ));

                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 2);

                    Bukkit.getScheduler().runTask(MFIO.getInstance(), () -> {
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        player.removePotionEffect(PotionEffectType.SLOW);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                MFIO.getInstance().getConfig().getString("sex." + sex.name().toLowerCase() + ".command_on_confirm", "")
                                        .replaceAll("%player%", player.getName()));
                    });
                }
                else if (raw.equalsIgnoreCase("нет")) {
                    String message = MFIO.getInstance().getConfig().getString("messages.cancel_choose");
                    if(message != null && !message.isEmpty())
                        player.sendMessage(mm.deserialize(message));
                    tempSex.remove(player);
                    inSexConfirm.remove(player);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);

                    player.showTitle(Title.title(
                            mm.deserialize(PlaceholderAPI.setPlaceholders(player, MFIO.getInstance().getConfig().getString("messages.register_sex.title", ""))),
                            mm.deserialize(PlaceholderAPI.setPlaceholders(player, MFIO.getInstance().getConfig().getString("messages.register_sex.subtitle", ""))),
                            Title.Times.times(Duration.ofMillis(250), Duration.ofDays(255), Duration.ofMillis(500))
                    ));
                }
                else {
                    String message = MFIO.getInstance().getConfig().getString("messages.unknown_argument");
                    if(message != null && !message.isEmpty())
                        player.sendMessage(mm.deserialize(message));
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);
                }
            }
            else {
                FileConfiguration cfg = MFIO.getInstance().getConfig();
                List<String> male = Arrays.asList(cfg.getString("sex.male.choose-answer", "").split("/"));
                List<String> female = Arrays.asList(cfg.getString("sex.female.choose-answer", "").split("/"));


                if(male.contains(raw) || female.contains(raw)) {
                    Sex temp = male.contains(raw) ? Sex.MALE : Sex.FEMALE;

                    tempSex.put(player, temp);
                    inSexConfirm.add(player);

                    String message = MFIO.getInstance().getConfig().getString("messages.confirm_sex");
                    if(message != null && !message.isEmpty())
                        player.sendMessage(mm.deserialize(message.replaceAll("%TEMP_SEX%", cfg.getString("sex." + tempSex.get(player).name().toLowerCase() + ".confirm-text", ""))));
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);
                }
                else {
                    String message = MFIO.getInstance().getConfig().getString("messages.invalid_sex");
                    if(message != null && !message.isEmpty())
                        player.sendMessage(mm.deserialize(message));
                }
            }
        }
        else if(inFioConfirm.contains(player)) {
            if(raw.equalsIgnoreCase("да")) {
                player.showTitle(Title.title(
                        mm.deserialize(PlaceholderAPI.setPlaceholders(player, MFIO.getInstance().getConfig().getString("messages.register_sex.title", ""))),
                        mm.deserialize(PlaceholderAPI.setPlaceholders(player, MFIO.getInstance().getConfig().getString("messages.register_sex.subtitle", ""))),
                        Title.Times.times(Duration.ofMillis(250), Duration.ofDays(255), Duration.ofMillis(500))
                ));
                inSexChoose.add(player);
            }
            else if (raw.equalsIgnoreCase("нет")) {
                String message = MFIO.getInstance().getConfig().getString("messages.cancel_choose");
                if(message != null && !message.isEmpty())
                    player.sendMessage(mm.deserialize(message));
                tempFio.remove(player);
                inFioConfirm.remove(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);
            }
            else {
                String message = MFIO.getInstance().getConfig().getString("messages.unknown_argument");
                if(message != null && !message.isEmpty())
                    player.sendMessage(mm.deserialize(message));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);
            }
        }
        else if (raw.matches("^[А-ЯЁ][а-яё]* [А-ЯЁ][а-яё]*$")) {
            String[] rawArgs = raw.split(" ");
            String name = rawArgs[0], surname = rawArgs[1];

            final int MIN_LENGTH = MFIO.getInstance().getConfig().getInt("options.min_length"),
                    MAX_LENGTH = MFIO.getInstance().getConfig().getInt("options.max_length");

            if(name.length() < MIN_LENGTH || surname.length() < MIN_LENGTH) {
                String message = MFIO.getInstance().getConfig().getString("messages.too_short");
                if(message != null && !message.isEmpty())
                    player.sendMessage(mm.deserialize(message));
                return;
            }

            if(name.length() > MAX_LENGTH || surname.length() > MAX_LENGTH) {
                String message = MFIO.getInstance().getConfig().getString("messages.too_short");
                if(message != null && !message.isEmpty())
                    player.sendMessage(mm.deserialize(message));
                return;
            }

            FileConfiguration data = MFIO.getData();
            for (String key: data.getKeys(false)) {
                String temp = data.getString(key + ".name", "");
                if(temp.equals(name + " " + surname)) {
                    String message = MFIO.getInstance().getConfig().getString("messages.already_registered");
                    if(message != null && !message.isEmpty())
                        player.sendMessage(mm.deserialize(message));
                    return;
                }
            }

            tempFio.put(player, raw);
            inFioConfirm.add(player);


            String message = MFIO.getInstance().getConfig().getString("messages.confirm_name");
            if(message != null && !message.isEmpty())
                player.sendMessage(mm.deserialize(message.replaceAll("%TEMP_FIO%", tempFio.get(player))));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);

        }
        else {
            String message = MFIO.getInstance().getConfig().getString("messages.invalid_symbols");
            if(message != null && !message.isEmpty())
                player.sendMessage(mm.deserialize(message));
        }
    }

    @EventHandler public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(inRegistrationProcess.contains(player))
            event.setCancelled(true);
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String raw = MFIO.getData().getString(playerUUID.toString());

        if(raw == null) {
            inRegistrationProcess.add(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 255, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true, false));
            player.showTitle(Title.title(
                    mm.deserialize(MFIO.getInstance().getConfig().getString("messages.register_name.title", "")),
                    mm.deserialize(MFIO.getInstance().getConfig().getString("messages.register_name.subtitle", "")),
                    Title.Times.times(Duration.ofMillis(250), Duration.ofDays(255), Duration.ofMillis(500))));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 100, 1);
        }

    }

    @EventHandler public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (inRegistrationProcess.contains(player))
                event.setCancelled(true);
        }
    }

    @EventHandler public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(inRegistrationProcess.contains(player)) {
            inRegistrationProcess.remove(player);
            inFioConfirm.remove(player);
            tempFio.remove(player);
        }
    }

}
