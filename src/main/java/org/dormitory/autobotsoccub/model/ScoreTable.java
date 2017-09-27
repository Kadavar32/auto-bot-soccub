package org.dormitory.autobotsoccub.model;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.ImmutableMap.of;
import static org.dormitory.autobotsoccub.model.Scores.AUTO_SCORED;
import static org.dormitory.autobotsoccub.model.Scores.SCORED;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreTable {
    private final Map<Long, ScoreTableRecord> statsByUserId;
    private final Map<Long, MatchTeam> teamsByUserIds;
    private final Map<MatchTeam, Map<PlayPosition, Long>> usersByTeamsAndPositions;
    private final Map<Long, Scores> lastScore;

    public static ScoreTable fromGame(Game currentGame) {
        ImmutableMap.Builder<Long, ScoreTableRecord> statsBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Long, MatchTeam> teamsByUserIdsBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<MatchTeam, Map<PlayPosition, Long>> usersByTeamsAndPositionsBuilder = ImmutableMap.builder();

        currentGame.getPlayersByTeams().entrySet().stream()
                .map(userKeyTeamValue -> ScoreTableRecord.builder()
                        .userId(userKeyTeamValue.getKey())
                        .position(userKeyTeamValue.getValue().getPosition())
                        .team(userKeyTeamValue.getValue().getTeam())
                        .build())
                .forEach(stats -> {
                    statsBuilder.put(stats.getUserId(), stats);
                    teamsByUserIdsBuilder.put(stats.getUserId(), stats.getTeam());
                    usersByTeamsAndPositionsBuilder.put(stats.getTeam(), of(stats.getPosition(), stats.getUserId()));
                });

        return new ScoreTable(statsBuilder.build(),
                teamsByUserIdsBuilder.build(),
                usersByTeamsAndPositionsBuilder.build(),
                new HashMap<>());
    }

    public Optional<Scores> getLastScoreType(long userId) {
        return Optional.ofNullable(lastScore.get(userId));
    }

    public void swapPositions(MatchTeam team) {
        Map<PlayPosition, Long> teamMap = usersByTeamsAndPositions.get(team);
        long currGoalkeeperUserId = teamMap.get(PlayPosition.GK);
        long currForwardUserId = teamMap.get(PlayPosition.FW);  //could be shortened through team's invariant
        usersByTeamsAndPositions.put(team, of(
                PlayPosition.GK, currForwardUserId,
                PlayPosition.FW, currGoalkeeperUserId));
    }

    public boolean isActivePlayer(long userId) {
        return statsByUserId.containsKey(userId);
    }

    public void incrementScored(long userId) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        userStats.setScored(userStats.getScored() + 1);
        lastScore.put(userId, SCORED);
    }

    public void decrementScored(long userId) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        userStats.setScored(userStats.getScored() - 1);          //check < 0
        lastScore.remove(userId);                                //for safety
    }

    public void incrementMissed(long userId) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        userStats.setMissed(userStats.getMissed() + 1);
    }

    public void decrementMissed(long userId) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        userStats.setMissed(userStats.getMissed() - 1);         //check < 0
    }

    public void incrementAutoScored(long userId) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        userStats.setAutoScored(userStats.getAutoScored() + 1);
        lastScore.put(userId, AUTO_SCORED);
    }

    public void decrementAutoScored(long userId) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        userStats.setAutoScored(userStats.getAutoScored() - 1);  //check < 0
        lastScore.remove(userId);                                //for safety
    }

    public void decrementScore(long userId, Scores scoreType) {
        ScoreTableRecord userStats = statsByUserId.get(userId);
        //TODO: Refactor
        if (scoreType == SCORED) {
            decrementScored(userId);
            decrementGoalkeeperMissed(MatchTeam.anotherTeam(userStats.getTeam()));
            return;
        }

        if (scoreType == AUTO_SCORED) {
            decrementAutoScored(userId);
            decrementGoalkeeperMissed(userStats.getTeam());
        }
    }

    public void incrementGoalkeeperMissed(MatchTeam team) {
        Optional.ofNullable(usersByTeamsAndPositions.get(team))
                .map(userIdsByPositions -> userIdsByPositions.get(PlayPosition.GK))
                .ifPresent(this::incrementMissed);
    }

    public void decrementGoalkeeperMissed(MatchTeam team) {
    }

    public MatchTeam getTeamByUserId(long userId) {
        return teamsByUserIds.get(userId);
    }


}
