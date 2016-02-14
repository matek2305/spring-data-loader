package com.github.matek2305.dataloader

import com.github.matek2305.dataloader.exception.DataLoaderBeanNotFoundException
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import spock.lang.Specification

import static com.github.matek2305.dataloader.TestDataLoaders.*

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
class ContextRefreshedDataLoaderSpec extends Specification {

    private ContextRefreshedEvent contextRefreshedEventMock
    private ApplicationContext applicationContextMock
    private ContextRefreshedDataLoader dataLoader

    void setup() {
        contextRefreshedEventMock = Mock(ContextRefreshedEvent)
        applicationContextMock = Mock(ApplicationContext)
        dataLoader = new ContextRefreshedDataLoader(applicationContextMock)
    }

    def "should do nothing when no data loader beans found"() {
        when:
            dataLoader.onApplicationEvent(contextRefreshedEventMock)
        then:
            1 * applicationContextMock.getBeansOfType(DataLoader) >> [:]
        then:
            0 * applicationContextMock._
    }

    def "should throw DataLoaderBeanNotFoundException when dependency bean not found"() {
        given:
            applicationContextMock.getBeansOfType(DataLoader) >> [
                    'firstDataLoader': Mock(FirstDataLoader)
            ]
        when:
            dataLoader.onApplicationEvent(contextRefreshedEventMock)
        then:
            def ex = thrown(DataLoaderBeanNotFoundException)
            ex.message.contains(FourthDataLoader.getName())
    }

    def "should call all loaders in proper order"() {
        given:
            def firstDataLoaderMock = Mock(FirstDataLoader)
            def secondDataLoaderMock = Mock(SecondDataLoader)
            def thirdDataLoaderMock = Mock(ThirdDataLoader)
            def fourthDataLoaderMock = Mock(FourthDataLoader)
            def anotherDataLoaderMock = Mock(AnotherDataLoader)
        and:
            applicationContextMock.getBeansOfType(DataLoader) >> [
                    'firstDataLoader': firstDataLoaderMock,
                    'secondDataLoader': secondDataLoaderMock,
                    'thirdDataLoader': thirdDataLoaderMock,
                    'fourthDataLoader': fourthDataLoaderMock,
                    'anotherDataLoader': anotherDataLoaderMock
            ]
        and:
            applicationContextMock.getBeanNamesForType(_ as Class<?>) >> { Class<?> type ->
                def simpleName = type.getSimpleName()
                return [Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1)]
            }
        when:
            dataLoader.onApplicationEvent(contextRefreshedEventMock)
        then:
            1 * anotherDataLoaderMock.load()
        then:
            1 * fourthDataLoaderMock.load()
        then:
            1 * firstDataLoaderMock.load()
        then:
            1 * thirdDataLoaderMock.load()
        then:
            1 * secondDataLoaderMock.load()
    }
}
