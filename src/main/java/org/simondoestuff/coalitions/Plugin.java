package org.simondoestuff.coalitions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.simondoestuff.coalitions.Command.Commands.Creation;
import org.simondoestuff.coalitions.Command.Commands.InfoCommand;
import org.simondoestuff.coalitions.Command.Commands.Other;
import org.simondoestuff.coalitions.Command.Infrastructure.AnnotatedCommandExecutor;

public final class Plugin extends JavaPlugin {
    private final static String prefix = "§8[§3Coalitions§8]";
    private static Plugin instance;
    private AnnotatedCommandExecutor primaryExecutor = new AnnotatedCommandExecutor();

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getCommand("coa").setExecutor(primaryExecutor);
        initCommands();
    }

    @Override
    public void onDisable() {
    }

    private void initCommands() {
        primaryExecutor.addClass(new Other());
        primaryExecutor.addClass(new InfoCommand());
        primaryExecutor.addClass(new Creation());
    }

    public static void println(String str, CommandSender... players) {
        println(str, true, players);
    }

    public static void println(String str, boolean addPrefix, CommandSender... players) {
        String msg = (addPrefix ? prefix  + " " : "") + "§b" + str;

        if (players == null || players.length == 0) {
            Bukkit.broadcastMessage(msg);
            return;
        }

        for (CommandSender player : players) {
            player.sendMessage(msg);
        }
    }
}
