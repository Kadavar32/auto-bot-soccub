package org.dormitory.autobotsoccub.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreTableRecord {
    private long userId;
    private MatchTeam team;
    private PlayPosition position;
    private int scored;
    private int missed;
    private int autoScored;
}
