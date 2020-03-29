package org.simondoestuff.coalitions.Command.Commands.Roles;

import org.simondoestuff.coalitions.Command.Commands.Roles.Subcommands.Other;
import org.simondoestuff.coalitions.Command.Infrastructure.AnnotatedCommandExecutor;

public class RolesRoot {
    private AnnotatedCommandExecutor subCmdExecutor = new AnnotatedCommandExecutor();

    private void initCommands() {
        subCmdExecutor.addClass(new Other());
    }

    public RolesRoot() {
        initCommands();
    }
}
