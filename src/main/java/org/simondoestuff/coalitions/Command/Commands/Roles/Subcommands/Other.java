package org.simondoestuff.coalitions.Command.Commands.Roles.Subcommands;

import org.bukkit.command.CommandSender;
import org.simondoestuff.coalitions.Plugin;
import org.simondoestuff.coalitions.Command.Infastructure.Cmd;

public class Other {
    @Cmd(handle = "test", description = "testing sub commands!", perms = "")
    private void test(CommandSender sender, String[] args) {
        Plugin.println("Whats up dog!", sender);
    }
}
