package com.melodicalbuild.maximuslogging.commands;

import com.melodicalbuild.maximuslogging.MaximusLogging;
import com.melodicalbuild.maximuslogging.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandLive implements ICommand {

    @Override
    public void run(@NotNull final MaximusLogging pl, @NotNull final CommandSender sender, @NotNull final String[] args) {
        if (!sender.hasPermission("maximuslogging.commands.live")) {
            final String noperm = pl.getLogUtils().getColoredString("messages.noperm");
            if (noperm != null) sender.sendMessage(noperm);
            return;
        }

        final String prefix = pl.getLogUtils().getColoredString("messages.prefix");
        if (args.length < 2) {
            sender.sendMessage(prefix + Utils.color("&7Live command arguments:"));
            sender.sendMessage(prefix + Utils.color("  &7- &b<player>&7: player name to live log"));

            // Checking if the player is live logging someone
            final List<String> logging = pl.getLogManager().getLive().entrySet().stream()
                    .filter(e -> e.getValue().contains(sender))
                    .map(Map.Entry::getKey).collect(Collectors.toList());
            if (!logging.isEmpty()) {
                sender.sendMessage(prefix + Utils.color("  &7- &eYou are actually live logging:"));
                logging.forEach(p -> sender.sendMessage("    &7- " + p));
            }
            return;
        }

        final Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            sender.sendMessage(prefix + Utils.color("&cPlayer &e" + args[1] + "&c not found."));
            return;
        }

        final String pName = p.getName();

        // Getting senders for this target
        final List<CommandSender> senders = pl.getLogManager().getLive().getOrDefault(pName, new ArrayList<>());

        // Already logging, unlogging
        if (senders.contains(sender)) {
            senders.remove(sender);
            pl.getLogManager().getLive().put(pName, senders);
            sender.sendMessage(prefix + Utils.color("&eYou don't live log &2" + pName + "&e anymore."));
        } else {
            // Adding to log
            senders.add(sender);
            pl.getLogManager().getLive().put(pName, senders);
            sender.sendMessage(prefix + Utils.color("&2You are now live logging &a" + pName + "&2."));
        }
    }

    @NotNull
    @Override
    public List<String> getTabCompletition(@NotNull final String[] args) {
        if (args.length == 2) return StringUtil.copyPartialMatches(args[1], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), new ArrayList<>());
        return Collections.emptyList();
    }

}
