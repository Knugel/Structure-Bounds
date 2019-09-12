package knugel.structurebounds.client;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerHandler implements Listener {
    private BoundaryManager manager;

    public PlayerHandler(BoundaryManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerConnectionCloseEvent event) {
        manager.remove(event.getPlayerUniqueId());
    }
}
