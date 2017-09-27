package org.dormitory.autobotsoccub.engine;


import org.dormitory.autobotsoccub.exception.GameDoesNotExistException;
import org.dormitory.autobotsoccub.model.Game;
import org.dormitory.autobotsoccub.model.GameData;
import org.dormitory.autobotsoccub.model.MatchTeam;
import org.dormitory.autobotsoccub.model.ScoreTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO: Document synchronization policy
public class GameEngine implements GameEngineOperations {

    private final Map<Integer, GameData> activeGames = new HashMap<>();
    private final List<Game> games = new ArrayList<>();

    @Override
    public boolean startGame(Integer userId, Game game) {
        synchronized (game) {
            if (activeGames.containsKey(userId)) {
                return false;
            }

            GameData gameData = GameData.builder()
                    .game(game)
                    .scoreTable(ScoreTable.fromGame(game))
                    .build();

            game.getPlayers().forEach(playerId -> activeGames.put(playerId, gameData));
            games.add(game);
        }

        return true;
    }

    @Override
    public GameData stopGame(Integer userId) {
        GameData gameData = findGameDataOrFail(userId);
        Game currentGame = gameData.getGame();
        synchronized (currentGame) {
            currentGame.getPlayers()
                    .forEach(activeGames::remove);
            games.remove(currentGame);
        }

        return gameData;
    }

    @Override
    public void score(Integer userId) {
        GameData activeGame = findGameDataOrFail(userId);
        synchronized (activeGame.getGame()) {
            ScoreTable scoreTable = activeGame.getScoreTable();
            MatchTeam currUserTeam = scoreTable.getTeamByUserId(userId);
            scoreTable.incrementScored(userId);
            scoreTable.incrementGoalkeeperMissed(MatchTeam.anotherTeam(currUserTeam));
        }
    }

    @Override
    public void autoScore(Integer userId) {
        GameData activeGame = findGameDataOrFail(userId);
        synchronized (activeGame.getGame()) {
            ScoreTable scoreTable = activeGame.getScoreTable();
            MatchTeam currUserTeam = scoreTable.getTeamByUserId(userId);
            scoreTable.incrementAutoScored(userId);
            scoreTable.incrementGoalkeeperMissed(currUserTeam);
        }
    }

    @Override
    public void changePositionsInTeam(Integer userId) {
        GameData activeGame = findGameDataOrFail(userId);
        synchronized (activeGame.getGame()) {
            ScoreTable scoreTable = activeGame.getScoreTable();
            MatchTeam team = scoreTable.getTeamByUserId(userId);
            scoreTable.swapPositions(team);
        }
    }

    @Override
    public void revert(Integer userId) {
        GameData activeGame = findGameDataOrFail(userId);
        synchronized (activeGame.getGame()) {
            ScoreTable scoreTable = activeGame.getScoreTable();
            scoreTable.getLastScoreType(userId)
                    .ifPresent(scoreType -> scoreTable.decrementScore(userId, scoreType));
        }
    }


    private GameData findGameDataOrFail(Integer userId) {
        return Optional.ofNullable(activeGames.get(userId))
                .orElseThrow(() -> new GameDoesNotExistException(userId));
    }

}
