package org.simondoestuff.coalitions.Command.Infastructure;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.junit.Test;
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
            Plugin.println("§4A critical error has occurred in an Annotated Command Executor.", Bukkit.getServer().getConsoleSender());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length == 0) {
            helpCmd(sender, new String[1]);
            return true;
        }

        String handle = args[0];

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
                Plugin.println("§4Command not found.");
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
            Plugin.println("§4A critical error has occurred in the an Annotated Command Executor.", sender);
        }

        return true;
    }

    @Cmd(handle = "help", description = "[pagenum]\nDisplays information for all commands.", perms = "")
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

        int maxPages;

        if (cmds.size() % 10 == 0) maxPages = cmds.size()/10;
        else maxPages = (cmds.size() + 1) / 10;

        String helpBar = "&3&s---------- ".replace('&', '§') + (page + 1) + "/" + (maxPages + 1);
        Plugin.println(helpBar, false);
        CmdHandle[] vals = cmds.values().toArray(new CmdHandle[0]);

        for (int i = 0; i < 10; i++) {
            int index = i + 10 * page;

            if (index >= vals.length || index < 0) break;

            CmdHandle info = vals[index];
            Plugin.println(String.format("&b&n%s&8 - &7%s", info.cmd.handle(), info.cmd.description()).replace('&', '§'), false, sender);
        }

        Plugin.println(helpBar, false);
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

        cmds.put(annot.handle(), new CmdHandle(method, inst, annot));

        return true;
    }

    public void addClass(Object inst) {
        for (Method method : inst.getClass().getDeclaredMethods()) {
            addCmd(method, inst);
        }
    }
}
