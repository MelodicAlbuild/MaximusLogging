package net.melodicalbuild.maximuslogging.commands;

import net.melodicalbuild.maximuslogging.MaximusLogging;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSave implements ICommand {

    @Override
    public void run(@NotNull final MaximusLogging pl, @NotNull final CommandSender sender, @NotNull final String[] args) {
        if (!sender.hasPermission("maximuslogging.commands.save")) {
            final String noperm = pl.getLogUtils().getColoredString("messages.noperm");
            if (noperm != null) sender.sendMessage(noperm);
            return;
        }

        final String prefix = pl.getLogUtils().getColoredString("messages.prefix");
        Bukkit.getScheduler().runTaskAsynchronously(pl, () -> {
            final int count = pl.getLogManager().saveAll();
            final String logMessage = pl.getLogUtils().getColoredString("messages.logs-saved");
            if (logMessage != null) sender.sendMessage(prefix + logMessage.replace("{LOGS}", Integer.toString(count)));
        });
    }

    @NotNull
    @Override
    public List<String> getTabCompletition(@NotNull final String[] args) {
        return Collections.emptyList();
    }
}

