package org.simondoestuff.coalitions.Command.Infastructure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String handle();

    String description();

    String[] perms();
}
