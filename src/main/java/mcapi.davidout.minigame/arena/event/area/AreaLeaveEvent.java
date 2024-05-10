package mcapi.davidout.minigame.arena.event.area;


import mcapi.davidout.minigame.arena.ArenaManager;
import mcapi.davidout.minigame.arena.IArena;
import mcapi.davidout.minigame.arena.area.IArea;
import mcapi.davidout.minigame.arena.event.ArenaEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class AreaLeaveEvent extends ArenaEvent implements Listener {

    private final IArena arena;
    private final IArea area;
    private final Player player;

    private final Location from;
    private final Location to;

    public AreaLeaveEvent(IArena arena, IArea area, Player player, Location from, Location to) {
        this.arena = arena;
        this.area = area;
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public Player getPlayer() {
        return player;
    }


    public IArena getArena() {
        return arena;
    }

    public IArea getArea() {
        return area;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }

    @EventHandler
    public void onLeave(PlayerMoveEvent e) {
        World w = e.getPlayer().getWorld();

        if(ArenaManager.getInstance() == null) {
            return;
        }

        IArena cArena =  ArenaManager.getInstance().getArenaByWorld(w);
        if(cArena == null) {
            return;
        }

        IArea area = cArena.getAreas().stream().filter(a ->
                        a.locationInArea(e.getFrom()) && !a.locationInArea(e.getTo())
                ).findFirst()
                .orElse(null);

        if(area == null) {
            return;
        }


        AreaLeaveEvent leaveEvent = new AreaLeaveEvent(cArena, area, e.getPlayer(), e.getFrom(), e.getTo());
        Bukkit.getPluginManager().callEvent(leaveEvent);

        if(!leaveEvent.isCancelled()) {
            return;
        }

        e.setCancelled(true);
    }
}
