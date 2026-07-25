package dev.neddslayer.voidbound.renderer.visual;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.neddslayer.voidbound.blockentity.BrassDrillBlockEntity;
import dev.neddslayer.voidbound.registrar.VoidboundPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BrassDrillVisual extends KineticBlockEntityVisual<BrassDrillBlockEntity> {

    protected final RotatingInstance shaft;
    protected final RotatingInstance drill;

    private final RandomSource random = RandomSource.create();

    public BrassDrillVisual(VisualizationContext context, BrassDrillBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance()
                .rotateToFace(Direction.SOUTH, Direction.UP)
                .setup(blockEntity)
                .setPosition(getVisualPosition());
        drill = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(VoidboundPartialModels.BRASS_DRILL))
                .createInstance()
                .rotateToFace(Direction.UP)
                .setup(blockEntity)
                .setPosition(getVisualPosition().getX(), getVisualPosition().getY() - Mth.abs(blockEntity.getSpeed() / 256f) * 0.2f, getVisualPosition().getZ());

        shaft.setChanged();
        drill.setChanged();
    }

    @Override
    public void update(float partialTick) {
        shaft.setup(blockEntity)
                .setChanged();
        drill.setup(blockEntity)
                .setPosition(getVisualPosition().getX() + (float)random.nextGaussian() * blockEntity.getBedrockDestroyProgress() * 0.05f, getVisualPosition().getY() - Mth.abs(blockEntity.getSpeed() / 256f) * 0.2f + random.nextFloat() * blockEntity.getBedrockDestroyProgress() * 0.2f, getVisualPosition().getZ() + (float)random.nextGaussian() * blockEntity.getBedrockDestroyProgress() * 0.05f)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(pos.above(), shaft);
        relight(pos, drill);
    };

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(drill);
    }

    @Override
    protected void _delete() {
        shaft.delete();
        drill.delete();
    }
}
