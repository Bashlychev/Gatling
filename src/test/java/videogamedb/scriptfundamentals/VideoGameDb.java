package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class VideoGameDb extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private ScenarioBuilder scn = scenario("Video Game DB - Scenario 5 code")

            .exec(http("Get all video games - 1st call")
                    .get("/videogame") // "videogame" - это URI, на который отправляется GET-запрос.
                    .check(status().is(200))// используется для проверки ответа сервера на соответствие ожидаемым значениям или условиям.
//                    .check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4")))

            //Выражение $[?(@.id==1)].name — это JSONPath выражение, которое используется для выборки данных из JSON-структуры. В этом конкретном случае:
            //$ указывает на корень JSON-объекта.
            //?(@.id==1) является фильтром, который проверяет, что значение поля id равно 1. Символ @ ссылается на текущий объект.
            //.name указывает, что после применения фильтра нужно выбрать поле name у объектов, которые прошли фильтр.

                    .check(jmesPath("[? id == `1`].name").ofList().is(List.of("Resident Evil 4"))))

//            1. Выражение jmesPath("[? id == 1].name") - языке JMESPath выполняет фильтрацию элементов JSON-массива,
//            проверяя, есть ли в массиве элемент, где поле id равно 1.
//            [? id == 1]: Фильтрует элементы массива, где поле id равно 1.
//            .name: Извлекает значение поля name для каждого отфильтрованного элемента.
//            2. .ofList():
//    Этот метод указывает, что результат JMESPath-запроса будет списком (даже если в результате окажется один элемент).
//            3. .is(List.of("Resident Evil 4")):
//    Проверяет, что результат JMESPath-запроса (в виде списка) точно соответствует списку, содержащему одно значение "Resident Evil 4".
//    То есть проверка пройдет успешно, если результат JMESPath будет равен List.of("Resident Evil 4").

            .pause(5)

            .exec(http("Get specific game")
                    .get("/videogame/1")
                    .check(status().in(200, 201, 202)))

            .pause(1, 10)  //duration between 1 and 10

            .exec(http("Get all video games - 2nd call")
                    .get("/videogame")
                    .check(status().not (404), status().not(500)))
            .pause(Duration.ofMillis(40000));

    {
      setUp(
        scn.injectOpen(atOnceUsers(1))
).protocols(httpProtocol);

    }
}
