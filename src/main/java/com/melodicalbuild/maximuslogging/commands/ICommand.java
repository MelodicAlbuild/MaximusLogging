package com.melodicalbuild.maximuslogging.commands;

import com.melodicalbuild.maximuslogging.MaximusLogging;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ICommand {

    void run(@NotNull final MaximusLogging pl, @NotNull final CommandSender sender, @NotNull final String[] args);

    @NotNull
    List<String> getTabCompletition(@NotNull final String[] args);

}
