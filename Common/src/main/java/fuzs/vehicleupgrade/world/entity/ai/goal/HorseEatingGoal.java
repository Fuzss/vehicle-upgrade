package fuzs.vehicleupgrade.world.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;

public class HorseEatingGoal extends Goal {
    private final AbstractHorse horse;
    private int eatingCounter;

    public HorseEatingGoal(AbstractHorse horse) {
        this.horse = horse;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
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
        return this.horse.isEating() && this.eatingCounter <= 50;
//        return this.eatingCounter <= 50 && !this.horse.isVehicle() && this.horse.level().getBlockState(this.horse.blockPosition().below()).is(Blocks.GRASS_BLOCK);
    }

    @Override
    public boolean canUse() {

        if (this.horse.isEating()) return true;

        if (this.horse.canEatGrass()) {
            if (!this.horse.isVehicle() && this.horse.getRandom().nextInt(300) == 0 && this.horse.level().getBlockState(
                    this.horse.blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
                return true;
            }
        }

        return false;
    }
}
