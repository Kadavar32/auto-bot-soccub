package org.dormitory.autobotsoccub.bot

import org.dormitory.autobotsoccub.command.Command
import org.telegram.telegrambots.api.objects.Update
import spock.lang.Specification

class SoccubBotShould extends Specification {

    static NAME = "TestBotName"
    static TOKEN = "TestBotToken"

    def commandA = Mock(Command)
    def commandB = Mock(Command)
    def update = Mock(Update)

    def sut = new SoccubBot(NAME, TOKEN, [commandA, commandB])

    def "return specified bot name"() {
        expect:
        sut.getBotUsername() == NAME
    }

    def "return specified bot token"() {
        expect:
        sut.getBotToken() == TOKEN
    }

    def "execute correct command"() {
        given:
        commandA.accepts(update) >> false
        commandB.accepts(update) >> true

        when:
        sut.onUpdateReceived(update)

        then:
        1 * commandB.execute(update)
        0 * commandA.execute(_)
    }
}
