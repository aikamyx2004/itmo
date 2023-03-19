package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;
import ru.itmo.web.hw4.model.UserColor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", UserColor.GREEN),
            new User(6, "pashka", "Pavel Mavrin", UserColor.RED),
            new User(9, "geranazavr555", "Georgiy Nazarov", UserColor.BLUE),
            new User(11, "tourist", "Gennady Korotkevich", UserColor.GREEN)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }

    private static final List<Post> POSTS = List.of(
            new Post(1, "Codeforces Round #830 (Div. 2)",
                    "Привет, Codeforces!\n" +
                            "В четверг, 20 октября 2022 г. в 17:35 состоится Educational Codeforces Round 138 (Rated for Div. 2).\n" +
                            "Продолжается серия образовательных раундов в рамках инициативы Harbour.Space University! Подробности о сотрудничестве Harbour.Space University и Codeforces можно прочитать в посте.\n" +
                            "Этот раунд будет рейтинговым для участников с рейтингом менее 2100. Соревнование будет проводиться по немного расширенным правилам ICPC. Штраф за каждую неверную посылку до посылки, являющейся полным решением, равен 10 минутам. После окончания раунда будет период времени длительностью в 12 часов, в течение которого вы можете попробовать взломать абсолютно любое решение (в том числе свое). Причем исходный код будет предоставлен не только для чтения, но и для копирования.\n" +
                            "Вам будет предложено 6 или 7 задач на 2 часа. Мы надеемся, что вам они покажутся интересными.\n" +
                            "Задачи вместе со мной придумывали и готовили Адилбек adedalic Далабаев, Владимир vovuh Петров, Иван BledDest Андросов и Максим Neon Мещеряков. Также большое спасибо Михаилу MikeMirzayanov Мирзаянову за системы Polygon и Codeforces.\n" +
                            "Удачи в раунде! Успешных решений!",
                    1, 42),
            new Post(3, "Codeforces Round #828 (Div. 3)",
                    "Hello, Codeforces\n" +
                            "Codeforces Round #510 (Div. 2) will start at Monday, September 17, 2018 at 11:05. The round will be rated for Div. 2 contestants (participants with the rating below 2100). Div. 1 participants can take a part out of competition as usual.\n" +
                            "This round is held on the tasks of the school stage All-Russian Olympiad of Informatics 2018/2019 year in city Saratov. The problems were prepared by PikMike, fcspartakm, Ne0n25, BledDest, Ajosteen and Vovuh. Great thanks to our coordinator _kun_ for the help with the round preparation! I also would like to thank our testers DavidDenisov, PrianishnikovaRina, Decibit and Vshining.\n" +
                            "UPD: The scoring distribution is 500-1000-1500-2000-2250-2750.",
                    6, 68),
            new Post(5, "Codeforces Round #829 (Div.1, Div.2, по МКОШП, рейтинговый)",
                    "Всем привет!\n" +
                            "В воскресенье в Москве пройдет XX Московская командная олимпиада — командное соревнование для школьников, проходящее в Москве как отборочное соревнование на ВКОШП. Олимпиаду подготовила Московская методическая комиссия, известная вам также по Московской олимпиаде школьников по программированию, Открытой олимпиаде школьников по программированию и олимпиаде Мегаполисов (раунды 327, 342, 345, 376, 401, 433, 441, 466, 469, 507, 516, 541, 545, 567, 583, 594, 622, 626, 657, 680, 704, 707, 727, 751, 775, 802).\n" +
                            "Раунд состоится в воскресенье, 23 октября 2022 г. в 10:50 и продлится 2 часа. Обратите внимание на нестандартное время начала раунда. В каждом дивизионе будет предложено по 6 задач. Раунд будет проведён по правилам Codeforces, будет рейтинговым для обоих дивизионов.\n" +
                            "В связи с этим мы просим всех участников сообщества, участвующих в соревновании, проявить уважение к себе и другим участникам соревнования и не пытаться читерить никоим образом, в частности, выясняя задачи у участников соревнования в Москве. Если вы узнали какие-либо из задач МКОШП (участвуя в ней лично, от кого-то из участников или каким-либо иным образом), пожалуйста, не пишите раунд. Участников олимпиады мы просим воздержаться от публичного обсуждения задач до окончания раунда. Любое нарушение правил выше будет являться поводом для дисквалификации.\n" +
                            "Задачи соревнования были подготовлены Tikhon228, teraqqq, Ormlis, sevlll777, Artyom123, vaaven, Mangooste, Siberian, Alexdat2000, TeaTime, Ziware под руководством Tikhon228, grphil и Андреевой Елены Владимировны.\n" +
                            "Спасибо DishonoredRighteous и KAN за координацию раунда, перевод условий, подготовку и предоставление дополнительных задач, а так же MikeMirzayanov за системы Codeforces и Polygon, который использовался при подготовке задач этой олимпиады.\n" +
                            "Также спасибо KLPP и TheOneYouWant за предоставление и подготовку дополнительных задач, которые помогли составить (я надеюсь) сбалансированный проблемсет для раунда.\n" +
                            "Обращаем ваше внимание, что рейтинги не будут пересчитаны до начала Codeforces Round #830 (Div. 2), системное тестирование также может быть задержано до окончания Codeforces Round #830 (Div. 2).", 1, 21)
    );
}
