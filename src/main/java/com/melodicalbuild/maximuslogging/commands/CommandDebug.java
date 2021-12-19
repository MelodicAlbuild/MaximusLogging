package com.melodicalbuild.maximuslogging.commands;

import com.melodicalbuild.maximuslogging.MaximusLogging;
import com.melodicalbuild.maximuslogging.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandDebug implements ICommand {

    @Override
    public void run(@NotNull final MaximusLogging pl, @NotNull final CommandSender sender, @NotNull final String[] args) {
        if (!sender.isOp()) {
            final String noperm = pl.getLogUtils().getColoredString("messages.noperm");
            if (noperm != null) sender.sendMessage(noperm);
            return;
        }

        final String prefix = pl.getLogUtils().getColoredString("messages.prefix");
        pl.getLogManager().setDebug(!pl.getLogManager().isDebug());

        if (!pl.getLogManager().isDebug()) sender.sendMessage(prefix + Utils.color("&eDebug: &c&lOFF&e."));
        else sender.sendMessage(prefix + Utils.color("&eDebug: &2&lON&e."));
    }

    @NotNull
    @Override
    public List<String> getTabCompletition(@NotNull final String[] args) {
        return Collections.emptyList();
    }


}
