package org.dormitory.autobotsoccub.model;

public enum MatchTeam {
    A, B;

    public static MatchTeam anotherTeam(MatchTeam team) {
        return team == A ? B : A;
    }
}
