// import ratpack.groovy.template.MarkupTemplateModule

// import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.*
import ratpack.session.SessionModule
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.http.client.direct.ParameterClient
import ratpack.pac4j.RatpackPac4j
import ratpack.exec.Blocking
import org.pac4j.core.profile.CommonProfile
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.http.client.direct.HeaderClient

String JWT_SALT = '12345678901234567890123456789012' // length = 32

// Direct client authenticator
final def signatureConfiguration = new SecretSignatureConfiguration(JWT_SALT)
final def encryptionConfiguration = new SecretEncryptionConfiguration(JWT_SALT)
final def jwtAuthenticator = new JwtAuthenticator([signatureConfiguration], [encryptionConfiguration])
final def parameterClient = new ParameterClient("token", jwtAuthenticator)
final def headerClient = new HeaderClient("Authorization", "Bearer ", jwtAuthenticator)
parameterClient.supportGetRequest = true
parameterClient.supportPostRequest = true

ratpack {

  serverConfig {
    development(true)
  }

  bindings {
    // module MarkupTemplateModule
    module SessionModule
  }

  handlers {

    // all(RatpackPac4j.authenticator('callback', parameterClient))
    all(RatpackPac4j.authenticator(parameterClient, headerClient))

    // get {
      // render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
    //   render json(['message':'ok'])
    // }

    post('login') {
      parse(jsonNode()).then { def data ->
        Blocking.get({
          CommonProfile model = new CommonProfile()
          model.addAttribute('username', data['username'].asText())
          model.addAttribute('password', data['password'].asText())
          JwtGenerator generator = new JwtGenerator(signatureConfiguration, encryptionConfiguration)
          return generator.generate(model)
        }).onError({ def e ->
          ctx.response.status(400).send(e.message)
        }).then({ def token ->
          render json(['token': token])
        })
      }
    }

    post("logout") { def ctx ->
        RatpackPac4j.logout(ctx).then {
            // redirect("/") // needed when using server rendering
            render json(['result': true])
        }
    } // post "/logout"


    prefix('admin'){
      // Secured with URL Parameter Token
      all(RatpackPac4j.requireAuth(ParameterClient.class))

      get {
        // render groovyMarkupTemplate("admin.gtpl")
        render json(['admin':true])
      }
    }

    prefix('secured'){
      // Secured with Authorization Header Token
      all(RatpackPac4j.requireAuth( HeaderClient.class ))

      get { def ctx ->
        // render json(['message':'success'])
        RatpackPac4j.userProfile(ctx).route({ def profile ->
          profile.isPresent()
        },{ def profile ->
        //    // You can also used `profile.get().attributes` which is a user profile
           render json(profile)
        //
        }).then{ def profile ->
            ctx.response.status(400).send("Access denied.")
        }
      }
    } // prefix('secured'

    // Use indexFiles in case you want to build a REST API with a unique web page
    files { dir "public" indexFiles 'index.html' }

  } //handlers

}
