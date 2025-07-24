package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(
            description = "Prevent saddled mounts from strolling away from the position they are left at.",
            worldRestart = true
    )
    public boolean saddledMountsDoNotWander = true;
    @Config(description = "When the player enters any vehicle like set the player rotation to the vehicle instead of the other way around.")
    public boolean rotateVehicleWithPlayer = true;
    @Config(
            description = {
                    "Remove entities from ridden vehicles by sneak + right-clicking.",
                    "Does not apply to other players."
            }
    )
    public boolean manuallyDismountPassengers = true;
    @Config(description = "Open the inventory screen for mobs that have it by sneak + right-clicking.")
    public boolean openMobInventoryByInteracting = true;
    @Config(description = "Allows ridden mobs to pass though leaves with a slight slowdown.")
    public boolean mountsPassThroughLeaves = true;
    @Config(description = "Mobs that would otherwise throw off their rider when sinking in water are now able to swim while always keeping the rider.")
    public boolean mountsSwimInWater = true;
    @Config(
            description = {
                    "When determining collisions for a vehicle; passengers are no longer ignored and are now properly taken into account.",
                    "Due to Minecraft's quirky physics engine this behavior will still break when moving quickly or glitching into corners."
            }
    )
    public boolean correctPassengerCollisions = true;
    @Config(description = "Using shears on a donkey, mule or llama allows for removing the equipped chest.")
    public boolean shearsRemoveChests = true;
}
