package fuzs.vehicleupgrade.world.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;

/**
 * Turns the eating behavior into a proper goal, so it cannot interrupt other goals like
 * {@link net.minecraft.world.entity.ai.goal.TemptGoal}.
 */
public class HorseEatingGoal extends Goal {
    private final AbstractHorse horse;
    private int eatingCounter;

    public HorseEatingGoal(AbstractHorse abstractHorse) {
        this.horse = abstractHorse;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public void tick() {
        this.eatingCounter++;
    }

    @Override
    public void stop() {
        this.eatingCounter = 0;
        this.horse.setEating(false);
    }

    @Override
    public void start() {
        this.horse.setEating(true);
    }

    @Override
    public boolean canContinueToUse() {
        return this.horse.isEating() && this.eatingCounter <= this.adjustedTickDelay(50);
    }

    @Override
    public boolean canUse() {
        if (this.horse.isEating()) {
            return true;
        } else if (this.horse.canEatGrass()) {
            // this seems to be a good value to get similar success rates compared to the eating behavior running every tick
            return !this.horse.isVehicle() && this.horse.getRandom().nextInt(90) == 0 && this.horse.level()
                    .getBlockState(this.horse.blockPosition().below())
                    .is(Blocks.GRASS_BLOCK);
        } else {
            return false;
        }
    }
}
