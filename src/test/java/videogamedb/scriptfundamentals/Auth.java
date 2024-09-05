package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;


public class Auth extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static ChainBuilder auth =
            exec(http("Auth")
                    .post("/authenticate")
                    .body(StringBody(
                            "{\n" +
                                    "  \"password\": \"admin\",\n" +
                                    "  \"username\": \"admin\"\n" +
                                    "}"
                    ))
                    .check(jmesPath("token").saveAs("jwtToken"))); // токен сохраняется как переменная jwttoken

    private static ChainBuilder createNewGame =
            exec(http("Create new game")
                    .post("/videogame")
                    .header("Authorization","Bearer #{jwtToken}") //Коррелируется токен
                    .body(StringBody(
"{\n" +
        "  \"category\": \"Platform\",\n" +
        "  \"name\": \"Mario\",\n" +
        "  \"rating\": \"Mature\",\n" +
        "  \"releaseDate\": \"2012-05-04\",\n" +
        "  \"reviewScore\": 85\n" +
        "}"
                    )));

    private ScenarioBuilder scn = scenario("Video Game DB - Scenario 5 code")
            .exec(auth)
            .exec(createNewGame);


    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);

    }
}
