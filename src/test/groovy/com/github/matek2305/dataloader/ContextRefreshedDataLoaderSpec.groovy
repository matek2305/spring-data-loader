package com.github.matek2305.dataloader

import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import spock.lang.Specification

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
class ContextRefreshedDataLoaderSpec extends Specification {

    private ApplicationContext applicationContextMock
    private ContextRefreshedDataLoader dataLoader

    void setup() {
        applicationContextMock = Mock(ApplicationContext)
        dataLoader = new ContextRefreshedDataLoader(applicationContextMock)
    }

    def "should do nothing when not data loader beans found"() {
        given:
            def contextRefreshedEvent = Mock(ContextRefreshedEvent)
        and:
            applicationContextMock.getBeansOfType(DataLoader) >> new HashMap<String, DataLoader>()
        when:
            dataLoader.onApplicationEvent(contextRefreshedEvent)
        then:
            1 * applicationContextMock.getBeansOfType(DataLoader)
        and:
            0 * applicationContextMock._
    }
}
