package org.simondoestuff.coalitions.Command.Infrastructure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String handle();

    String args();

    String description();

    String[] perms();

    boolean showInHelp() default true;
}
