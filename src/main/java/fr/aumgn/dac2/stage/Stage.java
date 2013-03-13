package fr.aumgn.dac2.stage;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.dac2.arena.Arena;

public interface Stage {

    Arena getArena();

    void start();

    void stop(boolean force);

    Listener[] getListeners();

    boolean contains(Player player);

    void sendMessage(String message);

    Spectators getSpectators();

    void list(CommandSender sender);

    void onQuit(Player sender);
}
