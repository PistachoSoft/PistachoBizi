# PistachoBizi

## Preinstall

You will need __nodejs__ and __Tomcat__ installed on your computer/server.

Installing __gradle__ is totally optional, gradle wrapper is included in the project.
In case you use the wrapper, every `gradle` command will be either `./gradlew` or `gradlew.bat`.

E.g: `./gradlew clean`

## Install, build and deploy.

1. Launch Tomcat.
2. Download and deploy AXIS webapp inside Tomcat instance.
3. Run `gradle build` to build project files.
4. Copy `build\classes\main\service` and `build\classes\main\parser` to AXIS webapp `classes` folder.
5. Run `gradle deployAxisService`, this will deploy weather SOAP services.
6. Run `npm install`, this will build the webapp, installing necessary dependencies.
7. Run `gradle war`, this will create the application folder with frontend and backend ready to deploy.
8. Copy `build\libs\bizi.war` to Tomcat webapps folder and let it deploy. It's NECESSARY that it runs on context `bizi`.
9. You're done.

## Run

Go to [localhost:8080/bizi](http://localhost:8080/bizi). Additionally, we have a public instance of the application deployed at [recu.synology.me:8080/bizi](http://recu.synology.me:8080/bizi)

## Technologies involved

- AngularJS [angular-ui-router, angular-chartjs]
- ChartJS
- Bootstrap
- Bootstrap-Material-Design
- jQuery
- NodeJS
- Bower
- Jersey Jax-RS
- Java
- Gradle
