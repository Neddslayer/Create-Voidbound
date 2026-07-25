package dev.neddslayer.voidbound.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Level.class)
public interface LevelEntityAccessor {

    @Invoker("getEntities")
    LevelEntityGetter<? extends EntityAccess> voidbound$getEntities();

}
