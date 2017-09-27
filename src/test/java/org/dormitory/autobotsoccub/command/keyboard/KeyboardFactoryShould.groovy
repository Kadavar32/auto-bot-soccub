package org.dormitory.autobotsoccub.command.keyboard

import org.hamcrest.BaseMatcher
import spock.lang.Specification

import static org.dormitory.autobotsoccub.command.keyboard.Button.REGISTER
import static org.dormitory.autobotsoccub.command.keyboard.Button.UNREGISTER
import static spock.util.matcher.HamcrestSupport.that

class KeyboardFactoryShould extends Specification {

    def buttonFactory = new ButtonFactory()

    def sut = new KeyboardFactory(buttonFactory)

    def "build keyboard with specified buttons in one line"() {
        given:
        def createdKeyboard = sut.keyboardOf(REGISTER, UNREGISTER)
        def expectedKeyboard = [buttons]

        expect:
        that createdKeyboard.keyboard, internallyEqualsTo(expectedKeyboard)

        where:
        buttons = [REGISTER, UNREGISTER]
    }

    private static internallyEqualsTo(def expected) {
        [
                matches: { arg ->
                    expected == arg.collect {
                        row -> row.collect {
                            telegramButton -> Button.of(telegramButton.text, telegramButton.callbackData)
                    }}
                },
                describeTo: { description ->
                    description.appendText("be internally qeual to ${expected}")
                },
                describeMismatch: { list, description ->
                    description.appendValue(list.toListString()).appendText(" was not internally equal to ${expected}")
                }
        ] as BaseMatcher
    }
}
