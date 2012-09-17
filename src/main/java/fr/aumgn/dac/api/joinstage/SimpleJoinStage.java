package fr.aumgn.dac.api.joinstage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;

import fr.aumgn.dac.api.DAC;
import fr.aumgn.dac.api.arena.Arena;
import fr.aumgn.dac.api.config.DACColor;
import fr.aumgn.dac.api.config.DACColors;
import fr.aumgn.dac.api.config.DACMessage;
import fr.aumgn.dac.api.event.joinstage.DACJoinStageJoinEvent;
import fr.aumgn.dac.api.event.stage.DACStagePlayerQuitEvent;
import fr.aumgn.dac.api.event.stage.DACStageStartEvent;
import fr.aumgn.dac.api.event.stage.DACStageStopEvent;
import fr.aumgn.dac.api.game.mode.DACGameMode;
import fr.aumgn.dac.api.game.mode.GameMode;
import fr.aumgn.dac.api.stage.SimpleStage;
import fr.aumgn.dac.api.stage.StagePlayer;
import fr.aumgn.dac.api.stage.StageQuitReason;
import fr.aumgn.dac.api.stage.StageStopReason;
import fr.aumgn.dac.api.util.DACUtil;

public class SimpleJoinStage extends SimpleStage implements JoinStage {

    private DACColors colors;
    private Set<DACColor> colorsMap;
    private List<SimpleJoinStagePlayer> players;

    public SimpleJoinStage(Arena arena) {
        super(arena);
        colors = DAC.getColors();
        colorsMap = new HashSet<DACColor>();
        players = new ArrayList<SimpleJoinStagePlayer>();
        DACUtil.broadcast(DACMessage.JoinNewGame.getContent(arena.getName()), "dac.game.watch");
        DACUtil.broadcast(DACMessage.JoinNewGame2.getContent(), "dac.game.watch");
        DAC.callEvent(new DACStageStartEvent(this));
    }

    private boolean isColorAvailable(String name) {
        DACColor color = colors.get(name);
        if (color == null) {
            return false;
        } else {
            return isColorAvailable(color);
        }
    }

    private boolean isColorAvailable(DACColor color) {
        return !colorsMap.contains(color);
    }

    private DACColor getFirstColorAvailable() {
        for (Entry<String, DACColor> color : colors.colors()) {
            if (!colorsMap.contains(color.getValue())) {
                return color.getValue();
            }
        }
        // Should never be reached
        return colors.first();
    }

    private void addPlayer(Player player, DACColor color) {
        DACJoinStageJoinEvent event = new DACJoinStageJoinEvent(this, player, color, player.getLocation());
        DAC.callEvent(event);

        if (!event.isCancelled()) {
            SimpleJoinStagePlayer dacPlayer = new SimpleJoinStagePlayer(this, player, event.getColor(), event.getStartLocation());
            if (players.size() > 0) {
                dacPlayer.send(DACMessage.JoinCurrentPlayers);
                for (StagePlayer currentPlayer : players) {
                    dacPlayer.send(DACMessage.JoinPlayerList.getContent(currentPlayer.getDisplayName()));
                }
            }
            send(DACMessage.JoinPlayerJoin.getContent(dacPlayer.getDisplayName()));
            players.add(dacPlayer);
            DAC.getPlayerManager().register(dacPlayer);
            colorsMap.add(event.getColor());
        }
    }

    @Override
    public void addPlayer(Player player, List<String> colorsName) {
        int i = 0;
        DACColor color;
        while (i < colorsName.size() && !isColorAvailable(colorsName.get(i))) {
            i++;
        }

        if (i == colorsName.size()) {
            color = getFirstColorAvailable();
        } else {
            color = colors.get(colorsName.get(i));
        }
        addPlayer(player, color);
    }

    @Override
    public void removePlayer(StagePlayer player, StageQuitReason reason) {
        DAC.callEvent(new DACStagePlayerQuitEvent(this, player, reason));
        send(DACMessage.JoinPlayerQuit.getContent(player.getDisplayName()));
        players.remove(player);
        DAC.getPlayerManager().unregister(player);
        colorsMap.remove(player.getColor());
        if (players.size() == 0) {
            stop(StageStopReason.NotEnoughPlayer);
        }
    }

    @Override
    public List<StagePlayer> getPlayers() {
        return new ArrayList<StagePlayer>(players);
    }

    @Override
    public void stop(StageStopReason reason) {
        DAC.callEvent(new DACStageStopEvent(this, reason));
        DAC.getStageManager().unregister(this);
        if (reason != StageStopReason.ChangeStage) {
            DACUtil.broadcast(DACMessage.JoinStopped.getContent(), "dac.game.watch");
        }
    }

    @Override
    public boolean isMinReached(GameMode mode) {
        int minimum = mode.getClass().getAnnotation(DACGameMode.class).minPlayers();
        return players.size() >= minimum;
    }

    @Override
    public boolean isMaxReached() {
        return (players.size() >= DAC.getColors().size());
    }

}
