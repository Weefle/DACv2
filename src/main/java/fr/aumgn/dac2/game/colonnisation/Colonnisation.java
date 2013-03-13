package fr.aumgn.dac2.game.colonnisation;

import java.util.Arrays;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.localization.PluginMessages;
import fr.aumgn.dac2.DAC;
import fr.aumgn.dac2.arena.regions.Pool;
import fr.aumgn.dac2.game.AbstractGame;
import fr.aumgn.dac2.game.start.GameStartData;
import fr.aumgn.dac2.shape.column.Column;
import fr.aumgn.dac2.shape.column.ColumnPattern;
import fr.aumgn.dac2.shape.column.GlassyPattern;

public class Colonnisation extends AbstractGame<ColonnPlayer> {

    private boolean finished;
    private int setupTurns;

    public Colonnisation(DAC dac, GameStartData data) {
        super(dac, data, new ColonnPlayer.Factory());
        finished = false;
    }

    @Override
    public void start() {
        resetPoolOnStart();

        double size = arena.getPool().size2D();
        double setupColumns = size * dac.getConfig().getColonnisationRatio();
        setupTurns = (int) Math.ceil(setupColumns / party.size());

        send("colonnisation.start");
        send("colonnisation.playerslist");
        for (ColonnPlayer player : party.iterable()) {
            send("colonnisation.start.playerentry", player.getIndex() + 1,
                    player.getDisplayName());
        }
        send("colonnisation.setup.turns", setupTurns);
        nextTurn();
    }

    @Override
    public void onNewTurn() {
        setupTurns--;
        if (setupTurns == 0) {
            send("colonnisation.setup.finished");
            send("colonnisation.enjoy");
        }
    }

    private void nextTurn() {
        ColonnPlayer player = party.nextTurn();

        if (!player.isOnline()) {
            send("colonnisation.playerturn.notconnected",
                    player.getDisplayName());
            removePlayer(player);
            if (!finished) {
                nextTurn();
            }
            return;
        }

        send("colonnisation.playerturn", player.getDisplayName());
        tpBeforeJump(player);
        startTurnTimer();
    }

    @Override
    protected void turnTimedOut() {
        ColonnPlayer player = party.getCurrent();
        removePlayer(player);
        if (!finished) {
            nextTurn();
        }
    }

    private void removePlayer(ColonnPlayer player) {
        party.remove(player);
        spectators.add(player.getRef());
        if (party.size() < 2) {
            dac.getStages().stop(this);
        }
    }

    @Override
    public void stop(boolean force) {
        cancelTurnTimer();
        finished = true;
        resetPoolOnEnd();

        if (force) {
            send("colonnisation.stopped");
        } else {
            send("colonnisation.finished");
        }

        ColonnPlayer[] ranking = party.iterable().clone();
        Arrays.sort(ranking);

        int index = ranking.length - 1;
        send("colonnisation.winner", ranking[index].getDisplayName(),
                ranking[index].getScore());
        index--;
        for (; index >= 0; index--) {
            send("colonnisation.ranking", ranking.length - index,
                    ranking[index].getDisplayName(),
                    ranking[index].getScore());
        }
    }

    @Override
    public void onJumpSuccess(Player player) {
        ColonnPlayer gamePlayer = party.get(player);
        World world = arena.getWorld();
        Pool pool = arena.getPool();

        Column column = pool.getColumn(player);
        ColumnPattern pattern;
        if (setupTurns > 0) {
            pattern = dac.getConfig().getNeutralPattern();
            send("colonnisation.setup.success", gamePlayer.getDisplayName());
        } else {
            pattern = gamePlayer.getColumnPattern();
            boolean isADAC = column.isADAC(world);
            if (isADAC) {
                gamePlayer.incrementMultiplier();
                pattern = new GlassyPattern(pattern);
            }

            PoolVisitor visitor = new PoolVisitor(world, pool,
                    gamePlayer.getColor());
            int points = visitor.visit(column.getPos());
            points *= gamePlayer.getMultiplier();
            gamePlayer.addPoints(points);

            if (isADAC) {
                send("colonnisation.multiplier.increment",
                        gamePlayer.getMultiplier());
            }
            send("colonnisation.jump.success", gamePlayer.getDisplayName(),
                    points, gamePlayer.getScore());
        }

        column.set(world, pattern);
        tpAfterJumpSuccess(gamePlayer, column);
        if (pool.isFilled(world)) {
            dac.getStages().stop(this);
        } else {
            nextTurn();
        }
    }

    @Override
    public void onJumpFail(Player player) {
        ColonnPlayer gamePlayer = party.get(player);

        send("colonnisation.jump.fail", gamePlayer.getDisplayName());
        if (gamePlayer.getMultiplier() > 1) {
            gamePlayer.resetMultiplier();
            send("colonnisation.multiplier.reset");
        }

        tpAfterJumpFail(gamePlayer);
        nextTurn();
    }

    @Override
    public void onQuit(Player player) {
        ColonnPlayer gamePlayer = party.get(player);
        removePlayer(gamePlayer);
        send("colonnisation.player.quit", gamePlayer.getDisplayName(),
                gamePlayer.getScore());
        if (!finished) {
            nextTurn();
        }
    }

    @Override
    public void list(CommandSender sender) {
        PluginMessages messages = dac.getMessages();

        sender.sendMessage(messages.get("colonnisation.playerslist"));
        for (ColonnPlayer player : party.iterable()) {
            sender.sendMessage(messages.get("colonnisation.playerentry",
                    player.getIndex(), player.getDisplayName(),
                    player.getScore()));
        }
    }
}
