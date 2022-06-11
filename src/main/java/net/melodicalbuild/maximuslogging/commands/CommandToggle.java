package net.melodicalbuild.maximuslogging.commands;

import net.melodicalbuild.maximuslogging.MaximusLogging;
import net.melodicalbuild.maximuslogging.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandToggle implements ICommand {

    @Override
    public void run(@NotNull final MaximusLogging pl, @NotNull final CommandSender sender, @NotNull final String[] args) {
        if (!sender.hasPermission("maximuslogging.commands.toggle")) {
            final String noperm = pl.getLogUtils().getColoredString("messages.noperm");
            if (noperm != null) sender.sendMessage(noperm);
            return;
        }

        pl.getLogConfig().setEnabled(!pl.getLogConfig().isEnabled());

        final String prefix = pl.getLogUtils().getColoredString("messages.prefix");
        if (pl.getLogConfig().isEnabled()) sender.sendMessage(prefix + Utils.color("&eSuperLog: &c&lOFF&e."));
        else sender.sendMessage(prefix + Utils.color("&eSuperLog: &2&lON&e."));
    }

    @NotNull
    @Override
    public List<String> getTabCompletition(@NotNull final String[] args) {
        return Collections.emptyList();
    }

}
