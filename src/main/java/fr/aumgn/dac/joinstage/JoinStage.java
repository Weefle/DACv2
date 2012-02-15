package fr.aumgn.dac.joinstage;

import org.bukkit.entity.Player;

import fr.aumgn.dac.game.mode.GameMode;
import fr.aumgn.dac.stage.Stage;

public interface JoinStage<T extends JoinStagePlayer> extends Stage<T> {

	void addPlayer(Player player, String[] args);
	
	boolean isMinReached(GameMode<?> mode);
	
	boolean isMaxReached();
	
}
