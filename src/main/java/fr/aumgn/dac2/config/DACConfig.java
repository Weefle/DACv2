package fr.aumgn.dac2.config;

import java.text.MessageFormat;
import java.util.Locale;

import org.bukkit.Material;

import fr.aumgn.bukkitutils.timer.TimerConfig;
import fr.aumgn.bukkitutils.util.Util;
import fr.aumgn.dac2.shape.column.UniformPattern;
import fr.aumgn.dac2.shape.column.ColumnPattern;
import fr.aumgn.dac2.utils.DACUtil;

public class DACConfig {

    private String language = Locale.getDefault().toString();

    private int initMessageRadius = -1;

    private PoolReset poolReset = PoolReset.START;

    private String spectatorsMessage = "{yellow}[{0}] {1}";
    private transient MessageFormat spectatorsMessageFormat = null;

    private String timerFormat = "%02d:%02d";
    private int timeOut = 60;

    private boolean autoGameMode = false;
    private boolean tpBeforeJump = true;
    private int tpAfterJumpSuccessDelay = 0;
    private int tpAfterJumpFailDelay = 3;

    private double colonnisationSetupPercent = 5;
    private Material neutralBlock = Material.OBSIDIAN;
    private byte neutralData = 0;

    public Locale getLocale() {
        return Util.parseLocale(language);
    }

    public boolean initMessageHasRadius() {
        return initMessageRadius >= 0;
    }

    public int getInitMessageRadius() {
        return initMessageRadius;
    }

    public boolean getResetOnStart() {
        return (poolReset.flag() & PoolReset.START.flag()) != 0;
    }

    public boolean getResetOnEnd() {
        return (poolReset.flag() & PoolReset.END.flag()) != 0;
    }

    public MessageFormat getSpectatorsMsg() {
        if (spectatorsMessageFormat == null) {
            spectatorsMessageFormat = new MessageFormat(
                    Util.parseColorsMarkup(spectatorsMessage),
                    getLocale());
        }

        return spectatorsMessageFormat;
    }

    public TimerConfig getTimerConfig() {
        return new TimerConfig(timeOut / 2, timeOut / 4, timerFormat, "");
    }

    public int getTimeOut() {
        return timeOut;
    }

    public boolean getAutoGameMode() {
        return autoGameMode;
    }

    public boolean getTpBeforeJump() {
        return tpBeforeJump;
    }

    public int getTpAfterJumpSuccessDelay() {
        return tpAfterJumpSuccessDelay * DACUtil.TICKS_PER_SECONDS;
    }

    public int getTpAfterJumpFailDelay() {
        return tpAfterJumpFailDelay * DACUtil.TICKS_PER_SECONDS;
    }

    public double getColonnisationRatio() {
        return ((double) colonnisationSetupPercent) / 100;
    }

    public ColumnPattern getNeutralPattern() {
        return new UniformPattern(neutralBlock, neutralData);
    }
}
