yieldUnescaped '<!DOCTYPE html>'
html {
  head {
    meta(charset:'utf-8')
    title("Ratpack: $title")

    meta(name: 'apple-mobile-web-app-title', content: 'Ratpack')
    meta(name: 'description', content: '')
    meta(name: 'viewport', content: 'width=device-width, initial-scale=1')

    script(src:'/scripts/jquery-1.7.2.js'){}
    link(href: '/images/favicon.ico', rel: 'shortcut icon')
  }
  body {
    header {
      h1 'Ratpack'
      p 'Simple, lean &amp; powerful HTTP apps'
    }

    section {
      h2 title
      p 'This is the main page for your Ratpack app.'
    }

    form (id:'loginForm', method: 'POST', action: 'login') {
      input id:'username', name:'username', placeholder:'User Name'
      input id:'password', name:'password', placeholder:'Password'
      input type:'submit', value:'Login'
    }

    br()

    a href:"admin", 'Administration page'

    br()

    span (id:'token'){}

    br()
    button id:'btnSecured', 'Secured'


    footer {}

script(src:'/scripts/app.js'){}

  }
}
