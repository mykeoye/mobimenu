package io.mobimenu.domain

import io.mobimenu.domain.enums.SalesChannel
import spock.lang.Specification
import java.time.Duration
import java.time.LocalTime

class RestaurantSpec extends Specification {

    def "test that a restaurant cannot operate outside its working hours"() {
        given: "a created restaurant object with working hours set"
            var restaurant = createTestObject([SalesChannel.DINE_IN] as Set,
                new WorkPeriod(LocalTime.of(4, 55, 00),
                    LocalTime.of(22, 00, 00),
                    Duration.ofMinutes(15))
            )

        when: "we check to see if the restaurant is within operating hours"
            var isOperational = restaurant.isWithinWorkingPeriod(LocalTime.of(4, 56, 00))
        then:
            isOperational
    }

    def "test that a restaurant can only allow order from channels it registered"() {
        given: "a created restaurant with sales channels"
            var restaurant = createTestObject([SalesChannel.PICKUP] as Set,
                new WorkPeriod(LocalTime.of(4, 55, 00),
                    LocalTime.of(22, 00, 00),
                    Duration.ofMinutes(15))
            )
        when: "we try to check for an unsupported sales channel"
            var offersDelivery = restaurant.canAcceptOrderFromChannel(SalesChannel.DELIVERY)
        then:
            !offersDelivery
    }

    def createTestObject(channels, workPeriod) {
        return Restaurant.withAllFields(
                UUID.randomUUID().toString(),
                "Dominoes Pizza",
                Restaurant.Type.CHAIN,
                "info@dominos.ng",
                "10 Agungi Ajiran Road Lekki Phase 1",
                "https://smartmenu.io/cdn/84uufufs",
                channels,
                new PhoneNumber("+234", "08135009935"),
                workPeriod
        )
    }

}
