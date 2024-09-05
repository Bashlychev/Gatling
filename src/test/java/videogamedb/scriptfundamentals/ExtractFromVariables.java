package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.netty.util.internal.SystemPropertyUtil;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class ExtractFromVariables extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private ScenarioBuilder scn = scenario("Video Game DB - Scenario 5 code")

            .exec(http("Get specific game")
                    .get("/videogame/1")
                    .check(status().in(200, 201, 202))
                    .check(jmesPath("name").is("Resident Evil 4")))
            .pause(1,10)

            .exec(http("Get all video games")
                    .get("/videogame")
                    .check(status().not (404), status().not(500))
                    .check(jmesPath("[1].id").saveAs("gameId"))) //first variable
            .pause(Duration.ofMillis(4000))

//            Debugging Gatling Session Variables

            .exec(
                    session -> {
                        System.out.println(session);
                        System.out.println("gameID set to: " + session.getString("gameId"));
                        return session;
                    }
            )

            .exec(http("Get specific game with ID - #{gameId}")
                    .get("/videogame/#{gameId}")
                    .check(jmesPath("name").is("Gran Turismo 3"))
                    .check(bodyString().saveAs("responseBody")))
    .exec(

            session -> {
                System.out.println("Response Body:" + session.getString("responseBody"));
                return session;
            }
    );

//    Этот сценарий Gatling выполняет следующие шаги:
//
//    Извлекает и выводит значение переменной gameId из сессии.
//    Делает HTTP GET запрос к /videogame/{gameId}, подставляя идентификатор игры, и проверяет, что имя игры соответствует "Gran Turismo 3".
//    Сохраняет полный ответ сервера в переменную responseBody.
//    Затем выводит сохраненное тело ответа в консоль для отладки.

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);

    }
}
