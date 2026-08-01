package dev.neddslayer.voidbound.registrar;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.neddslayer.voidbound.Voidbound;
import dev.neddslayer.voidbound.block.DistilledVoidEssenceBlock;
import dev.neddslayer.voidbound.block.RawVoidEssenceBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class VoidboundFluids {
    private static final CreateRegistrate REGISTRATE = Voidbound.registrate();

    public static final FluidEntry<BaseFlowingFluid.Flowing> DISTILLED_VOID_ESSENCE = REGISTRATE
            .fluid("distilled_void_essence", Voidbound.path("block/distilled_void_fluid"), Voidbound.path("block/distilled_void_fluid"),
                    (p, s, f) -> new NoRenderFluidType(p, s, f) {
                        @Override
                        protected int getTintColor(FluidStack stack) {
                            return 0xffffffff;
                        }

                        @Override
                        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                            return 0xffffffff;
                        }

                        protected Vector3f getCustomFogColor() {
                            return new Vector3f(0);
                        }

                        protected float getFogDistanceModifier() {
                            return 0.05f;
                        }
                    })
            .lang("Distilled Void Essence")
            .properties(b -> b.canPushEntity(false).canDrown(false).viscosity(0)
                    .density(1))
            .fluidProperties(p -> p.levelDecreasePerBlock(1)
                    .tickRate(2)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .block(DistilledVoidEssenceBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_WHITE))
            .build()
            .bucket()
            .onRegister(VoidboundFluids::registerFluidDispenseBehavior)
            .build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> RAW_VOID_ESSENCE = REGISTRATE
            .fluid("raw_void_essence", Voidbound.path("block/raw_void_fluid"), Voidbound.path("block/raw_void_fluid"),
                    (p, s, f) -> new NoRenderFluidType(p, s, f) {
                        @Override
                        protected int getTintColor(FluidStack stack) {
                            return 0xffffffff;
                        }

                        @Override
                        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                            return 0xffffffff;
                        }

                        protected Vector3f getCustomFogColor() {
                            return new Vector3f(0);
                        }

                        protected float getFogDistanceModifier() {
                            return 0.05f;
                        }
                    })
            .lang("Raw Void Essence")
            .properties(b -> b.canPushEntity(false).canDrown(false).viscosity(0)
                    .density(2))
            .fluidProperties(p -> p.levelDecreasePerBlock(1)
                    .tickRate(4)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .block(RawVoidEssenceBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_PURPLE))
            .build()
            .bucket()
            .onRegister(VoidboundFluids::registerFluidDispenseBehavior)
            .build()
            .register();

    public static void register() {
    }

    public static Item rawVoidEssenceBucket() {
        return REGISTRATE.get("raw_void_essence_bucket", Registries.ITEM).get();
    }
    public static Item distilledVoidEssenceBucket() {
        return REGISTRATE.get("distilled_void_essence_bucket", Registries.ITEM).get();
    }

    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior(){
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) pStack.getItem();
            BlockPos pos = pSource.pos().relative(pSource.state().getValue(DispenserBlock.FACING));
            Level level = pSource.level();
            if (dispensibleContainerItem.emptyContents(null, level, pos, null, pStack)) {
                return new ItemStack(Items.BUCKET);
            }
            return DEFAULT.dispense(pSource, pStack);
        }
    };

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }

    private abstract static class NoRenderFluidType extends FluidType {
        private final ResourceLocation still, flow;
        public NoRenderFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties);
            this.still = stillTexture;
            this.flow = flowingTexture;
        }

        protected abstract int getTintColor(FluidStack stack);

        protected abstract int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);

        protected @Nullable Vector3f getCustomFogColor() {
            return null;
        }

        protected float getFogDistanceModifier() {
            return 1f;
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return still;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return flow;
                }

                @Override
                public int getTintColor(FluidStack stack) {
                    return NoRenderFluidType.this.getTintColor(stack);
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return NoRenderFluidType.this.getTintColor(state, getter, pos);
                }

                @Override
                public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                               int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    Vector3f customFogColor = NoRenderFluidType.this.getCustomFogColor();
                    return customFogColor == null ? fluidFogColor : customFogColor;
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                            float nearDistance, float farDistance, FogShape shape) {
                    float modifier = NoRenderFluidType.this.getFogDistanceModifier();
                    float baseWaterFog = 96.0f;
                    if (modifier != 1f) {
                        RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                        RenderSystem.setShaderFogStart(-8);
                        RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                    }
                }

                public boolean renderFluid(FluidState fluidState, BlockAndTintGetter getter, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState) {
                    // this makes it be... not rendered... for some reason...
                    // lets it render in the block entity step instead of the liquid tessellation step
                    return true;
                }
            });
        }


    }

}
