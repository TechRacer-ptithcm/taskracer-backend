package ptithcm.itmc.taskracer.util.ranking;

import ptithcm.itmc.taskracer.common.object.RankingData;

public class RankingUtil {
    public static final Integer RAKING_TIER = 5;

    public static Integer getTotalPoint(Integer starPerTier, Integer pointPerStar) {
        return RAKING_TIER * starPerTier * pointPerStar;
    }

    public static RankingData calculateTierAndStar(Integer score, Integer starPerTier, Integer pointPerStar) {
        var tierSize = starPerTier * pointPerStar;
        var tier = (score / tierSize) + 1;
        var star = (score % tierSize) / pointPerStar + 1;
        tier = Math.min(tier, 5);
        star = Math.min(star, starPerTier);
        return RankingData.builder()
                .rank(null)
                .tier(tier)
                .star(star)
                .build();
    }
}
