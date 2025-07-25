package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class CommonConfig implements ConfigCore {
    @Config(
            description = "Prevent a slower block breaking speed caused by the player not standing on the ground while riding any vehicle.",
            gameRestart = true
    )
    public boolean removePassengerMiningSpeedMalus = true;
    @Config(
            category = ServerConfig.HORSE_CATEGORY,
            description = "Set the step height for all horses above one block to be able to run up from path blocks, farmland, etc.",
            gameRestart = true
    )
    public boolean increaseHorseStepHeight = true;
}
