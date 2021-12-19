package com.melodicalbuild.maximuslogging;

import com.melodicalbuild.maximuslogging.commands.ICommand;
import com.melodicalbuild.maximuslogging.log.LogConfig;
import com.melodicalbuild.maximuslogging.log.LogManager;
import com.melodicalbuild.maximuslogging.log.LogUtils;
import com.melodicalbuild.maximuslogging.utils.Utils;
import com.melodicalbuild.maximuslogging.utils.metrics.Metrics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MaximusLogging extends JavaPlugin {
    private LogManager logManager;
    private LogConfig logConfig;
    private final LogUtils logUtils = new LogUtils(this);

    @Override
    public void onEnable() {
        // Loading as console
        load(Bukkit.getConsoleSender());
        // Metrics
        new Metrics(this, 8600);
        // Update?
        if (getConfig().getBoolean("check-update"))
            Bukkit.getScheduler().runTaskAsynchronously(this, logUtils::checkUpdate);
    }

    @Override
    public void onDisable() {
        getLogger().info("Saved " + logManager.saveAll() + " logs.");
    }

    public void load(@NotNull final CommandSender sender) {
        // Cleaning up'
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        // Default config
        saveDefaultConfig();
        reloadConfig();

        // Log Manager
        logManager = new LogManager(this, sender);

        // Config
        logConfig = new LogConfig(this, sender);
    }

    private final List<String> commands = Stream.of(
            "&3/log debug",
            "&3/log filter &b<player> <event>",
            "&3/log live &b<player>",
            "&3/log reload",
            "&3/log save",
            "&3/log toggle").map(Utils::color).collect(Collectors.toList());
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission("maximuslogging.commands.access")) {
            final String message = logUtils.getColoredString("messages.noperm");
            if (message != null) sender.sendMessage(message);
            return true;
        }

        final String prefix = logUtils.getColoredString("messages.prefix");

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) { // reload the plugin
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                // Saving logs
                final int count = logManager.saveAll();
                // Finished saving, we can come back sync
                getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                    String logMessage = logUtils.getColoredString("messages.logs-saved");
                    if (logMessage != null) {
                        logMessage = logMessage.replace("{LOGS}", Integer.toString(count));
                        sender.sendMessage(prefix + logMessage);
                    }
                    load(sender);
                    sender.sendMessage(prefix + Utils.color("&aPlugin reloaded!"));
                });
            });

            return true;
        }

        try {
            final ICommand cmd = (ICommand) Class.forName("com.melodicalbuild.maximuslogging.commands.Command" + args[0].toLowerCase()).getDeclaredConstructor().newInstance();
            cmd.run(this, sender, args);
        } catch (final Exception e) {
            sender.sendMessage(prefix + Utils.color("&7Command usage (&ev" + getDescription().getVersion() + "&7):"));
            commands.forEach(l -> sender.sendMessage(prefix + Utils.color("  &7- ") + l));
        }
        return true;
    }

    private final List<String> tab = Arrays.asList("debug", "filter", "live", "reload", "save", "toggle", "version");
    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        if (!sender.hasPermission("maximuslogging.commands.access")) return Collections.emptyList();
        if (args.length == 1) return StringUtil.copyPartialMatches(args[0], tab, new ArrayList<>());
        try {
            final ICommand cmd = (ICommand) Class.forName("com.melodicalbuild.maximuslogging.commands.Command" + args[0].toLowerCase()).getDeclaredConstructor().newInstance();
            return cmd.getTabCompletition(args);
        } catch (final Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Get the log manager instance
     * @return the log manager instance
     */
    @NotNull
    public LogManager getLogManager() {
        return logManager;
    }

    /**
     * Get the current log configuration
     * @return current log configuration
     */
    @NotNull
    public LogConfig getLogConfig() {
        return logConfig;
    }

    /**
     * Get the log utils
     * @return the log utils
     */
    @NotNull
    public LogUtils getLogUtils() {
        return logUtils;
    }
}
