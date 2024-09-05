package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;


public class MultiplyMethods extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static ChainBuilder getAllVideogames =
            exec(http("Get all video games")
                    .get("/videogame")
                    .check(status().not(404), status().not(500)));

    private static ChainBuilder getSpecificVideoGame =
            exec(http("Get specific video game")
                    .get("/videogame/1")
                    .check(status().is(200)));

    private ScenarioBuilder scn = scenario("Video Game DB - Scenario 5 code")
            .exec(getAllVideogames)
            .pause(5)
            .exec(getSpecificVideoGame)
            .pause(5)
            .exec(getSpecificVideoGame);

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);

    }
}
