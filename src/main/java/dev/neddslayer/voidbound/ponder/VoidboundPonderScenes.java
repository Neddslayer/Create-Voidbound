package dev.neddslayer.voidbound.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.registrar.VoidboundBlocks;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class VoidboundPonderScenes {
    public static void brassDrill(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("brass_drill", "Using the Brass Drill");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.world().showSection(util.select().layer(0), Direction.UP);

        scene.idle(5);
        scene.world().showSection(util.select().position(5, 1, 2), Direction.DOWN);

        // TODO blehh
        scene.world().showSection(util.select().position(2, 1, 2), Direction.UP);

        scene.overlay().showText(60)
                .placeNearTarget()
                .text("Bedrock is typically indestructible.")
                .pointAt(util.vector().of(2, 2, 2));

        scene.idle(80);

        scene.world().showSection(util.select().position(2, 2, 2), Direction.UP);
        scene.world().setKineticSpeed(util.select().position(2, 2, 2), 0);
        scene.world().setBlock(new BlockPos(2, 2, 2), VoidboundBlocks.BRASS_DRILL.getDefaultState(), false);
        scene.overlay().showText(60)
                .placeNearTarget()
                .text("A Brass Drill may only be placed downwards.")
                .pointAt(util.vector().of(2, 3, 2));

        scene.idle(60);

        scene.world().showSection(util.select().position(5, 2, 2), Direction.DOWN);
        scene.idle(10);

        for (int i = 3; i < 4; i++) {
            scene.world().showSection(util.select().layer(i), Direction.DOWN);
            scene.idle(10);
        }
        scene.world().setKineticSpeed(util.select().position(2, 2, 2), 32);

        scene.idle(20);

        scene.world().setKineticSpeed(util.select().layers(1, 3), 256);
        scene.world().setKineticSpeed(util.select().layer(0), 128);

        scene.overlay().showText(60)
                .placeNearTarget()
                .text("If given sufficient RPM...")
                .pointAt(util.vector().of(2, 3, 2));

        scene.idle(120);

        scene.world().setBlock(new BlockPos(2, 1, 2), VoidboundBlocks.DRILLED_BEDROCK.getDefaultState(), true);
        scene.overlay().showText(60)
                .placeNearTarget()
                .text("...the Brass Drill can break bedrock.")
                .pointAt(util.vector().of(2, 2, 2));

    }

}
