package fr.aumgn.dac2.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Color {

    public final String name;
    public final ChatColor chat;
    public final Material block;
    public final byte data;

    public Color(String name, ChatColor chat, Material material, byte data) {
        this.name = name;
        this.chat = chat;
        this.block = material;
        this.data = data;
    }
}
