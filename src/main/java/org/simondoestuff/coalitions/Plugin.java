package org.simondoestuff.coalitions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.simondoestuff.coalitions.Command.Infastructure.AnnotatedCommandExecutor;

public final class Plugin extends JavaPlugin {
    private final static String prefix = "§8[§3Coalitions§8]";
    private AnnotatedCommandExecutor primaryExecutor = new AnnotatedCommandExecutor();

    @Override
    public void onEnable() {
        getCommand("coa").setExecutor(primaryExecutor);
        initCommands();
    }

    @Override
    public void onDisable() {
    }

    private void initCommands() {
//        primaryExecutor.addClass(new TestCommands());
    }

    public static void println(String str, CommandSender... players) {
        println(str, true, players);
    }

    public static void println(String str, boolean addPrefix, CommandSender... players) {
        String msg = (addPrefix ? prefix : "") + "§b " + str;

        if (players == null || players.length == 0) {
            Bukkit.broadcastMessage(msg);
            return;
        }

        for (CommandSender player : players) {
            player.sendMessage(msg);
        }
    }
}
