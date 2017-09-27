package org.dormitory.autobotsoccub.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameData {
    private Game game;
    private ScoreTable scoreTable;
}
