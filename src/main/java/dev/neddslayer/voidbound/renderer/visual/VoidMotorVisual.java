package dev.neddslayer.voidbound.renderer.visual;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.neddslayer.voidbound.blockentity.VoidMotorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class VoidMotorVisual extends KineticBlockEntityVisual<VoidMotorBlockEntity> {
    protected final RotatingInstance shaft;
    final Direction direction;

    public VoidMotorVisual(VisualizationContext context, VoidMotorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockState.getValue(FACING);
        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();

        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, direction)
                .setChanged();
    }

    @Override
    public void update(float partialTick) {
        shaft.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos behind = pos.relative(direction);
        relight(behind, shaft);
    }

    @Override
    protected void _delete() {
        shaft.delete();
    }
}
