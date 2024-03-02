package org.pac4j.demo

import spock.lang.Shared
import spock.lang.Specification
import ratpack.test.http.TestHttpClient
import ratpack.test.ApplicationUnderTest
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.CloseableApplicationUnderTest

class RatpackSpec extends Specification {

    @Shared
    CloseableApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()
    @Delegate
    TestHttpClient httpClient = aut.httpClient

    def setupSpec() {
        // System.setProperty('host', "http://${host}:${port}")
    }

    def cleanup() {
        aut.close()
    }

    def cleanupSpec() {
        // System.clearProperty('host')
    }

    def 'test is working'() {
      expect:
        httpClient != null
    }

    // def "redirects to home page"() {
        // given:
        //   def response = httpClient.get()
        // expect:
        //   response != null
          // response.statusCode == 200
          // response.body.text.contains("<h1>index</h1>")
    // }

}
