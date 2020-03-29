package org.simondoestuff.coalitions.GroupInfastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CRole {
    private String name;
    private int power;
    private ArrayList<CPERM> perms = new ArrayList<>();

    public CRole(String name, int power, CPERM... perms) {
        this.name = name;
        this.power = power;
        this.perms.addAll(Arrays.asList(perms));
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public List<CPERM> getPerms() {
        List<CPERM> ret = new ArrayList<>();
        Collections.copy(ret, perms);

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof CRole)) return false;

        CRole obj2 = (CRole) obj;

        if (!name.equals(obj2.name)) return false;
        if (power != ((CRole) obj).power) return false;
        return getPerms().equals(((CRole) obj).perms);
    }


//    -----------  these are all package private, used in Coalitions.java

    void setName(String name) {
        this.name = name;
    }

    void setPower(int power) {
        this.power = power;
    }

    void setPerms(ArrayList<CPERM> perms) {
        this.perms = perms;
    }
}
