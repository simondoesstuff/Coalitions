package org.simondoestuff.coalitions.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.simondoestuff.coalitions.Command.Infrastructure.Cmd;
import org.simondoestuff.coalitions.GroupInfrastructure.CPERM;
import org.simondoestuff.coalitions.GroupInfrastructure.CRole;
import org.simondoestuff.coalitions.GroupInfrastructure.CUser;
import org.simondoestuff.coalitions.GroupInfrastructure.Coalition;
import org.simondoestuff.coalitions.Plugin;

public class Other {
    // "<name>\n    Creates a new coalition."
    // Created by Nathan Languerand, 3/28/2020
    @Cmd(handle = "sethome", args = "", description = "Sets your coalition home at your location.", perms = "coa.sethome")
    private void onSethome(CommandSender sender, String[] args) {
        // sender -> player -> CUser

        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        Player p = (Player) sender;
        CUser user = CUser.fromPlayer(p);

        if (user.getCoa() != null && user.getRole().hasPermission(CPERM.SETHOME)) {
            user.getCoa().setHome(p.getLocation());
            Plugin.println("Your coalition's home has been set.", sender);
        } else if (user.getCoa() == null) {
            Plugin.println("§4You are not in a coalition.", sender);
        } else if (!user.getRole().hasPermission(CPERM.SETHOME)) {
            Plugin.println("§4You do not meet the required rank to set a home.", sender);
        } else {
            Plugin.println("§4Error, try again. If you continue to have this problem, please contact the developer.");
        }
    }


    @Cmd(handle = "home", args = "", description = "Teleports you to your coalition's home.", perms = "coa.home")
    private void onHome(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        Player p = (Player) sender;
        CUser user = CUser.fromPlayer(p);

        if (user.getCoa() == null) {
            Plugin.println("§4You are not in a coalition.", sender);
            return;
        }

        if (user.getCoa().getHome() == null) {
            Plugin.println("§4The coalition you are in does not have a home set.", sender);
            return;
        }

        p.teleport(user.getCoa().getHome());
        Plugin.println("Teleporting...", sender);
    }

    @Cmd(handle = "setrole", args = "<Player> <RoleName>", description = "Set a coalition member's role.", perms = "coa.setrole")           // move to rolesRoot
    private void setUserRole(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        if (args.length != 2) {
            Plugin.println("§4Incorrect number of arguments.", sender);
            return;
        }

        Player p = Bukkit.getPlayer(args[0]);
        Player senderP = (Player) sender;

        if (p == null) {
            Plugin.println("§4Player not found.", sender);
            return;
        }

        CUser user = CUser.fromPlayer(p);
        CUser senderUser = CUser.fromPlayer(senderP);
        Coalition coa = user.getCoa();
        CRole role = coa.getRole(args[1]);

        if (role == null) {
            Plugin.println("§4Role not found.", sender);
            return;
        }

//        -------------------------------

        if (!user.getCoa().equals(senderUser.getCoa())) {
            Plugin.println("§4The user must be in the same coalition.", sender);
            return;
        }

        if (user.getRole().getPower() <= senderUser.getRole().getPower()) {
            Plugin.println("§4The user must have a higher power value than you. (Lower power is better; the captain has power 1 by default)", sender);
            return;
        }

        coa.updateUserRole(user, role);
        Plugin.println("Done.", sender);
        Plugin.println("Your role has been updated to " + role.getName() + ".", user.getP());
    }
}
