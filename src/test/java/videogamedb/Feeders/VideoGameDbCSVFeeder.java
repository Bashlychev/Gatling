package videogamedb.Feeders;

import io.gatling.core.scenario.Scenario;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class VideoGameDbCSVFeeder extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static FeederBuilder.FileBased<String> csvFeeder = csv("data/gameCsvFile.csv").circular(); // random -  выбирает рандомно значения с файла

    private static  ChainBuilder getSpecificGame =
            feed(csvFeeder)
                    .exec(http("Get video game with name - #{gameName}")  // хардкодим значения
            .get("/videogame/#{gameId}")
    .check(jmesPath("name").isEL("#{gameName}"))); // В приведённом вами коде используются EL-выражения, чтобы подставить динамические значения для параметров gameId и gameName в HTTP-запрос.

    private ScenarioBuilder scn = scenario("Video Game Db - Section 6 code")
            .repeat(10).on(
                    exec(getSpecificGame)
                            .pause(1)
            );

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}

// Создаем новую директорию в resources "data"
//Создаем новый файл в data "gameCsvFile"