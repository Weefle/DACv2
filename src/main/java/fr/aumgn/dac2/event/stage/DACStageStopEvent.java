package fr.aumgn.dac2.event.stage;

import org.bukkit.event.HandlerList;

import fr.aumgn.dac2.event.DACStageEvent;
import fr.aumgn.dac2.stage.Stage;

public class DACStageStopEvent extends DACStageEvent {

    private static final HandlerList handlers = new HandlerList();

    public DACStageStopEvent(Stage stage) {
        super(stage);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}