package io.github.bennyboy1695.disablestructures.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.bennyboy1695.disablestructures.Config;
import io.github.bennyboy1695.disablestructures.DisableStructures;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {


    @Inject(method = "tryGenerateStructure", at = @At("HEAD"), cancellable = true)
    public void disableStructures$AttemptStructureDisable(StructureSet.StructureSelectionEntry structureSelectionEntry, StructureManager structureManager, RegistryAccess registryAccess, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkAccess chunkAccess, ChunkPos chunkPos, SectionPos sectionPos, CallbackInfoReturnable<Boolean> cir) {
        ResourceLocation structure = registryAccess.registryOrThrow(BuiltinRegistries.STRUCTURES.key()).getKey(structureSelectionEntry.structure().get());
        if (Config.COMMON.disabledStructures.get().contains(structure.toString())) {
            if (Config.COMMON.debug.get()) {
                DisableStructures.LOGGER.debug("Disabled generation of {}", structure);
            }
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "findNearestMapStructure", at = @At("HEAD"), cancellable = true)
    public void disableStructures$FindNoDisabledStructuresInsteadOfLooking(ServerLevel serverLevel, HolderSet<Structure> structureHolderSet, BlockPos blockPos, int i, boolean bool, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        structureHolderSet.stream().forEach(configuredStructureFeatureHolder -> {
            ResourceLocation structure = serverLevel.registryAccess().registryOrThrow(BuiltinRegistries.STRUCTURES.key()).getKey(configuredStructureFeatureHolder.get());
            if (Config.COMMON.disabledStructures.get().contains(structure.toString())) {
                cir.setReturnValue(null);
            }
        });
    }
}
