package fr.aumgn.dac.plugin.command;

import org.bukkit.entity.Player;

import fr.aumgn.dac.api.DAC;
import fr.aumgn.dac.api.arena.Arena;
import fr.aumgn.dac.api.config.DACMessage;
import fr.aumgn.dac.api.game.Game;
import fr.aumgn.dac.api.stage.Stage;
import fr.aumgn.utils.command.PlayerCommandContext;
import fr.aumgn.utils.command.PlayerCommandExecutor;

public class WatchCommand extends PlayerCommandExecutor {

    @Override
    public boolean checkUsage(String[] args) {
        return args.length < 2;
    }

    @Override
    public void onPlayerCommand(PlayerCommandContext context, String[] args) {
        if (args.length == 0) {
            Arena arena = DAC.getArenas().get(context.getPlayer());
            if (arena == null) {
                error(DACMessage.CmdWatchNotInArena);
            }
            processArena(context, arena);
        } else {
            for (String arg : args) {
                Arena arena = DAC.getArenas().get(arg);
                if (arena == null) {
                    context.error(DACMessage.CmdWatchUnknown.getContent(arg));
                } else {
                    processArena(context, arena);
                }
            }
        }

    }

    private void processArena(PlayerCommandContext context, Arena arena) {
        Stage stage = DAC.getStageManager().get(arena);
        if (!(stage instanceof Game)) {
            context.error(DACMessage.CmdWatchNotInGame.getContent(arena.getName()));
            return;
        }
        Player player = context.getPlayer();
        Game game = (Game) stage;
        if (game.canWatch(player)) {
            game.addSpectator(player);
            context.success(DACMessage.CmdWatchSuccess.getContent(arena.getName()));
        } else {
            context.send(DACMessage.CmdWatchAlreadyWatching.getContent(arena.getName()));
        }
    }

}
