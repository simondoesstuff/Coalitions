package org.simondoestuff.coalitions.Command.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.simondoestuff.coalitions.Command.Infrastructure.Cmd;
import org.simondoestuff.coalitions.GroupInfrastructure.CRole;
import org.simondoestuff.coalitions.GroupInfrastructure.CUser;
import org.simondoestuff.coalitions.GroupInfrastructure.Coalition;
import org.simondoestuff.coalitions.Plugin;

import java.util.ArrayList;
import java.util.List;


public class InfoCommand {
    private List<CRole> sort(List<CRole> inputList) {
        ArrayList<CRole> output = new ArrayList<>();


        output.add(inputList.get(0));
        for (int i = 1; i < inputList.size(); i++) {
            for (int j = 0; j < output.size() - 1; j++) {
                if (inputList.get(i).getPower() >= output.get(j).getPower())
                    output.add(j, inputList.get(j));

            }
        }


        return output;
    }

    @Cmd(handle = "info", args = "[CoalitionName]", description = "Lists information about a coalition.", perms = "coa.info")
    private void onInfo(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            Plugin.println("§4You must be a player to use that command.", sender);
            return;
        }

        Player p = (Player) sender;
        CUser user = CUser.fromPlayer(p);
        Coalition coalition;

        if (args.length == 1) {
            coalition = Coalition.fromName(args[0].toLowerCase());

            if (coalition == null) {
                Plugin.println("§4That coalition does not exist.", sender);
                return;
            }
        } else {
            coalition = user.getCoa();
        }

        if (coalition == null) {
            Plugin.println("§4You must be in a coalition to use that command.", sender);
            return;
        }


        StringBuilder allies = new StringBuilder();
        StringBuilder enemies = new StringBuilder();

        allies.append(" [").append(coalition.getAllies().size()).append("]");
        enemies.append(" [").append(coalition.getEnemies().size()).append("]");

        if (coalition.getEnemies().size() > 3) {
            for (int i = 0; i < coalition.getEnemies().size(); i++) {
                enemies.append("\n").append("    ");
                for (int j = 0; j < 3; j++) {
                    enemies.append(coalition.getEnemies().get(i));
                    if (i != coalition.getEnemies().size() - 1)
                        enemies.append(", ");
                }
            }

        } else
            enemies.append("    ");
        for (int i = 0; i < coalition.getEnemies().size(); i++) {
            enemies.append(coalition.getEnemies().get(i));
            if (i != coalition.getEnemies().size() - 1)
                enemies.append(", ");
        }


        if (coalition.getAllies().size() > 3) {
            for (int i = 0; i < coalition.getAllies().size(); i++) {
                allies.append("\n").append("    ");
                for (int j = 0; j < 3; j++) {
                    enemies.append(coalition.getAllies().get(i));
                    if (i != coalition.getAllies().size() - 1)
                        allies.append(", ");
                }
            }

        } else
            allies.append("    ");
        for (int i = 0; i < coalition.getAllies().size(); i++) {
            allies.append(coalition.getAllies().get(i));
            if (i != coalition.getAllies().size() - 1)
                allies.append(", ");
        }


        StringBuilder roles = new StringBuilder();
        ArrayList<CRole> temp = new ArrayList<>(sort(coalition.getRoles()));

        for (CRole role : temp) {
            roles.append(role.getName() + " [" + temp.size() + "]");
            if (coalition.usersInRole(role).size() > 3) {
                for (int i = 0; i < coalition.usersInRole(role).size(); i++) {
                    roles.append("\n").append("  ");
                    for (int j = 0; j < 3; j++)
                        roles.append(coalition.usersInRole(role).get(i).getP().getName());

                    if (i != coalition.usersInRole(role).size() - 1)
                        roles.append(", ");

                }
            } else
                for (int i = 0; i < coalition.usersInRole(role).size(); i++) {
                    roles.append("\n").append("    ");
                    roles.append(coalition.usersInRole(role).get(i).getP().getName());
                    if (i != coalition.usersInRole(role).size() - 1)
                        roles.append(", ");
                }
            roles.append("\n");
        }

        Plugin.println("\n§n" + coalition.getName() + "§3\nEnemies " + enemies.toString() + "\nAllies " + allies.toString() + "\n" + roles.toString(), sender);
    }
}
