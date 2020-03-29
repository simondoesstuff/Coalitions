package org.simondoestuff.coalitions.Command.Commands.Roles;

import org.bukkit.command.CommandSender;
import org.simondoestuff.coalitions.Command.Commands.Roles.Subcommands.Other;
import org.simondoestuff.coalitions.Command.Infastructure.AnnotatedCommandExecutor;
import org.simondoestuff.coalitions.Command.Infastructure.Cmd;

public class RolesRoot {
    private AnnotatedCommandExecutor subCmdExecutor = new AnnotatedCommandExecutor();

    private void initCommands() {
        subCmdExecutor.addClass(new Other());
    }

    public RolesRoot() {
        initCommands();
    }

    @Cmd(handle = "roles", description = "Root command for creating custom roles.", perms = "")
    private void roles(CommandSender sender, String[] args) {

    }
}
