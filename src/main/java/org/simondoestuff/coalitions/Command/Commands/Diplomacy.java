package org.simondoestuff.coalitions.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simondoestuff.coalitions.Plugin;

public class Diplomacy {

    private void hi(Player p) {
        Plugin.println("broadcast to all");
        Plugin.println("reply", p);
        Plugin.println("reply to multiple", p, Bukkit.getPlayer("simondoestuff"));
        Plugin.println("reply to multiple without prefix", false, p, Bukkit.getPlayer("simondoestuff"));
    }
}
