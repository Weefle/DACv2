package fr.aumgn.dac.api.event.game;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.aumgn.dac.api.game.Game;
import fr.aumgn.dac.api.stage.StagePlayer;

public class DACGameFailEvent extends DACGamePlayerEvent implements Cancellable {

	private static final long serialVersionUID = 1L;
	private static final HandlerList handlers = new HandlerList();

	private boolean isCancelled = false;
	private boolean cancelDeath = true; 
	
	public DACGameFailEvent(Game<?> game, StagePlayer player) {
		super("DACGameFailEvent", game, player);
	}
	
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public boolean cancelDeath() {
		return cancelDeath;
	}

	public void setCancelDeath(boolean cancelDeath) {
		this.cancelDeath = cancelDeath;
	}

}