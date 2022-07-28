package slimeknights.tconstruct.tables.menu.slot;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.tables.block.entity.inventory.LazyResultContainer;

/**
 * Slot for display of {@link LazyResultContainer}.
 */
@SuppressWarnings("WeakerAccess")
public class LazyResultSlot extends Slot {
  protected final LazyResultContainer inventory;
  protected int amountCrafted = 0;
  @Nullable
  protected Player player;

  public LazyResultSlot(LazyResultContainer inventory, @Nullable Player player, int xPosition, int yPosition) {
    super(inventory, 0, xPosition, yPosition);
    this.inventory = inventory;
    this.player = player;
  }

  @Override
  public ItemStack getItem() {
    return inventory.getResult(player);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return false;
  }

  @Override
  public ItemStack remove(int amount) {
    if (this.hasItem()) {
      this.amountCrafted += Math.min(amount, this.getItem().getCount());
    }

    return super.remove(amount);
  }

  @Override
  public void onTake(Player player, ItemStack stack) {
    inventory.craftResult(player, amountCrafted);
    amountCrafted = 0;
  }

  @Override
  protected void onQuickCraft(ItemStack stack, int amount) {
    this.amountCrafted += amount;
    this.checkTakeAchievements(stack);
  }

  @Override
  protected void onSwapCraft(int numItemsCrafted) {
    this.amountCrafted += numItemsCrafted;
  }
}
