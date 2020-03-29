package org.simondoestuff.coalitions.Command.Infrastructure;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.simondoestuff.coalitions.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotatedCommandExecutor implements CommandExecutor {
    private static class CmdHandle {
        Method fun;
        Cmd cmd;
        Object inst;

        public CmdHandle(Method fun, Object inst, Cmd cmd) {
            this.fun = fun;
            this.cmd = cmd;
            this.inst = inst;
        }
    }

    private HashMap<String, CmdHandle> cmds = new HashMap<>();

    public AnnotatedCommandExecutor() {
        try {
            addCmd(getClass().getDeclaredMethod("helpCmd", CommandSender.class, String[].class), this);
        } catch (NoSuchMethodException e) {
            Plugin.println("§4A critical error has occurred in an Annotated Command Executor initialization.", Bukkit.getServer().getConsoleSender());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length == 0) {
            helpCmd(sender, new String[1]);
            return true;
        }

        String handle = args[0].toLowerCase();

        Collection<String> colArgs = Arrays.asList(args);
        Collection<String> newColArgs = new ArrayList<>();

        Iterator<String> iter = colArgs.iterator();
        iter.next();

        while (iter.hasNext()) {
            newColArgs.add(iter.next());
        }

        String[] newArgs = newColArgs.toArray(new String[0]);

        try {
            CmdHandle cmd = cmds.get(handle);

            if (cmd == null) {
                Plugin.println("§4Command not found.",sender);
                return true;
            }

            for (String perm : cmd.cmd.perms()) {
                if (perm.equals("")) continue;

                if (!sender.hasPermission(perm)) {
                    Plugin.println("§4You do not have access to this command.", sender);
                    return true;
                }
            }

            boolean accessible = cmd.fun.isAccessible();

            if (!accessible) cmd.fun.setAccessible(true);

            cmd.fun.invoke(cmd.inst, sender, newArgs);

            if (!accessible) cmd.fun.setAccessible(true);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Plugin.println("§4A critical error has occurred in the an Annotated Command Executor.", sender);
        }

        return true;
    }

    // Created by Simon Walker 3/26/2020; re-themed by Mason Bott 3/28/2020
    @Cmd(handle = "help", description = "§4D§ci§6s§ep§2l§aa§by§3s §7information for all commands.", args = "[pagenum]", perms = "")
    private void helpCmd(CommandSender sender, String[] args) {
        int page = 0;

         try {
            if (args.length == 1) {
                page = Integer.parseInt(args[0]);
                page--;
            }
        } catch (NumberFormatException ignored) { }

        if (cmds.size() == 0) {
            Plugin.println("No commands loaded. Is this in error? Please contact the developer.", sender);
            return;
        }

//        ------------------------- ready to start method

        int maxPages;

        if (cmds.size() % 10 == 0) maxPages = cmds.size()/10;
        else maxPages = (cmds.size() + 1) / 10;

//        ----------------------- initialized the max pages for formatting

        String helpBar = String.format("&3&m----------&3 [&a%d &3/ &a%d&3]", (page + 1), (maxPages + 1));
        helpBar = helpBar.replace('&', '§');

//        -----------------------

        Plugin.println(helpBar, false, sender);             // PRINT BAR

        CmdHandle[] vals = cmds.values().toArray(new CmdHandle[0]);
        StringBuilder helpCatalog = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = i + 10 * page;

            if (index >= vals.length || index < 0) break;

            Cmd annot = vals[index].cmd;

            if (!annot.showInHelp()) continue;

//    --------------- help entry formatting below:

            String handle = annot.handle();
            String annotArgs = annot.args();
            String desc = annot.description();

            String helpEntry;

            if (annotArgs.equals("")) {
                helpEntry = String.format("&b&o%s &8- &7%s\n", handle, desc);
            } else {
                helpEntry = String.format("&b&o%s &3%s\n     &7%s\n", handle, annotArgs, desc);
            }

//    --------------- help entry formatting above:

            helpCatalog.append(helpEntry.replace('&', '§'));
        }

        Plugin.println(helpCatalog.toString(), false, sender);
        Plugin.println(helpBar, false, sender);             // PRINT BAR
    }

    public boolean addCmd(Method method, Object inst) {
        if (!method.isAnnotationPresent(Cmd.class)) return false;

        Class<?>[] params = method.getParameterTypes();

        if (params.length != 2) return false;
        if (params[0] != CommandSender.class) return false;
        if (params[1] != String[].class) return false;

        // ---------------------------------------------- method verified

        Cmd annot = method.getAnnotation(Cmd.class);

        if (cmds.containsKey(annot.handle())) return false;

        cmds.put(annot.handle().toLowerCase(), new CmdHandle(method, inst, annot));

        return true;
    }

    public void addClass(Object inst) {
        for (Method method : inst.getClass().getDeclaredMethods()) {
            addCmd(method, inst);
        }
    }
}
