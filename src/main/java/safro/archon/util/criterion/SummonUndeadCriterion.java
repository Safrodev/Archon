package safro.archon.util.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import safro.archon.Archon;

public class SummonUndeadCriterion extends AbstractCriterion<SummonUndeadCriterion.Conditions> {
    static final Identifier ID = new Identifier(Archon.MODID, "summon_undead");

    public SummonUndeadCriterion() {
    }

    public Identifier getId() {
        return ID;
    }

    public SummonUndeadCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        LootContextPredicate predicate2 = EntityPredicate.contextPredicateFromJson(jsonObject, "entity", advancementEntityPredicateDeserializer);
        return new SummonUndeadCriterion.Conditions(extended, predicate2);
    }

    public void trigger(ServerPlayerEntity player, AnimalEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, (conditions) -> {
            return conditions.matches(lootContext);
        });
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final LootContextPredicate entity;

        public Conditions(LootContextPredicate player, LootContextPredicate entity) {
            super(SummonUndeadCriterion.ID, player);
            this.entity = entity;
        }

        public static SummonUndeadCriterion.Conditions any() {
            return new SummonUndeadCriterion.Conditions(LootContextPredicate.EMPTY, LootContextPredicate.EMPTY);
        }

        public static SummonUndeadCriterion.Conditions create(EntityPredicate entity) {
            return new SummonUndeadCriterion.Conditions(LootContextPredicate.EMPTY, EntityPredicate.asLootContextPredicate(entity));
        }

        public boolean matches(LootContext tamedEntityContext) {
            return this.entity.test(tamedEntityContext);
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("entity", this.entity.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}
