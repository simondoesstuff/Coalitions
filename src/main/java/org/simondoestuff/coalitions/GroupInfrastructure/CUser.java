package org.simondoestuff.coalitions.GroupInfrastructure;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CUser {
    private static HashMap<UUID, CUser> allUsers = new HashMap<>();

    public static CUser fromPlayer(Player p) {
        if (!allUsers.containsKey(p.getUniqueId())) allUsers.put(p.getUniqueId(), new CUser(null, p, null));

        return allUsers.get(p.getUniqueId());
    }

//    ----------------------------

    private Coalition coa;
    private UUID p;
    private CRole role;

    // package construct
    private CUser(Coalition coa, Player p, CRole role) {
        this.coa = coa;
        this.p = p.getUniqueId();
        this.role = role;
    }

    public Coalition getCoa() {
        return coa;
    }

    public Player getP() {
        return Bukkit.getPlayer(p);
    }

    public CRole getRole() {
        return role;
    }

    // ------------------------- all package private, used much in Coalitions.java


    void setCoa(Coalition coa) {
        this.coa = coa;
    }

    void setP(Player p) {
        this.p = p.getUniqueId();
    }

    void setRole(CRole role) {
        this.role = role;
    }
}
