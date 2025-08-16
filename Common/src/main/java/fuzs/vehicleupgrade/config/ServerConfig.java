package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    static final String HORSE_CATEGORY = "horse";
    static final String BOAT_CATEGORY = "boat";

    @Config(description = {
            "Prevent saddled mounts from strolling away from the position they are left at.",
            "The entities this applies to are defined by a corresponding tag."
    }, worldRestart = true)
    public boolean saddledMountsDoNotWander = true;
    @Config(description = "When the player enters any vehicle like a boat, set the player rotation to the vehicle instead of the other way around.")
    public boolean rotateVehicleWithPlayer = true;
    @Config(description = {
            "Remove entities from ridden vehicles by sneak + right-clicking with an empty hand.",
            "Does not apply to other players."
    })
    public boolean manuallyDismountPassengers = true;
    @Config(description = {
            "Open the inventory screen for mobs wearing equipment by sneak + right-clicking.",
            "The entities this applies to are defined by a corresponding tag."
    })
    public boolean openMobInventoryByInteracting = true;
    @Config(description = {
            "Allows ridden mobs to pass though leaves with a slight slowdown and some fancy particles.",
            "The blocks this applies to are defined by a corresponding tag."
    })
    public boolean mountsPassThroughLeaves = true;
    @Config(description = "Mobs that would otherwise throw off their rider when sinking in water are now able to swim while allowing the rider to stay.")
    public boolean mountsSwimInWater = true;
    @Config(description = {
            "When determining collisions for a vehicle, passengers are no longer ignored and are now properly taken into account.",
            "Due to Minecraft's quirky physics engine this behavior will still break when moving quickly or purposefully glitching into corners."
    })
    public boolean correctPassengerCollisions = true;
    @Config(category = HORSE_CATEGORY,
            description = "Using shears on a donkey, mule or llama allows for removing the equipped chest.")
    public boolean shearsRemoveChests = true;
    @Config(category = HORSE_CATEGORY,
            description = "Update horse behavior, like making grazing less disruptive to other actions, and preventing bucking while riding.",
            worldRestart = true)
    public boolean upgradeHorseAi = true;
    @Config(category = HORSE_CATEGORY,
            description = "Increase the horse head pitch offset while riding to improve visibility.")
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double lowerHorseHeadAmount = 0.5;
    @Config(category = BOAT_CATEGORY, description = {
            "Allow large mobs like horses, spiders and polar bears to enter boats by shrinking them to an acceptable size when in the boat.",
            "The entities this applies to are defined by a corresponding tag."
    })
    public boolean shrinkOverSizedBoatPassengers = true;
    @Config(category = BOAT_CATEGORY,
            description = "Allow boats to use jumping behaviour similar to horses for navigating on land.")
    public BoatJumping jumpInBoats = BoatJumping.ON_LAND;
    @Config(category = BOAT_CATEGORY,
            description = "Increase the boat step height just enough to be able to row up carpets, path blocks, soul sand, etc.")
    public boolean increaseBoatStepHeight = true;
    @Config(description = "Allow the player to reach further than normal for interacting with blocks and entities while riding any vehicle.")
    public boolean increasePassengerInteractionRange = true;
    @Config(description = {
            "Allow the player to sprint while riding any mount.",
            "The entities this applies to are defined by a corresponding tag."
    })
    public boolean sprintWhileRiding = true;
    @Config(description = {
            "When the player is riding any vehicle that vehicle will not be able to become a passenger of another vehicle.",
            "E.g. a pig and walking over an empty boat will prevent the pig from entering the boat as long as it is controlled by the player."
    })
    public boolean preventVehicleCollisionsWhileRiding = true;
}
