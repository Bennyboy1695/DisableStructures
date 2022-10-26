package io.github.bennyboy1695.disablestructures.mixin;

import io.github.bennyboy1695.disablestructures.Config;
import io.github.bennyboy1695.disablestructures.DisableStructures;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {


    @Inject(method = "tryGenerateStructure", at = @At("HEAD"), cancellable = true)
    public void disableStructures$AttemptStructureDisable(StructureSet.StructureSelectionEntry structureSelectionEntry, StructureFeatureManager structureFeatureManager, RegistryAccess registryAccess, StructureManager structureManager, long chunk, ChunkAccess chunkAccess, ChunkPos chunkPos, SectionPos sectionPos, CallbackInfoReturnable<Boolean> cir) {
        if (Config.COMMON.disabledStructures.get().contains(structureSelectionEntry.structure().value().feature.getRegistryName().toString())) {
            if (Config.COMMON.debug.get()) {
                DisableStructures.LOGGER.debug("Disabled generation of {}", structureSelectionEntry.structure().value().feature.getRegistryName().toString());
            }
            cir.setReturnValue(false);
        }
    }
}
