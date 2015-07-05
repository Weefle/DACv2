package fr.aumgn.dac2.exceptions;

import com.sk89q.worldedit.regions.Region;
import fr.aumgn.dac2.DAC;

/**
 * Thrown when trying to assigns a shape based on a WorldEdit region
 * which is not currently supported.
 */
public class WERegionNotSupported extends DACException {

    private static final long serialVersionUID = 1498774297971531082L;

    public WERegionNotSupported(DAC dac,
                                Class<? extends Region> clazz) {
        super(dac.getMessages().get(
                "worldedit.region.unsupported", clazz.getSimpleName()));
    }
}
