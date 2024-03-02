Ratpack JWT & jQuery
-----------------------------

This is a simple demo of using JWT Authentication and jQuery.

This project demonstrate the usage of:

* Pac4j and JWT using both ParameterClient and HeaderClient
* Full AJAX using jQuery


* Project structure:

```
    <proj>
      |
      +- src
          |
          +- ratpack
          |     |
          |     +- Ratpack.groovy
          |     +- ratpack.properties
          |     +- public // Static assets in here
          |          |
          |          +- images
          |          +- lib
          |          +- scripts
          |             |
          |             +- app.js
          |             +- jquery-1.7.2.js
          |          +- styles
          |          |
          |          +- index.html
          |
          +- main
          |   |
          |   +- groovy
                   |
                   +- // App classes in here!
          |
          +- test
              |
              +- groovy
                   |
                   +- // Spock tests in here!
```

You can start this app with:

    ./gradle[w] run
