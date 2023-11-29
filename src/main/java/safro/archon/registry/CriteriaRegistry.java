package safro.archon.registry;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.Criteria;
import safro.archon.util.criterion.ChanneledCriterion;
import safro.archon.util.criterion.LearnSpellCriterion;
import safro.archon.util.criterion.SummonUndeadCriterion;

public class CriteriaRegistry {
    public static final SummonUndeadCriterion SUMMON_UNDEAD_CRITERION = register(new SummonUndeadCriterion());
    public static final ChanneledCriterion CHANNELED_CRITERION = register(new ChanneledCriterion());
    public static final LearnSpellCriterion LEARN_SPELL_CRITERION = register(new LearnSpellCriterion());

    private static <T extends AbstractCriterion<?>> T register(T c) {
        return Criteria.register(c);
    }

    public static void init() {
    }
}
