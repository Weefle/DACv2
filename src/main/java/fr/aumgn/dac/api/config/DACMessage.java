package fr.aumgn.dac.api.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import fr.aumgn.dac.api.game.messages.GameMessageContent;
import fr.aumgn.dac.api.util.DACUtil;
import fr.aumgn.dac.plugin.DACConfigLoader;
import fr.aumgn.dac.plugin.DACConfigLoader.Error;

public enum DACMessage implements GameMessageContent {

    CmdDefineExists         ("command.define.exists"),
    CmdDefineSuccess        ("command.define.success"),
    CmdDeleteUnknown        ("command.delete.unknown"),
    CmdDeleteInGame         ("command.delete.in-game"),
    CmdDeleteSuccess        ("command.delete.success"),
    CmdArenasList           ("command.arenas.list"),
    CmdArenasArena          ("command.arenas.arena"),
    CmdFillUnknown          ("command.fill.unknown"),
    CmdFillInGame           ("command.fill.in-game"),
    CmdFillUnknownStrategy  ("command.fill.unknown-strategy"),
    CmdFillSuccess          ("command.fill.success"),
    CmdGotoNotInGame        ("command.goto.not-in-game"),
    CmdGotoSuccess          ("command.goto.success"),
    CmdJoinAlreadyPlaying   ("command.join.already-playing"),
    CmdJoinNotInStart       ("command.join.not-in-start"),
    CmdJoinInGame           ("command.join.in-game"),
    CmdJoinMaxReached       ("command.join.max-reached"),
    CmdKickNoPlayerFound    ("command.kick.no-player-found"),
    CmdKickSuccess          ("command.kick.success"),
    CmdLivesNotInGame       ("command.lives.not-in-game"),
    CmdLivesUnknownPlayer   ("command.lives.unknown-player"),
    CmdModesUnknown         ("command.modes.unknown"),
    CmdModesList            ("command.modes.list"),
    CmdModesMode            ("command.modes.mode"),
    CmdModesAddSuccess      ("command.modes.add-success"),
    CmdModesRemoveSuccess   ("command.modes.remove-success"),
    CmdOptionsUnknown       ("command.options.unknown"),
    CmdOptionsList          ("command.options.list"),
    CmdOptionsOption        ("command.options.option"),
    CmdOptionsAddSuccess    ("command.options.add-success"),
    CmdOptionsRemoveSuccess ("command.options.remove-success"),
    CmdQuitNotInGame        ("command.quit.not-in-game"),
    CmdReloadSuccess        ("command.reload.success"),
    CmdSelectUnknown        ("command.select.unknown"),
    CmdSelectSuccessPool    ("command.select.success-pool"),
    CmdSelectSuccessStart   ("command.select.success-start"),
    CmdSelectError          ("command.select.error"),
    CmdSetUnknown           ("command.set.unknown"),
    CmdSetWrongWorld        ("command.set.another-world"),
    CmdSetSuccessDiving     ("command.set.success-diving"),
    CmdSetSuccessPool       ("command.set.success-pool"),
    CmdSetSuccessStart      ("command.set.success-start"),
    CmdSetError             ("command.set.error"),
    CmdSetIncompleteRegion  ("command.set.incomplete-selection"),
    CmdStartNotInGame       ("command.start.not-in-game"),
    CmdStartUnknownMode     ("command.start.unknown-mode"),
    CmdStartUnavailableMode ("command.start.unavailable-mode"),
    CmdStartMinNotReached   ("command.start.min-not-reached"),
    CmdStopNoGameToStop     ("command.stop.no-game-to-stop"),
    CmdWatchNotInArena      ("command.watch.not-in-arena"),
    CmdWatchUnknown         ("command.watch.unknown"),
    CmdWatchNotInGame       ("command.watch.not-in-game"),
    CmdWatchAlreadyWatching ("command.watch.already-watching"),
    CmdWatchSuccess         ("command.watch.success"),
    CmdUnwatchSuccess       ("command.unwatch.success"),

    JoinNewGame             ("join.new-game"),
    JoinNewGame2            ("join.new-game2"),
    JoinCurrentPlayers      ("join.current-players"),
    JoinPlayerList          ("join.player-list"),
    JoinPlayerJoin          ("join.player-join"),
    JoinPlayerQuit          ("join.player-quit"),
    JoinStopped             ("join.stopped"),

    GameStart               ("game.start"),
    GamePlayers             ("game.players"),
    GamePlayerList          ("game.player-list"),
    GameEnjoy               ("game.enjoy"),
    GamePlayerTurn          ("game.player-turn"),
    GamePlayerTurn2         ("game.player-turn2"),
    GameTurnTimedOut        ("game.turn-timed-out"),
    GameConfirmation        ("game.confirmation"),
    GameConfirmation2       ("game.confirmation2"),
    GameDACConfirmation     ("game.dac-confirmation"),
    GameDACConfirmation2    ("game.dac-confirmation2"),
    GameDACConfirmation3    ("game.dac-confirmation3"),
    GameDAC                 ("game.dac"),
    GameDAC2                ("game.dac2"),
    GameLivesAfterDAC       ("game.lives-after-dac"),
    GameLivesAfterDAC2      ("game.lives-after-dac2"),
    GameJumpSuccess         ("game.jump-success"),
    GameJumpSuccess2        ("game.jump-success2"),
    GameJumpFail            ("game.jump-fail"),
    GameJumpFail2           ("game.jump-fail2"),
    GameConfirmationFail    ("game.confirmation-fail"),
    GameConfirmationFail2   ("game.confirmation-fail2"),
    GameLivesAfterFail      ("game.lives-after-fail"),
    GameLivesAfterFail2     ("game.lives-after-fail2"),
    GamePlayerQuit          ("game.player-quit"),
    GameMustConfirmate      ("game.must-confirmate"),
    GameMustConfirmate2     ("game.must-confirmate2"),
    GameFinished            ("game.finished"),
    GameWinner              ("game.winner"),
    GameRank                ("game.rank"),
    GameStopped             ("game.stopped"),
    GameDisplayLives        ("game.display-lives"),
    GameSuddenDeath         ("game.sudden-death"),
    GamePlayerEliminated    ("game.player-eliminated"),
    GamePlayerEliminated2   ("game.player-eliminated2"),

    StatsSuccess            ("stats.success"),
    StatsDAC                ("stats.dac"),
    StatsFail               ("stats.fail");

    private static final String FILENAME = "messages.yml";

    private static void load(Configuration config, Configuration defaults) {
        for (DACMessage message : DACMessage.values()) {
            String node = message.getNode();
            if (config.isString(node)) {
                message.set(DACUtil.parseColorsMarkup(config.getString(node)));
            } else if (defaults.isString(node)) {
                message.set(DACUtil.parseColorsMarkup(defaults.getString(node)));
            } else {
                message.set("");
            }
        }
    }

    public static void reload(Plugin plugin) {
        try {
            DACConfigLoader loader = new DACConfigLoader();
            FileConfiguration config = loader.load(plugin, FILENAME);
            FileConfiguration defaults = loader.loadDefaults(plugin, FILENAME);
            load(config, defaults);
        } catch (Error exc) {
            plugin.getLogger().warning(
                    "An error occured while loading + " + FILENAME);
            exc.printStackTrace();
        }
    }

    private String node;
    private String value;

    private DACMessage(String node) {
        this.node = node;
        this.value = "";
    }

    private String getNode() {
        return node;
    }

    private void set(String value) {
        this.value = value;
    }

    public String getContent() {
        return value;
    }

    public String getContent(Object... args) {
        return String.format(getContent(), args);
    }

    @Override
    public String toString() {
        return getContent();
    }

}
