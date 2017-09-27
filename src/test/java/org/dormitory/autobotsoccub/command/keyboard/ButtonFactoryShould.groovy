package org.dormitory.autobotsoccub.command.keyboard

import spock.lang.Specification

import static org.dormitory.autobotsoccub.command.keyboard.Button.REGISTER
import static org.dormitory.autobotsoccub.command.keyboard.Button.UNREGISTER

class ButtonFactoryShould extends Specification {

    def sut = new ButtonFactory()

    def "create inline keyboard button with correct params"() {
        when:
        def createdButton = sut.create(buttonType)

        then:
        createdButton
        createdButton.text == buttonType.text
        createdButton.callbackData == buttonType.callBackQuery

        where:
        buttonType | _
        REGISTER   | _
        UNREGISTER | _
    }
}
