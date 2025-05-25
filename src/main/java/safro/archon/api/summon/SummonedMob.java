package safro.archon.api.summon;

public interface SummonedMob {
    void archon$setLifetime(int seconds);

    void archon$setOwner(String uuid);

    boolean archon$isSummon();
}
