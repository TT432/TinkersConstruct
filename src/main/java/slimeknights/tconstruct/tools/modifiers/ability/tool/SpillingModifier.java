package slimeknights.tconstruct.tools.modifiers.ability.tool;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.modifiers.spilling.SpillingRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.spilling.SpillingRecipeLookup;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.ability.armor.WettingModifier;

import javax.annotation.Nullable;

/** Modifier to handle spilling recipes */
public class SpillingModifier extends WettingModifier {
  /** Overridable method to create the attack context */
  @Override
  public ToolAttackContext createContext(LivingEntity self, @Nullable Player player, Entity attacker, FluidStack fluid) {
    spawnParticles(attacker, fluid);
    return new ToolAttackContext(self, player, InteractionHand.MAIN_HAND, attacker, attacker instanceof LivingEntity living ? living : null, false, 1.0f, false);
  }

  @Override
  public int afterEntityHit(IToolStackView tool, int level, ToolAttackContext context, float damageDealt) {
    if (damageDealt > 0 && context.isFullyCharged()) {
      FluidStack fluid = getFluid(tool);
      if (!fluid.isEmpty()) {
        SpillingRecipe recipe = SpillingRecipeLookup.findRecipe(context.getAttacker().level.getRecipeManager(), fluid.getFluid());
        if (recipe != null) {
          FluidStack remaining = recipe.applyEffects(fluid, level, context);
          spawnParticles(context.getTarget(), fluid);
          Player player = context.getPlayerAttacker();
          if (player == null || !player.isCreative()) {
            setFluid(tool, remaining);
          }
        }
      }
    }
    return 0;
  }
}
