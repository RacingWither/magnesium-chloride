package net.racingwither.magnesiumchloride;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
import net.racingwither.magnesiumchloride.block.entity.HClBurnerFluidHandler;

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
                                                                Commands.argument("tank", IntegerArgumentType.integer(0))
                                                                        .then(
                                                                                Commands.argument("amount", IntegerArgumentType.integer(0, 8000))
                                                                                        .executes(context -> {
                                                                                            BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
                                                                                            ServerLevel level = context.getSource().getLevel();
                                                                                            BlockEntity be = level.getBlockEntity(pos);
                                                                                            IFluidHandler cap = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
                                                                                            if (cap != null) {
                                                                                                int tank = IntegerArgumentType.getInteger(context, "tank");
                                                                                                if (tank <= cap.getTanks() && cap instanceof HClBurnerFluidHandler hand) {
                                                                                                    hand.TANKS.get(tank).fill(new FluidStack(
                                                                                                            Fluids.WATER,
                                                                                                            IntegerArgumentType.getInteger(context, "amount")),
                                                                                                            IFluidHandler.FluidAction.EXECUTE);}}
                                                                                            return 1;
                                                                                        })
                                                                        ))
                                        )
                        )
        );
    }
}
