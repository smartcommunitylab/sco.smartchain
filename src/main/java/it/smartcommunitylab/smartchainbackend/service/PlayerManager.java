package it.smartcommunitylab.smartchainbackend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.CertificationActionDTO;
import it.smartcommunitylab.smartchainbackend.bean.ConsumptionDTO;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.GamificationPlayerProfile;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.PlayerExperience;
import it.smartcommunitylab.smartchainbackend.bean.UnusablePersonage;
import it.smartcommunitylab.smartchainbackend.bean.UnusableReward;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.GameModel.CertificationAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;
import it.smartcommunitylab.smartchainbackend.model.Subscription;
import it.smartcommunitylab.smartchainbackend.model.Subscription.CompositeKey;
import it.smartcommunitylab.smartchainbackend.model.Subscription.Consumption;
import it.smartcommunitylab.smartchainbackend.repository.SubscriptionRepository;
import it.smartcommunitylab.smartchainbackend.service.GEHelper.RankingType;
import it.smartcommunitylab.smartchainbackend.service.Rankings.Ranking;

@Service
public class PlayerManager {

    @Autowired
    private GEHelper gamificationEngineHelper;

    @Autowired
    private GameModelManager gameModelManager;

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    private static final Logger logger = LogManager.getLogger(PlayerManager.class);

    public void playAction(String playerId, Action action) {
        final String gameModelId = action.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final GameModel model = gameModelManager.getModel(gameModelId);
        final ModelAction modelAction = model.getAction(action.getId());
        final String gamificationId = model.getGamificationId();
        final String gamificationActionId =
                gameModelManager.getGamificationActionId(gameModelId, action.getId());
        Action gamificationAction = new Action();
        gamificationAction.setGameId(gamificationId);
        gamificationAction.setId(gamificationActionId);
        gamificationAction.setParams(action.getParams());
        gamificationEngineHelper.action(playerId, gamificationAction);
        completeAction(action);
        logger.info("GameModel {} Player {} completed action {} (id: {})", gameModelId, playerId,
                modelAction.getName(),
                modelAction.getActionId());
    }

    public void playExperience(String playerId, Experience experience) {
        final String gameModelId = experience.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));

        subscription.ifPresent(s -> {
            final GameModel model = gameModelManager.getModel(gameModelId);
            final ModelExperience modelExperience = model.getExperience(experience.getId());
            if (s.getCompletedExperiences().contains(experience.getId())) {
                logger.warn("GameModel {} Player {} already completed experience {} (id: {})",
                        gameModelId, playerId,
                        modelExperience.getName(), modelExperience.getExperienceId());
            } else {
                final String gamificationId = model.getGamificationId();
                final String gamificationExperienceId = gameModelManager
                        .getGamificationExperienceId(gameModelId, experience.getId());
                Experience gamificationExperience = new Experience();
                gamificationExperience.setGameId(gamificationId);
                gamificationExperience.setId(gamificationExperienceId);
                gamificationEngineHelper.experience(playerId, gamificationExperience);
                completeExperience(experience);
                logger.info("GameModel {} Player {} completed experience {} (id: {})", gameModelId,
                        playerId,
                        modelExperience.getName(), modelExperience.getExperienceId());
            }
        });
    }


    public void certificateExperience(CertificationActionDTO certification) {
        final String gameModelId = certification.getGameModelId();
        final String playerId = certification.getPlayerId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final GameModel model = gameModelManager.getModel(gameModelId);
        final ModelExperience experience = model.getExperience(certification.getExperienceId());

        // used to check if certification exists, otherwise is thrown a runtime exception
        experience.getCertificationAction(certification.getCertificationId());

        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));

        subscription.ifPresent(s -> {
            if (!s.getCompletedExperiences().contains(experience.getExperienceId())) {
                if (!s.getCompletedCertifications().contains(certification.getCertificationId())) {
                    s.getCompletedCertifications().add(certification.getCertificationId());
                    subscriptionRepo.save(s);
                    logger.info(
                            "GameModel {} Player {} certificates action {} of experience {} (id: {})",
                            gameModelId, playerId, certification.getCertificationId(),
                            experience.getName(), experience.getExperienceId());
                    if (experience.isCompleted(s.getCompletedCertifications())) {
                        Experience exp = new Experience();
                        exp.setId(experience.getExperienceId());
                        exp.setGameId(gameModelId);
                        exp.setPlayerId(playerId);
                        playExperience(playerId, exp);
                    }
                } else {
                    logger.warn(
                            "GameModel {} Player {} already certificates action {} of experience {} (id: {})",
                            gameModelId, playerId, certification.getCertificationId(),
                            experience.getName(), experience.getExperienceId());
                }
            } else {
                logger.warn("GameModel {} Player {} already completed experience {} (id: {})",
                        gameModelId, playerId, experience.getName(), experience.getExperienceId());
            }
        });
    }

    private void completeExperience(Experience experience) {
       final String gameModelId = experience.getGameId();
       final String playerId = experience.getPlayerId();
        final GameModel model = gameModelManager.getModel(gameModelId);
        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));
        subscription.ifPresent(s -> {
            s.getCompletedExperiences().add(experience.getId());
            List<CertificationAction> certifications =
                    model.getExperience(experience.getId()).getCertificationActions();
            for (CertificationAction certification : certifications) {
                s.getCompletedCertifications().remove(certification.getCertificationId());
            }
            subscriptionRepo.save(s);
        });
    }

    private void completeAction(Action action) {
        final String gameModelId = action.getGameId();
        final String playerId = action.getPlayerId();
        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));
        subscription.ifPresent(s -> {
            s.getCompletedActions().add(action.getId());
            subscriptionRepo.save(s);
        });
    }

    public void consumePersonage(String playerId, PersonageDTO personage) {
        final String gameModelId = personage.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }
        final GameModel model = gameModelManager.getModel(gameModelId);
        final String gamificationId = model.getGamificationId();
        final Personage modelPersonage = model.getPersonage(personage.getId());
        Cost personageCost = modelPersonage.getCost();
        GamificationPersonage gamificationPersonage =
                new GamificationPersonage(gamificationId, personage, personageCost);
        gamificationEngineHelper.consumePersonage(playerId, gamificationPersonage);

        Subscription subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId)).get();
        subscription.getConsumedPersonages().add(new Consumption(personage.getId()));
        subscriptionRepo.save(subscription);
        logger.info("GameModel {} Player {} consumes character {} (id: {})", gameModelId, playerId,
                modelPersonage.getName(), modelPersonage.getPersonageId());
    }

    public void consumeReward(String playerId, GameRewardDTO reward) {
        final String gameModelId = reward.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final GameModel model = gameModelManager.getModel(gameModelId);
        final String gamificationId = model.getGamificationId();
        final ModelReward modelReward = model.getReward(reward.getId());
        Cost cost = modelReward.getCost();
        GamificationReward gamificationReward = new GamificationReward(gamificationId, cost);
        gamificationEngineHelper.consumeReward(playerId, gamificationReward);

        Subscription subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId)).get();
        subscription.getConsumedRewards().add(new Consumption(reward.getId()));
        subscriptionRepo.save(subscription);
        logger.info("GameModel {} Player {} consumes reward {} (id: {})", gameModelId, playerId,
                modelReward.getName(), modelReward.getRewardId());
    }

    public PlayerProfile getProfile(String playerId, String gameModelId) {
        final GameModel model = gameModelManager.getModel(gameModelId);
        final String gamificationId = model.getGamificationId();
        final GamificationPlayerProfile gamificationProfile = gamificationEngineHelper.getPlayerProfile(playerId,
                gameModelId,
                gamificationId);

        PlayerProfile profile =
                new PlayerProfile(gameModelId, gamificationProfile);

        List<Personage> personages = gameModelManager.getPersonages(gameModelId);
        List<ModelReward> rewards = gameModelManager.getRewards(gameModelId);
        
        List<Personage> usablePersonages = usablePersonages(personages, gamificationProfile);
        List<ModelReward> usableRewards = usableRewards(rewards, gamificationProfile);

        profile.setUsablePersonages(usablePersonages);
        profile.setUsableRewards(usableRewards);

        List<Personage> unusablePersonageModels =
                unusablePersonages(personages, profile.getUsablePersonages());
        List<ModelReward> unusableRewardModels =
                unusableRewards(rewards, profile.getUsableRewards());

        List<UnusablePersonage> unusablePersonages =
                addPersonageMissingScore(unusablePersonageModels, gamificationProfile);
        List<UnusableReward> unusableRewards =
                addRewardMissingScore(unusableRewardModels, gamificationProfile);

        profile.setUnusablePersonages(unusablePersonages);
        profile.setUnusableRewards(unusableRewards);

        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));
        subscription.ifPresent(s -> {
            List<ModelExperience> modelExperiences =
                    model.getExperiences(s.getCompletedExperiences());
            profile.setCompletedExperiences(convertExperience(modelExperiences, true));

            List<ModelAction> modelActions = model.getActions(s.getCompletedActions());
            profile.setCompletedActions(modelActions);

            List<ModelExperience> experiences = model.getExperiences();
            for (ModelExperience experience : experiences) {
                if (experience.containsAnyCertification(s.getCompletedCertifications())) {
                    profile.getStartedExperiences()
                            .add(new PlayerExperience(experience, s.getCompletedCertifications()));
                }
            }
            profile.setConsumedPersonages(s.getConsumedPersonages().stream()
                    .map(c -> new ConsumptionDTO(c)).collect(Collectors.toList()));
            
            profile.setConsumedRewards(s.getConsumedRewards().stream()
                    .map(r -> new ConsumptionDTO(r)).collect(Collectors.toList()));
        });

        return profile;
    }

    private List<UnusablePersonage> addPersonageMissingScore(List<Personage> unusablePersonages,
            GamificationPlayerProfile profile) {
        final double personageTerritoryScore = profile.getPersonageTerritoryScore();
        final double personageCultureScore = profile.getPersonageCultureScore();
        final double personageSportScore = profile.getPersonageSportScore();

        return unusablePersonages.stream().map(p -> {
            final double missingTerritoryScore = p.getCost().getTerritoryScore() - personageTerritoryScore;
            final double missingCultureScore = p.getCost().getCultureScore() - personageCultureScore;
            final double missingSportScore = p.getCost().getSportScore() - personageSportScore;
            UnusablePersonage change = new UnusablePersonage(p);
            change.setMissingScore(new Cost(missingTerritoryScore > 0 ? missingTerritoryScore : 0,
                    missingCultureScore > 0 ? missingCultureScore : 0,
                    missingSportScore > 0 ? missingSportScore : 0));
            return change;
        }).collect(Collectors.toList());
    }

    private List<UnusableReward> addRewardMissingScore(List<ModelReward> unusableRewards,
            GamificationPlayerProfile profile) {
        final double rewardTerritoryScore = profile.getRewardTerritoryScore();
        final double rewardCultureScore = profile.getRewardCultureScore();
        final double rewardSportScore = profile.getRewardSportScore();

        return unusableRewards.stream().map(p -> {
            final double missingTerritoryScore =
                    p.getCost().getTerritoryScore() - rewardTerritoryScore;
            final double missingCultureScore =
                    p.getCost().getCultureScore() - rewardCultureScore;
            final double missingSportScore = p.getCost().getSportScore() - rewardSportScore;
            UnusableReward change = new UnusableReward(p);
            change.setMissingScore(new Cost(missingTerritoryScore > 0 ? missingTerritoryScore : 0,
                    missingCultureScore > 0 ? missingCultureScore : 0,
                    missingSportScore > 0 ? missingSportScore : 0));
            return change;
        }).collect(Collectors.toList());
    }

    private List<Personage> unusablePersonages(List<Personage> personages,
            List<Personage> usablePersonages) {
        return personages.stream().filter(p -> !usablePersonages.contains(p))
                .collect(Collectors.toList());
    }

    private List<ModelReward> unusableRewards(List<ModelReward> rewards,
            List<ModelReward> usableRewards) {
        return rewards.stream().filter(p -> !usableRewards.contains(p))
                .collect(Collectors.toList());
    }

    public Rankings getRankings(String playerId, String gameModelId) {
        final String gamificationId = gameModelManager.getGamificationId(gameModelId);
        final Ranking territoryRanking = gamificationEngineHelper.getRanking(gamificationId,
                playerId, RankingType.TERRITORY);
        final Ranking cultureRanking =
                gamificationEngineHelper.getRanking(gamificationId, playerId, RankingType.CULTURE);
        final Ranking sportRanking =
                gamificationEngineHelper.getRanking(gamificationId, playerId, RankingType.SPORT);

        return new Rankings(territoryRanking, cultureRanking, sportRanking);
    }


    private List<PlayerExperience> convertExperience(List<ModelExperience> experiences,
            boolean completed) {
        return experiences.stream().map(e -> new PlayerExperience(e, completed))
                .collect(Collectors.toList());
    }

    private List<Personage> usablePersonages(List<Personage> personages,
            GamificationPlayerProfile profile) {
        final double territoryScore = profile.getPersonageTerritoryScore();
        final double cultureScore = profile.getPersonageCultureScore();
        final double sportscore = profile.getPersonageSportScore();
        return personages.stream()
                .filter(p -> p.getCost().getCultureScore() <= cultureScore
                        && p.getCost().getTerritoryScore() <= territoryScore
                        && p.getCost().getSportScore() <= sportscore)
                .collect(Collectors.toList());
    }

    private List<ModelReward> usableRewards(List<ModelReward> rewards,
            GamificationPlayerProfile profile) {
        final double territoryScore = profile.getRewardTerritoryScore();
        final double cultureScore = profile.getRewardCultureScore();
        final double sportscore = profile.getRewardSportScore();
        return rewards.stream()
                .filter(p -> p.getCost().getCultureScore() <= cultureScore
                        && p.getCost().getTerritoryScore() <= territoryScore
                        && p.getCost().getSportScore() <= sportscore)
                .collect(Collectors.toList());
    }

}
