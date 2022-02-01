package safro.archon.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class PrimeSkeltEntity extends SkeltEntity {

    public PrimeSkeltEntity(EntityType<? extends SkeltEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createPrimeSkeltAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D).add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            if (player.isSneaking()) {
                if (stack.isEmpty()) {
                    dropNext();
                    return ActionResult.SUCCESS;
                } else if (stack.getItem() instanceof ArmorItem item) {
                    addNext(stack, item.getSlotType());
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.interactMob(player, hand);
    }

    // Very hacky code but it works
    private void dropNext() {
        if (this.hasStackEquipped(EquipmentSlot.HEAD)) {
            this.dropStack(this.getEquippedStack(EquipmentSlot.HEAD).copy());
            this.getEquippedStack(EquipmentSlot.HEAD).decrement(1);

        } else if (this.hasStackEquipped(EquipmentSlot.CHEST)) {
            this.dropStack(this.getEquippedStack(EquipmentSlot.CHEST).copy());
            this.getEquippedStack(EquipmentSlot.CHEST).decrement(1);

        } else if (this.hasStackEquipped(EquipmentSlot.LEGS)) {
            this.dropStack(this.getEquippedStack(EquipmentSlot.LEGS).copy());
            this.getEquippedStack(EquipmentSlot.LEGS).decrement(1);

        } else if (this.hasStackEquipped(EquipmentSlot.FEET)) {
            this.dropStack(this.getEquippedStack(EquipmentSlot.FEET).copy());
            this.getEquippedStack(EquipmentSlot.FEET).decrement(1);
        }
    }

    private void addNext(ItemStack stack, EquipmentSlot slot) {
        if (this.getEquippedStack(slot).isEmpty()) {
            this.equipStack(slot, stack.copy());
            stack.decrement(1);
        }
    }

    public void initEquipment(LocalDifficulty difficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }
}
