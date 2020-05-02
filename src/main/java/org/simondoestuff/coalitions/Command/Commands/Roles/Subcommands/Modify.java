//package org.simondoestuff.coalitions.Command.Commands.Roles.Subcommands;
//
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.simondoestuff.coalitions.Command.Infrastructure.Cmd;
//import org.simondoestuff.coalitions.GroupInfrastructure.CUser;
//import org.simondoestuff.coalitions.Plugin;
//
//public class Modify {
//
//    @Cmd(handle = "addPerm", args = "<RoleName> <Permission>", description = "Adds a permission to the given role.", perms = "coa.roles.addperm")
//    private void onAddperm(CommandSender sender, String[] arg) {
//        if (!(sender instanceof Player)) {
//            Plugin.println("§4You must be a player to use that command.", sender);
//            return;
//        }
//
//
//        Player p = (Player) sender;
//        CUser user = CUser.fromPlayer(p);
//
//        if (user.getCoa()==null){
//            Plugin.println("You must be in a coalition");
//            return;
//        }
//
//        if (user.getCoa().getRole(arg[0]))
//
//    }
//
//
//}
