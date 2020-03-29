package org.simondoestuff.coalitions.GroupInfastructure;

import org.bukkit.Location;

import java.util.*;

public class Coalition {
    private HashSet<CUser> users = new HashSet<>();
    private LinkedList<CRole> roles = new LinkedList<>();
    private CRole defaultRole;

    private HashSet<Coalition> allies = new HashSet<>();
    private HashSet<Coalition> enemies = new HashSet<>();

    private Location home = null;

//    ------------------------------------------------

    public List<CUser> getUsers() {
        return new ArrayList<>(users);
    }

//    ------------------------------------------------

    public void warCoalition(Coalition c) {
        c.setEnemy(this);
        setEnemy(c);
    }

    void setEnemy(Coalition c) {
        allies.remove(c);
        enemies.add(c);
    }

    public void allyCoalition(Coalition c) {
        c.setAlly(this);
        setAlly(c);
    }

    void setAlly(Coalition c) {
        enemies.remove(c);
        allies.add(c);
    }

//    ------------------------------------------------

    public boolean removeUser(CUser p) {
        if (!users.contains(p)) return false;

        p.setCoa(null);
        p.setRole(null);
        users.remove(p);
        return true;
    }

    public boolean addUser(CUser p) {
        if (users.contains(p)) return false;

        if (p.getCoa() != null) p.getCoa().removeUser(p);

        p.setCoa(this);
        p.setRole(defaultRole);
        users.add(p);
        return true;
    }

//    ------------------------------------------------

    public boolean roleExists(String name) {
        for (CRole role : roles) {
            if (role.getName().equals(name)) return true;
        }

        return false;
    }


    public boolean createRole(String name, int power, CPERM... perms) {
        CRole newRole = new CRole(name, power, perms);

        if (roleExists(name)) return false;

        roles.add(newRole);
        return true;
    }

    public boolean removeRole(String name) {
        if (!roleExists(name)) return false;

        roles.removeIf(role -> role.getName().equals(name));

        return true;
    }

//    -------------------------------------------------

    private void addDefaultRoles() {
        CRole captain = new CRole("Captain", 1, CPERM.values());
        CRole member = new CRole("Member", 2, CPERM.HOME);

        roles.add(captain);
        roles.add(member);
        defaultRole = member;
    }
}
