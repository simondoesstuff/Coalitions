package org.simondoestuff.coalitions.GroupInfrastructure;

import org.bukkit.Location;

import java.util.*;

public class Coalition {
    private static final HashMap<String, Coalition> coalitions = new HashMap<>();

    public static Coalition fromName(String name) {
        return coalitions.get(name);
    }

//    -----------------------------------------------

    private HashSet<CUser> users = new HashSet<>();
    private LinkedList<CRole> roles = new LinkedList<>();
    private CRole defaultRole;

    private String name;
    private boolean isDisbanded = false;

    private HashSet<Coalition> allies = new HashSet<>();
    private HashSet<Coalition> enemies = new HashSet<>();

    private Location home = null;

    public Coalition(String name) {
        if (coalitions.containsKey(name.toLowerCase())) throw new IllegalArgumentException("That name already exists.");

        this.name = name;
        coalitions.put(name.toLowerCase(), this);
        addDefaultRoles();
    }

    //    -----------------------------------------------

    public Location getHome() {
        if (home == null) return home;

        return home.clone();
    }

    public void setHome(Location loc) {
        home = loc;
    }

    public List<CUser> getUsers() {
        return new ArrayList<>(users);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void disband() {
        for (CUser user : users) {
            user.setCoa(null);
            user.setRole(null);

            coalitions.remove(this);
            isDisbanded = true;
        }
    }

    public boolean isDisbanded() {
        return isDisbanded;
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

    public List<Coalition> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public List<Coalition> getAllies() {
        return new ArrayList<>(allies);
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

    public CRole getRole(String name) {
        for (CRole role : roles) {
            if (role.getName().equals(name)) return role;
        }

        return null;
    }

    public boolean updateUserRole(CUser u, CRole role) {
        if (!roleExists(role.getName())) return false;
        if (!users.contains(u)) return false;

        u.setRole(role);
        return true;
    }

    public List<CRole> getRoles() {
        return new ArrayList<>(roles);
    }

    public List<CUser> usersInRole(CRole r) {
        ArrayList<CUser> members = new ArrayList<>();

        for (CUser user : users) {
            if (user.getRole().equals(r))
                members.add(user);
        }
        return members;
    }

    public int rolesAmount(){
        return roles.size();
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
