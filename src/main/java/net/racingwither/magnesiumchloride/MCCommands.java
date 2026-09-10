package net.racingwither.magnesiumchloride;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.racingwither.magnesiumchloride.block.entity.HClBurnerFluidHandler;
import net.racingwither.magnesiumchloride.component.FluidStorageComponent;
import net.racingwither.magnesiumchloride.fluid.MCFluids;
import net.racingwither.magnesiumchloride.item.FluidTankItem;

@EventBusSubscriber
public class MCCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("magnesiumchloride")
                        .then(
                                Commands.literal("fluid")
                                        .then(
                                                Commands.argument("pos", BlockPosArgument.blockPos())
                                                        .then(
                                                                Commands.argument("fluid", IntegerArgumentType.integer(0))
                                                                        .then(
                                                                                Commands.argument("amount", IntegerArgumentType.integer(0, 8000))
                                                                                        .executes(context -> {
                                                                                            BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
                                                                                            ServerLevel level = context.getSource().getLevel();
                                                                                            BlockEntity be = level.getBlockEntity(pos);
                                                                                            IFluidHandler cap = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
                                                                                            if (cap != null) {
                                                                                                int tank = IntegerArgumentType.getInteger(context, "fluid");
                                                                                                if (tank <= cap.getTanks() && cap instanceof HClBurnerFluidHandler hand) {
                                                                                                    hand.TANKS.get(tank).fill(new FluidStack(
                                                                                                            Fluids.WATER,
                                                                                                            IntegerArgumentType.getInteger(context, "amount")),
                                                                                                            IFluidHandler.FluidAction.EXECUTE);}}
                                                                                            return 1;
                                                                                        })
                                                                        )
                                                        )
                                        )
                        ).then(
                                Commands.literal("canister")
                                        .then(
                                                Commands.literal("chlorine")
                                                        .executes(context -> {
                                                                    CommandSourceStack source = context.getSource();
                                                                    Player player = source.getPlayer();
                                                                    ItemStack stack = new ItemStack(MagnesiumChlorideMod.FLUID_CANISTER);
                                                                    stack.update(MagnesiumChlorideMod.FLUID_STORAGE_COMPONENT, FluidStorageComponent.FluidStorageRecord.CANISTER_DEFAULT, record -> record.setValue(new FluidStack(MCFluids.SOURCE_CHLORINE_GAS, 4000)));
                                                                    player.addItem(stack);
                                                                    context.getSource().sendSystemMessage(Component.literal("Gave a fluid canister to " + player.getName()));
                                                                    return 1;
                                                                }
                                                        )

                                        )
                        )
        );
    }
}
