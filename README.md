# FunixAPI
API used for Funix Projects

Version [![Maven Central](https://img.shields.io/maven-central/v/fr.funixgaming.api/funix-api.svg)](https://search.maven.org/artifact/fr.funixgaming.api/funix-api)

### Importer le projet
- Ajouter comme parent le projet funix-api (requis)
```xml
<parent>
    <artifactId>funix-api</artifactId>
    <groupId>fr.funixgaming.api</groupId>
    <version>(version)</version>
</parent>
```

- Si vous voulez utiliser le core
```xml
<dependency>
    <groupId>fr.funixgaming.api.core</groupId>
    <artifactId>funix-api-core</artifactId>
    <version>(version)</version>
    <scope>compile</scope>
</dependency>
```

- Si vous voulez utiliser l'api funix
```xml
<dependency>
    <groupId>fr.funixgaming.api.client</groupId>
    <artifactId>funix-api-client</artifactId>
    <version>(version)</version>
    <scope>compile</scope>
</dependency>
```

- Si vous voulez utiliser le serveur
```xml
<dependency>
    <groupId>fr.funixgaming.api.server</groupId>
    <artifactId>funix-api-server</artifactId>
    <version>(version)</version>
    <scope>compile</scope>
</dependency>
```

### Configuration des app spring

Vous devez spécifier les variables d'env ou alors les changer avec une surcharge de properties.

- App api application.properties
````properties
funix.api.app-domain-url=https://api.funixgaming.fr

funix.api.user-api-username=${API_USERNAME}
funix.api.user-api-password=${API_PASSWORD}
````

- App core application.properties
````properties
#Google captcha settings (console captcha google: https://www.google.com/recaptcha/admin)
google.recaptcha.key.site=${GOOGLE_RECAPTCHA_SITE}
google.recaptcha.key.secret=${GOOGLE_RECAPTCHA_SECRET}
google.recaptcha.key.threshold=0.7
#You can disable google captcha with this, cool in unit test env or local development
#If you set it to true you only need to set this google.recaptcha line
google.recaptcha.key.disabled=false

#For securing some actions by ip on api (example: 127.0.0.1,10.2.4.5)
config.api.ip-whitelist=${API_WHITELIST}
#128, 192, or 256 bits for encryption key strength
config.api.key-size=128

#Mail config
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USER}
spring.mail.password=${MAIL_USER_PASSWORD}
#Mail advanced config (here is default values)
spring.mail.tls=true
spring.mail.protocol=smtp
spring.mail.ssl=true
spring.mail.debug=false
sping.mail.auth=true
````

- App service application.properties
````properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://${DB_HOSTNAME}:${DB_PORT}/${DB_DATABASE}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

server.port=${APP_PORT}

funix.api.email=${API_EMAIL}
funix.api.password-numbers=${PASSWORD_NUMBERS}
funix.api.password-specials=${PASSWORD_SPECIALS}
funix.api.password-caps=${PASSWORD_CAPS}
funix.api.password-min=${PASSWORD_MIN}

paypal.url.auth=https://api-m.sandbox.paypal.com
paypal.client-id=${PAYPAL_CLIENT_ID}
paypal.client-secret=${PAYPAL_CLIENT_SECRET}
````