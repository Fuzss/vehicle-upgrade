package fuzs.vehicleupgrade.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Entity.class)
abstract class EntityMixin implements EntityAccess {

    @ModifyReturnValue(method = "collide", at = @At(value = "RETURN", ordinal = 0))
    private Vec3 collide$0(Vec3 stepUpVector, @Local(ordinal = 1) Vec3 collideVector) {
        if (this.isVehicle()) {
            double minY = this.getBoundingBox().minY;
            for (Entity entity : this.getPassengers()) {
                // fix weird boat behavior; since the player hitbox reaches beneath the boat will be floating on land
                AABB aABB = entity.getBoundingBox().setMinY(minY);
                List<VoxelShape> list = entity.level().getEntityCollisions(entity, aABB.expandTowards(stepUpVector));
                Vec3 vec3 = stepUpVector.lengthSqr() == 0.0 ? stepUpVector : Entity.collideBoundingBox(entity, stepUpVector, aABB, entity.level(), list);
                if (vec3.y() != stepUpVector.y()) {
                    return collideVector;
                } else {
                    if (vec3.x() != stepUpVector.x()) {
                        vec3 = new Vec3(collideVector.x(), vec3.y(), vec3.z());
                    }
                    if (vec3.z() != stepUpVector.z()) {
                        vec3 = new Vec3(vec3.x(), vec3.y(), collideVector.z());
                    }
                    stepUpVector = vec3;
                }
            }
        }

        return stepUpVector;
    }

    @ModifyReturnValue(method = "collide", at = @At(value = "RETURN"))
    private Vec3 collide$1(Vec3 vec3) {
        // this must be the second method in the class, it must run for both return statements at last
        if (this.isVehicle()) {
            double minY = this.getBoundingBox().minY;
            for (Entity entity : this.getPassengers()) {
                // fix unique boat behaviour; since the player hitbox reaches beneath, the boat will be floating on land
                AABB aABB = entity.getBoundingBox().setMinY(minY);
                List<VoxelShape> list = entity.level().getEntityCollisions(entity, aABB.expandTowards(vec3));
                vec3 = vec3.lengthSqr() == 0.0 ? vec3 : Entity.collideBoundingBox(entity, vec3, aABB, entity.level(), list);
            }
        }

        return vec3;
    }

    @Shadow
    public abstract boolean isVehicle();

    @Shadow
    public final List<Entity> getPassengers() {
        throw new RuntimeException();
    }
}
