package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;


public class LoopingHTTPCalls extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static ChainBuilder getAllVideogames =
            repeat(3).on( // повторить 3 раза
            exec(http("Get all video games")
                    .get("/videogame")
                    .check(status().not(404), status().not(500)))
);

    private static ChainBuilder getSpecificVideoGame =
            repeat(5, "myCounter").on(
            exec(http("Get specific video game with id: #{myCounter}")
                    .get("/videogame/#{myCounter}")
                    .check(status().is(200)))
//                    .get("/videogame/#{myCounter}"): Это фактический HTTP-запрос к URL-адресу.
//                    Gatling подставит текущее значение myCounter в адрес запроса.
//                    Например, на первой итерации URL будет выглядеть как /videogame/1,
//                    на второй как /videogame/2 и так далее до /videogame/5
            );

    private ScenarioBuilder scn = scenario("Video Game DB - Scenario 5 code")
            .exec(getAllVideogames)
            .pause(5)
            .exec(getSpecificVideoGame)
            .pause(5)
            .repeat(2).on(
exec(getAllVideogames)
);
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);

    }
}
