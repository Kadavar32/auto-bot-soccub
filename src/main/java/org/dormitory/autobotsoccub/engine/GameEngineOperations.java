package org.dormitory.autobotsoccub.engine;

import org.dormitory.autobotsoccub.model.Game;
import org.dormitory.autobotsoccub.model.GameData;

public interface GameEngineOperations {

    boolean startGame(Integer userId, Game game);
    GameData stopGame(Integer userId);

    void score(Integer userId);
    void autoScore(Integer userId);
    void changePositionsInTeam(Integer userId);
    void revert(Integer userId);

}



