package safro.archon.util.criterion;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import safro.archon.Archon;
import safro.archon.api.Spell;
import safro.archon.registry.SpellRegistry;
import safro.archon.util.ArchonUtil;

import java.util.List;
import java.util.Locale;

public class LearnSpellCriterion extends AbstractCriterion<LearnSpellCriterion.Conditions> {
    public static final String ONE = "one";
    public static final String ALL = "all";
    static final Identifier ID = new Identifier(Archon.MODID, "learn_spell");

    public LearnSpellCriterion() {
    }

    public Identifier getId() {
        return ID;
    }

    public LearnSpellCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        String type = JsonHelper.getString(jsonObject, "amount");
        return new LearnSpellCriterion.Conditions(extended, type);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> conditions.matches(player));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final String type;

        public Conditions(EntityPredicate.Extended player, String type) {
            super(LearnSpellCriterion.ID, player);
            this.type = checkType(type);
        }

        public boolean matches(ServerPlayerEntity player) {
            List<Spell> list = ArchonUtil.getSpells(player);
            if (type.equals(LearnSpellCriterion.ONE)) {
                return list.size() >= 1;
            }
            return list.size() == SpellRegistry.REGISTRY.size();
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            JsonPrimitive json = new JsonPrimitive(this.type);
            jsonObject.add("amount", json);
            return jsonObject;
        }

        public static String checkType(String type) {
            type = type.toLowerCase(Locale.ROOT);
            if (type.equals(LearnSpellCriterion.ALL) || type.equals(LearnSpellCriterion.ONE)) {
                return type;
            }
            throw new IllegalArgumentException("Spell Learned Type must be either 'one' or 'all'");
        }
    }
}
