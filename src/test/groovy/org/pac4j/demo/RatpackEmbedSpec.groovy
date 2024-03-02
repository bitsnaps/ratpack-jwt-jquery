package org.pac4j.demo

import groovy.json.JsonSlurper
import spock.lang.Shared
import spock.lang.Specification
import ratpack.test.embed.EmbeddedApp
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import org.pac4j.http.client.direct.ParameterClient
import ratpack.pac4j.RatpackPac4j
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration
import ratpack.test.http.TestHttpClient

class RatpackEmbedSpec extends Specification {

    @Shared
    EmbeddedApp app = GroovyEmbeddedApp.of {
      String JWT_SALT = '12345678901234567890123456789012' // length = 32

      // Direct client authenticator
      final def signatureConfiguration = new SecretSignatureConfiguration(JWT_SALT)
      final def encryptionConfiguration = new SecretEncryptionConfiguration(JWT_SALT)
      final def jwtAuthenticator = new JwtAuthenticator([signatureConfiguration], [encryptionConfiguration])

      final def parameterClient = new ParameterClient("token", jwtAuthenticator)
      parameterClient.supportGetRequest = true
      parameterClient.supportPostRequest = true

        handlers {
            all(RatpackPac4j.authenticator('callback', parameterClient))
            get {
                render '{"data": [{"title" : "Groovy in Action", "author" : "Paul King"}]}'
            }
        }
    }

    // @Delegate
    TestHttpClient client = app.httpClient

    def 'app is working'() {
      expect:
        app != null
    }

    def "return data json object"() {
        given:
          def response = client.get()
          def json = new JsonSlurper()
        expect:
          response != null
          response.statusCode == 200
          def data = json.parseText(response.body.text)['data']
          with(data[0]) {
            title == "Groovy in Action"
          }
    }

}
