package net.racingwither.magnesiumchloride;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.DispenseFluidContainer;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.racingwither.magnesiumchloride.block.FluidCanisterBlock;
import net.racingwither.magnesiumchloride.block.HClBurnerBlock;
import net.racingwither.magnesiumchloride.block.HydrochloricAcidBlock;
import net.racingwither.magnesiumchloride.block.entity.FluidCanisterBlockEntity;
import net.racingwither.magnesiumchloride.block.entity.HClBurnerBlockEntity;
import net.racingwither.magnesiumchloride.fluid.BaseFluidType;
import net.racingwither.magnesiumchloride.fluid.MCFluidTypes;
import net.racingwither.magnesiumchloride.fluid.MCFluids;
import net.racingwither.magnesiumchloride.item.FluidCanisterItem;
import net.racingwither.magnesiumchloride.item.FluidTankItem;
import org.joml.Vector3f;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MagnesiumChlorideMod.MOD_ID)
public class MagnesiumChlorideMod {
    public static final String MOD_ID = "magnesium_chloride";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredBlock<Block> MAGNESIUM_ORE = BLOCKS.register("magnesium_ore", () -> new Block(BlockBehaviour.Properties.of()
            .destroyTime(1.5f)
            .explosionResistance(6.0f)
            .sound(SoundType.STONE)
    ));

    public static final DeferredBlock<FluidCanisterBlock> FLUID_CANISTER = BLOCKS.register("fluid_canister",
            () -> new FluidCanisterBlock(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<HClBurnerBlock> HCL_BURNER = BLOCKS.register("hcl_burner", () -> new HClBurnerBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<LiquidBlock> HYDROCHLORIC_ACID = BLOCKS.register("hydrochloric_acid", () -> new HydrochloricAcidBlock(MCFluids.SOURCE_HYDROCHLORIC_ACID.get(),
            BlockBehaviour.Properties.of().replaceable().noCollission().strength(100.0f).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)));

    public static final Supplier<BlockItem> MAGNESIUM_ORE_ITEM = ITEMS.registerSimpleBlockItem(MAGNESIUM_ORE);
    public static final Supplier<BlockItem> HCL_BURNER_ITEM = ITEMS.registerSimpleBlockItem(HCL_BURNER);

    public static final DeferredItem<Item> MAGNESIUM_CHLORIDE = ITEMS.registerSimpleItem("magnesium_chloride", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static final DeferredItem<Item> RAW_MAGNESIUM = ITEMS.registerSimpleItem("raw_magnesium");
    public static final DeferredItem<Item> MAGNESIUM = ITEMS.registerSimpleItem("magnesium");
    public static final DeferredItem<BucketItem> ACID_BUCKET = ITEMS.register("acid_bucket",
            () -> new BucketItem(MCFluids.SOURCE_HYDROCHLORIC_ACID.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
    public static final DeferredItem<FluidCanisterItem> FLUID_CANISTER_ITEM = ITEMS.register("fluid_canister",
            () -> new FluidCanisterItem(FLUID_CANISTER.get(), new Item.Properties()));

    public static final Supplier<BlockEntityType<HClBurnerBlockEntity>> HCL_BURNER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "hcl_burner_block_entity", () -> new BlockEntityType<>(HClBurnerBlockEntity::new,
                    Set.of(HCL_BURNER.get()), null));
    public static final Supplier<BlockEntityType<FluidCanisterBlockEntity>> FLUID_CANISTER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "fluid_canister_block_entity", () -> BlockEntityType.Builder.of(FluidCanisterBlockEntity::new,
                    FLUID_CANISTER.get()).build(null));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magnesium_chloride"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> MAGNESIUM_CHLORIDE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(MAGNESIUM_CHLORIDE.get());
                output.accept(MAGNESIUM_ORE_ITEM.get());
                output.accept(RAW_MAGNESIUM.get());
                output.accept(MAGNESIUM.get());
                output.accept(ACID_BUCKET.get());
                output.accept(HCL_BURNER_ITEM.get());
                output.accept(FLUID_CANISTER_ITEM.get());
            }).build());

    public MagnesiumChlorideMod(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MCFluids.register(modEventBus);
        MCFluidTypes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class MagnesiumChlorideClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(MCFluids.SOURCE_HYDROCHLORIC_ACID.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(MCFluids.FLOWING_HYDROCHLORIC_ACID.get(), RenderType.translucent());
        }

        @SubscribeEvent
        public static void registerFluidExtensions(RegisterClientExtensionsEvent event) {
            BaseFluidType hydrochloric_acid = MCFluidTypes.HYDROCHLORIC_ACID_TYPE.get();
            event.registerFluidType(new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return hydrochloric_acid.getStillTexture();
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return hydrochloric_acid.getFlowingTexture();
                }

                @Override
                public int getTintColor() {
                    return hydrochloric_acid.getTintColor();
                }

                @Override
                public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    return hydrochloric_acid.getFogColor();
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                    RenderSystem.setShaderFogStart(1f);
                    RenderSystem.setShaderFogEnd(6f);
                }
            }, hydrochloric_acid);
        }
    }

    @EventBusSubscriber(modid = MOD_ID)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            MCFluids.registerFluidInteractions();
            DispenserBlock.registerBehavior(ACID_BUCKET.asItem(), DispenseFluidContainer.getInstance());
        }

        @SubscribeEvent
        public static void registerFluidHandlers(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, HCL_BURNER_BLOCK_ENTITY.get(),
                    (blockEntity, side) ->  blockEntity.getHandler());
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FLUID_CANISTER_BLOCK_ENTITY.get(),
                    (blockEntity, side) -> blockEntity.getTank());
            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> ((FluidCanisterItem) itemStack.getItem()).getOrCreateFluidHandler(itemStack),
                    FLUID_CANISTER_ITEM);
        }
    }
}
