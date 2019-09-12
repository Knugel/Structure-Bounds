package knugel.structurebounds.command;

import knugel.structurebounds.client.Boundary;
import knugel.structurebounds.client.BoundaryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BoundaryCommand implements CommandExecutor {
    private final BoundaryManager manager;

    public BoundaryCommand(BoundaryManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            UUID uuid = ((Player) sender).getUniqueId();

            if(!manager.contains(uuid))
                manager.add(uuid);
            Boundary boundary = manager.get(uuid);
            boundary.toggle();

            sender.sendMessage("Structure Boundaries are now " + (boundary.isVisible() ? "visible" : "hidden"));
            return true;
        }
        return false;
    }
}
