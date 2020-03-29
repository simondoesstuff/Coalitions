package org.simondoestuff.coalitions.Command.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.simondoestuff.coalitions.Command.Infrastructure.Cmd;
import org.simondoestuff.coalitions.GroupInfrastructure.CUser;
import org.simondoestuff.coalitions.GroupInfrastructure.Coalition;
import org.simondoestuff.coalitions.Plugin;

import java.util.HashSet;
import java.util.UUID;

public class Creation {
    private HashSet<UUID> openDisbandReqs = new HashSet<>();

    @Cmd(handle = "new", args = "<name>", description = "Creates a new coalition.", perms = "coa.new")
    public void newCoalition(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        if (args.length != 1 || args[0] == null) {
            Plugin.println("§4Invalid arguments.", sender);
            return;
        }

        String name = args[0];
        Player p = (Player) sender;
        CUser user = CUser.fromPlayer(p);
        Coalition coa =  user.getCoa();

        if (coa != null) {
            Plugin.println("§4You must leave or disband your coalition first before creating a new one.", sender);
            return;
        }

        try {
            coa = new Coalition(name);
        } catch (IllegalArgumentException e) {
            Plugin.println("§4Failed to create coalition because that name is taken.", sender);
            return;
        }

        if (!coa.addUser(user)) {
            Plugin.println("§4Failed to create coalition.", sender);
            return;
        }

        coa.updateUserRole(user, coa.getRole("Captain"));
        Plugin.println("Created coalition " + name + ".", sender);
    }

    @Cmd(handle = "disband", args = "", description = "Disbands the coalition that you are in.", perms = "coa.disband")
    public void disbandCoa(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        if (args.length != 0) {
            Plugin.println("§4Too many arguments.", sender);
            return;
        }

        Player p = (Player) sender;
        CUser user = CUser.fromPlayer(p);
        Coalition coa =  user.getCoa();

        if (openDisbandReqs.contains(p.getUniqueId())) {
            Plugin.println("§4You already have a request open.", sender);
            return;
        }

        if (user.getCoa() == null) {
            Plugin.println("§4You can not disband a coalition if you are not in a coalition.", sender);
            return;
        }
        
        Plugin.println("You have 60s to type §a/coa confirmDisband §bto follow through with this action.", sender);
        openDisbandReqs.add(p.getUniqueId());

        new BukkitRunnable() {
            UUID uuid = p.getUniqueId();
            @Override
            public void run() {
                Plugin.println("Your request has timed out.", sender);
                openDisbandReqs.remove(uuid);
            }
        }.runTaskLater(Plugin.getInstance(), 60 * 20);
    }

    @Cmd(handle = "confirmDisband", args = "", description = "Confirm an open disband request.", perms = "", showInHelp = false)
    public void confirmDisbandCoa(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        if (args.length != 0) {
            Plugin.println("§4Too many arguments.", sender);
            return;
        }

        Player p = (Player) sender;

        if (!openDisbandReqs.contains(p.getUniqueId())) {
            Plugin.println("§4You do not have a request open.", sender);
            return;
        }

        CUser user = CUser.fromPlayer(p);
        Coalition coa =  user.getCoa();

        if (user.getCoa() == null) {
            Plugin.println("§4You can not disband a coalition if you are not in a coalition.", sender);
            return;
        }

        coa.disband();
        openDisbandReqs.remove(p.getUniqueId());
        Plugin.println("Disbanded coalition.", sender);
    }
}
