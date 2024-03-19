package com.parser.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class ParserApplication {
    List<Agrarian> agrarians = new ArrayList<>();
    List<VillageCouncil> villageCouncils = new ArrayList<>();
    HashMap<String, String> oblToOurOblHashMap = new HashMap<>();
    HashMap<String, String> ourOblToOblHashMap = new HashMap<>();
    HashMap<String, HashMap<String, String>> regions = new HashMap<>();
    Map<String, List<Region>> ourOblRegHashmap = new HashMap<>();

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(ParserApplication.class, args);
        ParserApplication parser = new ParserApplication();
        parser.jsonToHashMap();
        parser.start();
    }
    public void jsonToHashMap() throws JsonProcessingException {
        String json = "{\"vinnycya\":[{\"slug\":\"vinnitsa\",\"rus\":\"Винницкий район\"},{\"slug\":\"hnivan\",\"rus\":\"город Гнивань\"},{\"slug\":\"illinetsky\",\"rus\":\"Ильинецкий район\"},{\"slug\":\"lypovetsky\",\"rus\":\"Липовецкий район\"},{\"slug\":\"litinsky\",\"rus\":\"Литинский район\"},{\"slug\":\"nemirovsky\",\"rus\":\"Немировский район\"},{\"slug\":\"orativ\",\"rus\":\"Оратовский район\"},{\"slug\":\"pohrebishche\",\"rus\":\"Погребищенский район\"},{\"slug\":\"tyvriv\",\"rus\":\"Тывровский район\"},{\"slug\":\"turbiv\",\"rus\":\"Турбовский район\"},{\"slug\":\"gaissin\",\"rus\":\"Гайсинский район\"},{\"slug\":\"bershad\",\"rus\":\"Бершадский район\"},{\"slug\":\"ladizhin\",\"rus\":\"город Ладыжин\"},{\"slug\":\"teplichi\",\"rus\":\"Теплицкий район\"},{\"slug\":\"trostyanets\",\"rus\":\"Тростянецкий район\"},{\"slug\":\"chechelnytsky\",\"rus\":\"Чечельницкий район\"},{\"slug\":\"zhmerynka\",\"rus\":\"Жмеринский район\"},{\"slug\":\"barsky\",\"rus\":\"Барский район\"},{\"slug\":\"shargorod\",\"rus\":\"Шаргородский район\"},{\"slug\":\"mohyliv-podilsky\",\"rus\":\"Могилев-Подольский район\"},{\"slug\":\"murovankurilovetsky\",\"rus\":\"Мурованокуриловецкий район\"},{\"slug\":\"chernive\",\"rus\":\"Черновицкий район\"},{\"slug\":\"yampil\",\"rus\":\"Ямпольский район\"},{\"slug\":\"tulchyn\",\"rus\":\"Тульчинский район\"},{\"slug\":\"bratslav\",\"rus\":\"Брацлав район\"},{\"slug\":\"vapnyarka\",\"rus\":\"Вапнярка район\"},{\"slug\":\"kryzhopilsky\",\"rus\":\"Крыжопольский район\"},{\"slug\":\"pishchansky\",\"rus\":\"Песчанский район\"},{\"slug\":\"tomashpilsky\",\"rus\":\"Томашпольский район\"},{\"slug\":\"khmilnytsky\",\"rus\":\"Хмельницкий район\"},{\"slug\":\"kalynivsky\",\"rus\":\"Калиновский район\"},{\"slug\":\"kozyatyn\",\"rus\":\"Казатинский район\"}],\"volyn\":[{\"slug\":\"volodymyr-volynsky\",\"rus\":\"Владимир-Волынский район\"},{\"slug\":\"ivanychivsky\",\"rus\":\"Иваничевский район\"},{\"slug\":\"lokachynsky\",\"rus\":\"Локачинский район\"},{\"slug\":\"novovolynsky\",\"rus\":\"Нововолынский район\"},{\"slug\":\"ustiluh\",\"rus\":\"Устилуг\"},{\"slug\":\"kamin-kashyrsky\",\"rus\":\"Камень-Каширский район\"},{\"slug\":\"liubeshivsky\",\"rus\":\"Любешевский район\"},{\"slug\":\"manevytsky\",\"rus\":\"Маневицкий район\"},{\"slug\":\"kovel\",\"rus\":\"Ковельский район\"},{\"slug\":\"liubomlsky\",\"rus\":\"Любомльский район\"},{\"slug\":\"ratnivsky\",\"rus\":\"Ратновский район\"},{\"slug\":\"starovyzhivsky\",\"rus\":\"Старовыжевский район\"},{\"slug\":\"turiysky\",\"rus\":\"Турийский район\"},{\"slug\":\"shatsky\",\"rus\":\"Шацкий район\"},{\"slug\":\"lutsk\",\"rus\":\"Луцкий район\"},{\"slug\":\"berestechko\",\"rus\":\"Берестечко\"},{\"slug\":\"horokhivsky\",\"rus\":\"Гороховский район\"},{\"slug\":\"kivertsivsky\",\"rus\":\"Киверцовский район\"},{\"slug\":\"rozhysche\",\"rus\":\"Рожищенский район\"},{\"slug\":\"olyka\",\"rus\":\"Олыка\"},{\"slug\":\"torchyn\",\"rus\":\"Торчин\"},{\"slug\":\"tsuman\",\"rus\":\"Цумань\"}],\"dnipro\":[{\"slug\":\"dniprovsky\",\"rus\":\"Днепровский район\"},{\"slug\":\"novopokrovske\",\"rus\":\"Новопокровское\"},{\"slug\":\"obukhivka\",\"rus\":\"Обуховка\"},{\"slug\":\"slobozhanske\",\"rus\":\"Слобожанское\"},{\"slug\":\"pidhorodnenske\",\"rus\":\"Подгородненское\"},{\"slug\":\"petrikivsky\",\"rus\":\"Петриковский\"},{\"slug\":\"tsarychansky\",\"rus\":\"Царичанский район\"},{\"slug\":\"kamiansky\",\"rus\":\"Каменский район\"},{\"slug\":\"verkhnedniprovsky\",\"rus\":\"Верхнеднепровский район\"},{\"slug\":\"krynychansky\",\"rus\":\"Криничанский район\"},{\"slug\":\"piatykhatky\",\"rus\":\"Пятихатский район\"},{\"slug\":\"zhovti-vody\",\"rus\":\"Желтые Воды\"},{\"slug\":\"krivorizky\",\"rus\":\"Криворожский район\"},{\"slug\":\"apostolivsky\",\"rus\":\"Апостоловский район\"},{\"slug\":\"sofiyivsky\",\"rus\":\"Софиевский район\"},{\"slug\":\"shyrokivsky\",\"rus\":\"Широковский район\"},{\"slug\":\"nikopolsky\",\"rus\":\"Никопольский район\"},{\"slug\":\"marganets\",\"rus\":\"Марганец\"},{\"slug\":\"pokrovsk\",\"rus\":\"Покровск\"},{\"slug\":\"tomakivsky\",\"rus\":\"Томаковский район\"},{\"slug\":\"novomoskovsky\",\"rus\":\"Новомосковский район\"},{\"slug\":\"magdalinivsky\",\"rus\":\"Магдалиновский район\"},{\"slug\":\"pereshchepyne\",\"rus\":\"Перещепино\"},{\"slug\":\"pavlogradsky\",\"rus\":\"Павлоградский район\"},{\"slug\":\"ternivka\",\"rus\":\"Терновка\"},{\"slug\":\"yuriivsky\",\"rus\":\"Юрьевский район\"},{\"slug\":\"sinyelnikivsky\",\"rus\":\"Синельниковский район\"},{\"slug\":\"vasylkivsky\",\"rus\":\"Васильковский район\"},{\"slug\":\"pershotravensk\",\"rus\":\"Першотравенск\"},{\"slug\":\"petropavlivsky\",\"rus\":\"Петропавловский район\"},{\"slug\":\"pokrovsky\",\"rus\":\"Покровский район\"}],\"donetsk\":[{\"slug\":\"bakhmutske\",\"rus\":\"Бахмутский район\"},{\"slug\":\"volnovaske\",\"rus\":\"Волновахский район\"},{\"slug\":\"velikonovosilkivske\",\"rus\":\"Великоновосельковский район\"},{\"slug\":\"horlivske\",\"rus\":\"Горловский район\"},{\"slug\":\"shakhtarske\",\"rus\":\"Шахтерский район\"},{\"slug\":\"debaltsieve\",\"rus\":\"Дебальцево\"},{\"slug\":\"yenakiieve\",\"rus\":\"Енакиево\"},{\"slug\":\"snizhne\",\"rus\":\"Снежное\"},{\"slug\":\"donetske\",\"rus\":\"Донецкий район\"},{\"slug\":\"amvrosiivske\",\"rus\":\"Амвросиевский район\"},{\"slug\":\"ilovaisk\",\"rus\":\"Иловайск\"},{\"slug\":\"makiiivka\",\"rus\":\"Макеевка\"},{\"slug\":\"khartsyzk\",\"rus\":\"Харцызск\"},{\"slug\":\"yasinuvatske\",\"rus\":\"Ясиноватский район\"},{\"slug\":\"kalmiuske\",\"rus\":\"Кальмиусский район\"},{\"slug\":\"boykivske\",\"rus\":\"Бойковский район\"},{\"slug\":\"novoazovske\",\"rus\":\"Новоазовский район\"},{\"slug\":\"starobeshivske\",\"rus\":\"Старобешевский район\"},{\"slug\":\"dokuchayivske\",\"rus\":\"Докучаевск\"},{\"slug\":\"kramatorske\",\"rus\":\"Краматорский район\"},{\"slug\":\"druzhkivka\",\"rus\":\"Дружковка\"},{\"slug\":\"kostyantynivske\",\"rus\":\"Константиновский район\"},{\"slug\":\"lymanske\",\"rus\":\"Лиманский район\"},{\"slug\":\"oleksandrivske\",\"rus\":\"Александровский район\"},{\"slug\":\"slovyanske\",\"rus\":\"Славянский район\"},{\"slug\":\"mariupolske\",\"rus\":\"Мариупольский район\"},{\"slug\":\"manguske\",\"rus\":\"Мангушский район\"},{\"slug\":\"nikolske\",\"rus\":\"Никольский район\"},{\"slug\":\"pokrovske\",\"rus\":\"Покровский район\"},{\"slug\":\"avdiivka\",\"rus\":\"Авдеевка\"},{\"slug\":\"dobropilsky\",\"rus\":\"Добропольский район\"},{\"slug\":\"kurakhove\",\"rus\":\"Курахово\"},{\"slug\":\"maryinka\",\"rus\":\"Марьинка\"}],\"zhytomyr\":[{\"slug\":\"berdychivsky\",\"rus\":\"Бердичевский район\"},{\"slug\":\"andrushivsky\",\"rus\":\"Андрушевский район\"},{\"slug\":\"ruzhynsky\",\"rus\":\"Ружинский район\"},{\"slug\":\"zhytomyrsky\",\"rus\":\"Житомирский район\"},{\"slug\":\"brusylivsky\",\"rus\":\"Брусиловский район\"},{\"slug\":\"korostyshivsky\",\"rus\":\"Коростышевский район\"},{\"slug\":\"liubarsky\",\"rus\":\"Любарский район\"},{\"slug\":\"popilniansky\",\"rus\":\"Попильнянский район\"},{\"slug\":\"pulynsky\",\"rus\":\"Пулинский район\"},{\"slug\":\"radomyshlsky\",\"rus\":\"Радомышльский район\"},{\"slug\":\"romanivsky\",\"rus\":\"Романовский район\"},{\"slug\":\"khoroshivsky\",\"rus\":\"Хорошевский район\"},{\"slug\":\"chernyakhivsky\",\"rus\":\"Черняховский район\"},{\"slug\":\"chudnivsky\",\"rus\":\"Чудновский район\"},{\"slug\":\"korostensky\",\"rus\":\"Коростенский район\"},{\"slug\":\"luhynsky\",\"rus\":\"Лугинский район\"},{\"slug\":\"malynsky\",\"rus\":\"Малинский район\"},{\"slug\":\"narodytsky\",\"rus\":\"Народицкий район\"},{\"slug\":\"ovrutsky\",\"rus\":\"Овруцкий район\"},{\"slug\":\"olevsky\",\"rus\":\"Олевский район\"},{\"slug\":\"novohrad-volynsky\",\"rus\":\"Новоград-Волынский район\"},{\"slug\":\"baranivsky\",\"rus\":\"Барановский район\"},{\"slug\":\"yemilchynsky\",\"rus\":\"Емильчинский район\"},{\"slug\":\"horodnytsya\",\"rus\":\"Городница\"},{\"slug\":\"dovbyske\",\"rus\":\"Довбыш\"}],\"zakarpattya\":[{\"slug\":\"berehivsky\",\"rus\":\"Береговский район\"},{\"slug\":\"vinohradivsky\",\"rus\":\"Виноградовский район\"},{\"slug\":\"mukachivsky\",\"rus\":\"Мукачевский район\"},{\"slug\":\"volovetsky\",\"rus\":\"Воловецкий район\"},{\"slug\":\"svaliavsky\",\"rus\":\"Свалявский район\"},{\"slug\":\"rakhivsky\",\"rus\":\"Раховский район\"},{\"slug\":\"tyachivsky\",\"rus\":\"Тячевский район\"},{\"slug\":\"uzhhorodsky\",\"rus\":\"Ужгородский район\"},{\"slug\":\"velykoberizniansky\",\"rus\":\"Великоберезнянский район\"},{\"slug\":\"perechynsky\",\"rus\":\"Перечинский район\"},{\"slug\":\"chop\",\"rus\":\"Чоп\"},{\"slug\":\"khustsky\",\"rus\":\"Хустский район\"},{\"slug\":\"irshavsky\",\"rus\":\"Иршавский район\"},{\"slug\":\"mizhhirsky\",\"rus\":\"Межгорский район\"}],\"zaporizhzhya\":[{\"slug\":\"berdiansky\",\"rus\":\"Бердянский район\"},{\"slug\":\"prymorsky\",\"rus\":\"Приморский район\"},{\"slug\":\"chernihivsky\",\"rus\":\"Черниговский район\"},{\"slug\":\"vasylivsky\",\"rus\":\"Васильевский район\"},{\"slug\":\"enerhodar\",\"rus\":\"Энергодар\"},{\"slug\":\"kamiansko-dniprovsky\",\"rus\":\"Каменско-Днепровский район\"},{\"slug\":\"mykhailivsky\",\"rus\":\"Михайловский район\"},{\"slug\":\"zaporizky\",\"rus\":\"Запорожский район\"},{\"slug\":\"vilniansky\",\"rus\":\"Вольнянский район\"},{\"slug\":\"novomykolaivsky\",\"rus\":\"Новомиколаевский район\"},{\"slug\":\"melitopolsky\",\"rus\":\"Мелитопольский район\"},{\"slug\":\"veselivsky\",\"rus\":\"Веселовский район\"},{\"slug\":\"priazovsky\",\"rus\":\"Приазовский район\"},{\"slug\":\"iakymivsky\",\"rus\":\"Якимовский район\"},{\"slug\":\"pereiaslavsky\",\"rus\":\"Приазовский район\"},{\"slug\":\"pologivsky\",\"rus\":\"Пологовский район\"},{\"slug\":\"bilomatsky\",\"rus\":\"Бильмакский район\"},{\"slug\":\"huliaypilsky\",\"rus\":\"Гуляйпольский район\"},{\"slug\":\"orikhivsky\",\"rus\":\"Ореховский район\"},{\"slug\":\"rozivsky\",\"rus\":\"Розовский район\"},{\"slug\":\"tokmatsky\",\"rus\":\"Токмакский район\"}],\"ivano-frankivsk\":[{\"slug\":\"verkhovynsky\",\"rus\":\"Верховинский район\"},{\"slug\":\"ivano-frankivsky\",\"rus\":\"Ивано-Франковский район\"},{\"slug\":\"bohorodchansky\",\"rus\":\"Богородчанский район\"},{\"slug\":\"halytsky\",\"rus\":\"Галицкий район\"},{\"slug\":\"rohatynsky\",\"rus\":\"Рогатинский район\"},{\"slug\":\"tysmenytsky\",\"rus\":\"Тысменицкий район\"},{\"slug\":\"tlumatsky\",\"rus\":\"Тлумацкий район\"},{\"slug\":\"kalusky\",\"rus\":\"Калуский район\"},{\"slug\":\"dolynsky\",\"rus\":\"Долинский район\"},{\"slug\":\"rozhnyativsky\",\"rus\":\"Рожнятовский район\"},{\"slug\":\"kolomyisky\",\"rus\":\"Коломыйский район\"},{\"slug\":\"horodenkivsky\",\"rus\":\"Городенковский район\"},{\"slug\":\"sniatynsky\",\"rus\":\"Снятынский район\"},{\"slug\":\"kosivsky\",\"rus\":\"Косовский район\"},{\"slug\":\"nadvirnyansky\",\"rus\":\"Надворнянский район\"},{\"slug\":\"vorokhta\",\"rus\":\"Ворохта\"},{\"slug\":\"delyatyn\",\"rus\":\"Делятин\"},{\"slug\":\"yaremche\",\"rus\":\"Яремче\"}],\"kyiv\":[{\"slug\":\"bilotserkivsky\",\"rus\":\"Белоцерковский район\"},{\"slug\":\"volodarsky\",\"rus\":\"Володарский район\"},{\"slug\":\"rokytnyansky\",\"rus\":\"Ракитнянский район\"},{\"slug\":\"skvyrsky\",\"rus\":\"Сквирский район\"},{\"slug\":\"stavishchensky\",\"rus\":\"Ставищенский район\"},{\"slug\":\"tarashchansky\",\"rus\":\"Таращанский район\"},{\"slug\":\"boryspilsky\",\"rus\":\"Бориспольский район\"},{\"slug\":\"peryaslavsky\",\"rus\":\"Переяславский район\"},{\"slug\":\"yahotynsky\",\"rus\":\"Яготинский район\"},{\"slug\":\"brovarsky\",\"rus\":\"Броварский район\"},{\"slug\":\"baryshivsky\",\"rus\":\"Барышевский район\"},{\"slug\":\"zghurivsky\",\"rus\":\"Згуровский район\"},{\"slug\":\"buchansky\",\"rus\":\"Бучанский район\"},{\"slug\":\"borodyansky\",\"rus\":\"Бородянский район\"},{\"slug\":\"vyshneve\",\"rus\":\"Вышневое\"},{\"slug\":\"irpinsky\",\"rus\":\"Ирпенский район\"},{\"slug\":\"makarivsky\",\"rus\":\"Макаровский район\"},{\"slug\":\"vyshhorodsky\",\"rus\":\"Вышгородский район\"},{\"slug\":\"ivankivsky\",\"rus\":\"Иванковский район\"},{\"slug\":\"polissky\",\"rus\":\"Полесский район\"},{\"slug\":\"obukhivsky\",\"rus\":\"Обуховский район\"},{\"slug\":\"bohuslavsky\",\"rus\":\"Богуславский район\"},{\"slug\":\"vasylkivsky\",\"rus\":\"Васильковский район\"},{\"slug\":\"kaharlytsky\",\"rus\":\"Кагарлыкский район\"},{\"slug\":\"myronivsky\",\"rus\":\"Мироновский район\"},{\"slug\":\"rzhyshchivsky\",\"rus\":\"Ржищевский район\"},{\"slug\":\"fastivsky\",\"rus\":\"Фастовский район\"},{\"slug\":\"boiarka\",\"rus\":\"Боярка\"},{\"slug\":\"kalynivsky\",\"rus\":\"Калиновка\"},{\"slug\":\"hlevakha\",\"rus\":\"Глеваха\"}],\"kropyvnytskyi\":[{\"slug\":\"holovanivsky\",\"rus\":\"Голованевский район\"},{\"slug\":\"blahovishchensky\",\"rus\":\"Благовещенский район\"},{\"slug\":\"vilshansky\",\"rus\":\"Ольшанский район\"},{\"slug\":\"haivoronsky\",\"rus\":\"Гайворонский район\"},{\"slug\":\"novoarkhanhelsky\",\"rus\":\"Новоархангельский район\"},{\"slug\":\"kropyvnytskyi\",\"rus\":\"Кропивницкий район\"},{\"slug\":\"bobrynetsky\",\"rus\":\"Бобринецкий район\"},{\"slug\":\"dolynsky\",\"rus\":\"Долинский район\"},{\"slug\":\"znamiansky\",\"rus\":\"Знаменский район\"},{\"slug\":\"kompaniivsky\",\"rus\":\"Компанеевский район\"},{\"slug\":\"novhorodkivsky\",\"rus\":\"Новгородковский район\"},{\"slug\":\"oleksandrivskyi\",\"rus\":\"Александрийский район\"},{\"slug\":\"novoukrainsky\",\"rus\":\"Александрия\"},{\"slug\":\"dobrovelychkivsky\",\"rus\":\"Новоукраинский район\"},{\"slug\":\"malovyskivsky\",\"rus\":\"Добровеличковский район\"},{\"slug\":\"novomyrhorodsky\",\"rus\":\"Маловискевский район\"},{\"slug\":\"oleksandriia\",\"rus\":\"Новомиргородский район\"},{\"slug\":\"onufriivsky\",\"rus\":\"Онуфриевский район\"},{\"slug\":\"petrivsky\",\"rus\":\"Петровский район\"},{\"slug\":\"svitlovodsky\",\"rus\":\"Светловодский район\"}],\"lugansk\":[{\"slug\":\"alchevskiy\",\"rus\":\"Алчевский район\"},{\"slug\":\"kadiivka\",\"rus\":\"Кадиевка\"},{\"slug\":\"zimogirivskiy\",\"rus\":\"Зимогорьевский\"},{\"slug\":\"dovzhanskiy\",\"rus\":\"Довжанский район\"},{\"slug\":\"luganskiy\",\"rus\":\"Луганский район\"},{\"slug\":\"lutugino\",\"rus\":\"Лутугино\"},{\"slug\":\"molodogvardiysk\",\"rus\":\"Молодогвардейск\"},{\"slug\":\"rovenkovskiy\",\"rus\":\"Ровеньковский район\"},{\"slug\":\"antratsit\",\"rus\":\"Антрацит\"},{\"slug\":\"rovenki\",\"rus\":\"Ровеньки\"},{\"slug\":\"khrustalniy\",\"rus\":\"Хрустальный\"},{\"slug\":\"svativskiy\",\"rus\":\"Сватовский район\"},{\"slug\":\"bilokurakinskiy\",\"rus\":\"Белокуракинский район\"},{\"slug\":\"severodonetsk\",\"rus\":\"Северодонецкий район\"},{\"slug\":\"gorskoe\",\"rus\":\"Горское\"},{\"slug\":\"kreminskiy\",\"rus\":\"Кременский район\"},{\"slug\":\"lisichansk\",\"rus\":\"Лисичанск\"},{\"slug\":\"popasnyanskiy\",\"rus\":\"Попаснянский район\"},{\"slug\":\"rubizhne\",\"rus\":\"Рубежное\"},{\"slug\":\"starobilskiy\",\"rus\":\"Старобельский район\"},{\"slug\":\"bilovodskiy\",\"rus\":\"Беловодский район\"},{\"slug\":\"markivskiy\",\"rus\":\"Марковский район\"},{\"slug\":\"milovskiy\",\"rus\":\"Миловский район\"},{\"slug\":\"novopskovskiy\",\"rus\":\"Новопсковский район\"},{\"slug\":\"shchastinskiy\",\"rus\":\"Щастинский район\"},{\"slug\":\"stanichno-luganskiy\",\"rus\":\"Станично-Луганский район\"},{\"slug\":\"shchastie\",\"rus\":\"Щастя\"},{\"slug\":\"novoaydar\",\"rus\":\"Новоайдар\"}],\"lviv\":[{\"slug\":\"drohobychskyi\",\"rus\":\"Дрогобычский район\"},{\"slug\":\"borislav\",\"rus\":\"Борислав\"},{\"slug\":\"skhidnytsia\",\"rus\":\"Схидница\"},{\"slug\":\"truskavets\",\"rus\":\"Трускавец\"},{\"slug\":\"zolochivskyi\",\"rus\":\"Золочевский район\"},{\"slug\":\"brody\",\"rus\":\"Броды\"},{\"slug\":\"busk\",\"rus\":\"Буск\"},{\"slug\":\"pidkamin\",\"rus\":\"Подкамень\"},{\"slug\":\"pomoriany\",\"rus\":\"Поморяны\"},{\"slug\":\"lvivskyi\",\"rus\":\"Львовский район\"},{\"slug\":\"bibirka\",\"rus\":\"Бибрка\"},{\"slug\":\"horodok\",\"rus\":\"Городок\"},{\"slug\":\"zhovkva\",\"rus\":\"Жолковский район\"},{\"slug\":\"kamianka-buzka\",\"rus\":\"Каменка-Бугская\"},{\"slug\":\"peremyshliany\",\"rus\":\"Перемышляны\"},{\"slug\":\"pustomyty\",\"rus\":\"Пустомыты\"},{\"slug\":\"rava-ruska\",\"rus\":\"Рава-Русская\"},{\"slug\":\"sambirskyi\",\"rus\":\"Самборский район\"},{\"slug\":\"dobromyl\",\"rus\":\"Добромиль\"},{\"slug\":\"starosambir\",\"rus\":\"Старый Самбор\"},{\"slug\":\"khyriv\",\"rus\":\"Хыров\"},{\"slug\":\"stryiskyi\",\"rus\":\"Стрыйский район\"},{\"slug\":\"zhidachiv\",\"rus\":\"Жидачов\"},{\"slug\":\"mykolaiv\",\"rus\":\"Николаев\"},{\"slug\":\"morshyn\",\"rus\":\"Моршин\"},{\"slug\":\"skole\",\"rus\":\"Сколе\"},{\"slug\":\"slavske\",\"rus\":\"Славское\"},{\"slug\":\"khodoriv\",\"rus\":\"Ходоров\"},{\"slug\":\"chervonohradskyi\",\"rus\":\"Червоноградский район\"},{\"slug\":\"belz\",\"rus\":\"Белз\"},{\"slug\":\"radekhiv\",\"rus\":\"Радехов\"},{\"slug\":\"sokal\",\"rus\":\"Сокаль\"},{\"slug\":\"yavorivskyi\",\"rus\":\"Яворовский район\"},{\"slug\":\"mostyska\",\"rus\":\"Мостиска\"},{\"slug\":\"novoyavorivsk\",\"rus\":\"Новояворовск\"},{\"slug\":\"sudova-vyshnia\",\"rus\":\"Судовая Вишня\"},{\"slug\":\"shehyni\",\"rus\":\"Шегини\"}],\"mykolaiv\":[{\"slug\":\"bashtanskyi\",\"rus\":\"Баштанский район\"},{\"slug\":\"berezneguvatskyi\",\"rus\":\"Березнегуватский район\"},{\"slug\":\"novobuzkyi\",\"rus\":\"Новобугский район\"},{\"slug\":\"snihurivskyi\",\"rus\":\"Снигиревский район\"},{\"slug\":\"voznesenskyi\",\"rus\":\"Вознесенский район\"},{\"slug\":\"bratskyi\",\"rus\":\"Братский район\"},{\"slug\":\"veselynivskyi\",\"rus\":\"Веселиновский район\"},{\"slug\":\"domanivskyi\",\"rus\":\"Доманевский район\"},{\"slug\":\"yelanetskyi\",\"rus\":\"Еланецкий район\"},{\"slug\":\"yuzhnyoukrainsk\",\"rus\":\"Южноукраинск\"},{\"slug\":\"mykolaivskyi\",\"rus\":\"Николаевский район\"},{\"slug\":\"berezhanskyi\",\"rus\":\"Березанский район\"},{\"slug\":\"novoodeskyi\",\"rus\":\"Новоодесский район\"},{\"slug\":\"ochakivskyi\",\"rus\":\"Очаковский район\"},{\"slug\":\"perwomayskyi\",\"rus\":\"Первомайский район\"},{\"slug\":\"arbuzynskyi\",\"rus\":\"Арбузинский район\"},{\"slug\":\"vradiyivskyi\",\"rus\":\"Врадиевский район\"},{\"slug\":\"kryvoozerskyi\",\"rus\":\"Кривоозерский район\"}],\"odesa\":[{\"slug\":\"berezivskyi\",\"rus\":\"Березовский район\"},{\"slug\":\"mykolaivskyi\",\"rus\":\"Николаевский район\"},{\"slug\":\"shyriaiivskyi\",\"rus\":\"Ширяевский район\"},{\"slug\":\"bilhorod-dnistrovskyi\",\"rus\":\"Белгород-Днестровский район\"},{\"slug\":\"saratovskyi\",\"rus\":\"Саратский район\"},{\"slug\":\"tatarbunarskyi\",\"rus\":\"Татарбунарский район\"},{\"slug\":\"bolhradskyi\",\"rus\":\"Болградский район\"},{\"slug\":\"artsyzkyi\",\"rus\":\"Арцизский район\"},{\"slug\":\"tarutynskyi\",\"rus\":\"Тарутинский район\"},{\"slug\":\"izmailskyi\",\"rus\":\"Измаильский район\"},{\"slug\":\"vylkove\",\"rus\":\"Вилково\"},{\"slug\":\"kiliiskyi\",\"rus\":\"Килийский район\"},{\"slug\":\"reniyskyi\",\"rus\":\"Ренийский район\"},{\"slug\":\"odeskyi\",\"rus\":\"Одесский район\"},{\"slug\":\"biliaivskyi\",\"rus\":\"Беляевский район\"},{\"slug\":\"ovidiopolskyi\",\"rus\":\"Овидиопольский район\"},{\"slug\":\"chornomorskyi\",\"rus\":\"Черноморский район\"},{\"slug\":\"yuzhne\",\"rus\":\"Южное\"},{\"slug\":\"podilskyi\",\"rus\":\"Подольский район\"},{\"slug\":\"ananiivskyi\",\"rus\":\"Ананьевский район\"},{\"slug\":\"baltskyi\",\"rus\":\"Балтский район\"},{\"slug\":\"kodymskyi\",\"rus\":\"Кодымский район\"},{\"slug\":\"liubashivskyi\",\"rus\":\"Любашевский район\"},{\"slug\":\"oknianskyi\",\"rus\":\"Окнянский район\"},{\"slug\":\"savranskyi\",\"rus\":\"Савранский район\"},{\"slug\":\"rozdilnianskyi\",\"rus\":\"Раздельнянский район\"},{\"slug\":\"velykomikhaylivskyi\",\"rus\":\"Великомихайловский район\"},{\"slug\":\"zakhariivskyi\",\"rus\":\"Захарьевский район\"},{\"slug\":\"lymanskyi\",\"rus\":\"Лиманский район\"}],\"poltava\":[{\"slug\":\"poltava\",\"rus\":\"Полтавский район\"},{\"slug\":\"dikanka\",\"rus\":\"Диканка\"},{\"slug\":\"zinkiv\",\"rus\":\"Зеньковский район\"},{\"slug\":\"karlivka\",\"rus\":\"Карловка\"},{\"slug\":\"kobelyaki\",\"rus\":\"Кобеляки\"},{\"slug\":\"kotelva\",\"rus\":\"Котельва\"},{\"slug\":\"novi-sanzhary\",\"rus\":\"Новые Санжары\"},{\"slug\":\"opishnya\",\"rus\":\"Опошня\"},{\"slug\":\"reshetylivka\",\"rus\":\"Решетиловка\"},{\"slug\":\"kremenchuk\",\"rus\":\"Кременчугский район\"},{\"slug\":\"globino\",\"rus\":\"Глобино\"},{\"slug\":\"gorishni-plavni\",\"rus\":\"Горишние Плавни\"},{\"slug\":\"mirgorod\",\"rus\":\"Миргородский район\"},{\"slug\":\"gadyach\",\"rus\":\"Гадяч\"},{\"slug\":\"gogolivske\",\"rus\":\"Гоголевское\"},{\"slug\":\"lokhvytsia\",\"rus\":\"Лохвица\"},{\"slug\":\"romodanivske\",\"rus\":\"Ромодановское\"},{\"slug\":\"shishaki\",\"rus\":\"Шишаки\"},{\"slug\":\"lubny\",\"rus\":\"Лубенский район\"},{\"slug\":\"hrebinka\",\"rus\":\"Гребенка\"},{\"slug\":\"pyryatyn\",\"rus\":\"Пирятин\"},{\"slug\":\"khorol\",\"rus\":\"Хорол\"},{\"slug\":\"chornukhine\",\"rus\":\"Чернухино\"}],\"rivne\":[{\"slug\":\"varasskiy\",\"rus\":\"Варашский район\"},{\"slug\":\"vladimiretskiy\",\"rus\":\"Владимирецкий район\"},{\"slug\":\"zarichnenskiy\",\"rus\":\"Заречненский район\"},{\"slug\":\"dubenskiy\",\"rus\":\"Дубенский район\"},{\"slug\":\"demidovskiy\",\"rus\":\"Демидовский район\"},{\"slug\":\"mlinovskiy\",\"rus\":\"Млиновский район\"},{\"slug\":\"radivilovskiy\",\"rus\":\"Радивиловский район\"},{\"slug\":\"rivenskiy\",\"rus\":\"Ровенский район\"},{\"slug\":\"bereznovskiy\",\"rus\":\"Березновский район\"},{\"slug\":\"goshchanskiy\",\"rus\":\"Гощанский район\"},{\"slug\":\"zdolbunovskiy\",\"rus\":\"Здолбуновский район\"},{\"slug\":\"klevan\",\"rus\":\"Клевань\"},{\"slug\":\"koretskiy\",\"rus\":\"Корецкий район\"},{\"slug\":\"kostopilskiy\",\"rus\":\"Костопольский район\"},{\"slug\":\"ostrogskiy\",\"rus\":\"Острожский район\"},{\"slug\":\"sarnenskiy\",\"rus\":\"Сарненский район\"},{\"slug\":\"dubrovitskiy\",\"rus\":\"Дубровицкий район\"},{\"slug\":\"rokitnovskiy\",\"rus\":\"Рокитновский район\"},{\"slug\":\"klesiv\",\"rus\":\"Клесов\"}],\"sumy\":[{\"slug\":\"konotopskyi\",\"rus\":\"Конотопский район\"},{\"slug\":\"burynskyi\",\"rus\":\"Буринский район\"},{\"slug\":\"krolevetskyi\",\"rus\":\"Кролевецкий район\"},{\"slug\":\"putivlskyi\",\"rus\":\"Путивльский район\"},{\"slug\":\"okhtyrskyi\",\"rus\":\"Ахтырский район\"},{\"slug\":\"velykopysarivskyi\",\"rus\":\"Великописаревский район\"},{\"slug\":\"trostyanetskyi\",\"rus\":\"Тростянецкий район\"},{\"slug\":\"romenskyi\",\"rus\":\"Роменский район\"},{\"slug\":\"lypovodynskyi\",\"rus\":\"Липоводолинский район\"},{\"slug\":\"nedryhaylivskyi\",\"rus\":\"Недригайловский район\"},{\"slug\":\"sumskyi\",\"rus\":\"Сумский район\"},{\"slug\":\"bilopilskyi\",\"rus\":\"Белопольский район\"},{\"slug\":\"krasnopilskyi\",\"rus\":\"Краснопольский район\"},{\"slug\":\"lebedynskyi\",\"rus\":\"Лебединский район\"},{\"slug\":\"shostkinskyi\",\"rus\":\"Шосткинский район\"},{\"slug\":\"hlukhivskyi\",\"rus\":\"Глуховский район\"},{\"slug\":\"seredynobudskyi\",\"rus\":\"Середино-Будский район\"},{\"slug\":\"yampilskyi\",\"rus\":\"Ямпольский район\"}],\"ternopil\":[{\"slug\":\"kremenetskyy\",\"rus\":\"Кременецкий район\"},{\"slug\":\"lanovetskyy\",\"rus\":\"Лановецкий район\"},{\"slug\":\"pochaiv\",\"rus\":\"город Почаев\"},{\"slug\":\"shumskyy\",\"rus\":\"Шумский район\"},{\"slug\":\"ternopilskyy\",\"rus\":\"Тернопольский район\"},{\"slug\":\"berezhanskyy\",\"rus\":\"Бережанский район\"},{\"slug\":\"zbarazkyy\",\"rus\":\"Збаражский район\"},{\"slug\":\"zborivskyy\",\"rus\":\"Зборовский район\"},{\"slug\":\"pidvolochyskyy\",\"rus\":\"Подволочиский район\"},{\"slug\":\"pidhayetskyy\",\"rus\":\"Подгаецкий район\"},{\"slug\":\"terebovlyanskyy\",\"rus\":\"Теребовлянский район\"},{\"slug\":\"chortkivskyy\",\"rus\":\"Чортковский район\"},{\"slug\":\"borschivskyy\",\"rus\":\"Борщевский район\"},{\"slug\":\"buchatskyy\",\"rus\":\"Бучачский район\"},{\"slug\":\"husyatynskyy\",\"rus\":\"Гусятинский район\"},{\"slug\":\"zalishchytskyy\",\"rus\":\"Залещицкий район\"},{\"slug\":\"monastyryskyy\",\"rus\":\"Монастыриский район\"}],\"kharkiv\":[{\"slug\":\"bogodukhovskiy\",\"rus\":\"Богодуховский район\"},{\"slug\":\"valkovskiy\",\"rus\":\"Валковский район\"},{\"slug\":\"zolochevskiy\",\"rus\":\"Золочевский район\"},{\"slug\":\"kolomatskiy\",\"rus\":\"Коломацкий район\"},{\"slug\":\"krasnokutskiy\",\"rus\":\"Краснокутский район\"},{\"slug\":\"izyumskiy\",\"rus\":\"Изюмский район\"},{\"slug\":\"balakliyskiy\",\"rus\":\"Балаклейский район\"},{\"slug\":\"barvinkovskiy\",\"rus\":\"Барвинковский район\"},{\"slug\":\"borovskiy\",\"rus\":\"Боровский район\"},{\"slug\":\"krasnogradskiy\",\"rus\":\"Красноградский район\"},{\"slug\":\"zachepilovskiy\",\"rus\":\"Зачепиловский район\"},{\"slug\":\"kegichevskiy\",\"rus\":\"Кегичевский район\"},{\"slug\":\"sakhnovshchinskiy\",\"rus\":\"Сахновщинский район\"},{\"slug\":\"kupyanskiy\",\"rus\":\"Купянский район\"},{\"slug\":\"velikoburlukskiy\",\"rus\":\"Великобурлукский район\"},{\"slug\":\"dvorichanskiy\",\"rus\":\"Двуречанский район\"},{\"slug\":\"shevchenkovskiy\",\"rus\":\"Шевченковский район\"},{\"slug\":\"lozovskiy\",\"rus\":\"Лозовский район\"},{\"slug\":\"blyznyukovskiy\",\"rus\":\"Близнюковский район\"},{\"slug\":\"permyakovskiy\",\"rus\":\"Первомайский район\"},{\"slug\":\"kharkovskiy\",\"rus\":\"Харьковский район\"},{\"slug\":\"dergachovskiy\",\"rus\":\"Дергачевский район\"},{\"slug\":\"lyubotinskiy\",\"rus\":\"Люботинский район\"},{\"slug\":\"novovodolazhskiy\",\"rus\":\"Нововодолажский район\"},{\"slug\":\"chuguevskiy\",\"rus\":\"Чугуевский район\"},{\"slug\":\"vovchanskiy\",\"rus\":\"Волчанский район\"},{\"slug\":\"zmiyevskiy\",\"rus\":\"Змиевский район\"},{\"slug\":\"pechenizhskiy\",\"rus\":\"Печенежский район\"}],\"kherson\":[{\"slug\":\"berislavskiy\",\"rus\":\"Бериславский район\"},{\"slug\":\"velikooleksandrovskiy\",\"rus\":\"Великоалександровский район\"},{\"slug\":\"visokopilskiy\",\"rus\":\"Высокопильский район\"},{\"slug\":\"novovorontsovskiy\",\"rus\":\"Нововоронцовский район\"},{\"slug\":\"genicheskiy\",\"rus\":\"Генический район\"},{\"slug\":\"ivanovskiy\",\"rus\":\"Ивановский район\"},{\"slug\":\"nizhnosirogozskiy\",\"rus\":\"Нижнесирогозский район\"},{\"slug\":\"novotroitskiy\",\"rus\":\"Новотроицкий район\"},{\"slug\":\"kakhovskiy\",\"rus\":\"Каховский район\"},{\"slug\":\"velikolepetiskiy\",\"rus\":\"Великолепетихский район\"},{\"slug\":\"verkhnerogachitskiy\",\"rus\":\"Верхнерогачицкий район\"},{\"slug\":\"gornostayivskiy\",\"rus\":\"Горностаивский район\"},{\"slug\":\"chaplynskiy\",\"rus\":\"Чаплинский район\"},{\"slug\":\"nova-kakhovka\",\"rus\":\"Новая Каховка\"},{\"slug\":\"skadovskiy\",\"rus\":\"Скадовский район\"},{\"slug\":\"golopristanskiy\",\"rus\":\"Голопристанский район\"},{\"slug\":\"kalanchatskiy\",\"rus\":\"Каланчакский район\"},{\"slug\":\"khersonskiy\",\"rus\":\"Херсонский район\"},{\"slug\":\"bilozeretskiy\",\"rus\":\"Белозерский район\"},{\"slug\":\"oleshkovskiy\",\"rus\":\"Олешковский район\"}],\"khmelnytskyi\":[{\"slug\":\"kamyanets-podilskyy\",\"rus\":\"Каменец-Подольский район\"},{\"slug\":\"dunayevetskyy\",\"rus\":\"Дунаевецкий район\"},{\"slug\":\"novoushytskyy\",\"rus\":\"Новоушицкий район\"},{\"slug\":\"chemerovetskyy\",\"rus\":\"Чемеровецкий район\"},{\"slug\":\"khmelnytskyy\",\"rus\":\"Хмельницкий район\"},{\"slug\":\"vinkovetskyy\",\"rus\":\"Виньковецкий район\"},{\"slug\":\"volochyskyy\",\"rus\":\"Волочиский район\"},{\"slug\":\"horodotskyy\",\"rus\":\"Городоцкий район\"},{\"slug\":\"derazhnyanskyy\",\"rus\":\"Деражнянский район\"},{\"slug\":\"krasylivskyy\",\"rus\":\"Красиловский район\"},{\"slug\":\"letychivskyy\",\"rus\":\"Летичевский район\"},{\"slug\":\"starokostyantynivskyy\",\"rus\":\"Староконстантиновский район\"},{\"slug\":\"starosyniavskyy\",\"rus\":\"Старосинявский район\"},{\"slug\":\"teofipolksyy\",\"rus\":\"Теофипольский район\"},{\"slug\":\"yarmolyntsiyskyy\",\"rus\":\"Ярмолинецкий район\"},{\"slug\":\"shepetivskyy\",\"rus\":\"Шепетовский район\"},{\"slug\":\"bilohirskyy\",\"rus\":\"Белогорский район\"},{\"slug\":\"izyaslavskyy\",\"rus\":\"Изяславский район\"},{\"slug\":\"slavutskyy\",\"rus\":\"Славутский район\"},{\"slug\":\"netishyn\",\"rus\":\"Нетешин\"}],\"cherkasy\":[{\"slug\":\"zvenigorodskyy\",\"rus\":\"Звенигородский район\"},{\"slug\":\"vatutine\",\"rus\":\"Ватутино\"},{\"slug\":\"katerynopilskyy\",\"rus\":\"Катеринопольский район\"},{\"slug\":\"lysianskyy\",\"rus\":\"Лысянский район\"},{\"slug\":\"talnivskyy\",\"rus\":\"Тальновский район\"},{\"slug\":\"shpolyanskyy\",\"rus\":\"Шполянский район\"},{\"slug\":\"zolotonoshskyy\",\"rus\":\"Золотоношский район\"},{\"slug\":\"drabivskyy\",\"rus\":\"Драбовский район\"},{\"slug\":\"chornobayivskyy\",\"rus\":\"Чернобаевский район\"},{\"slug\":\"umanskyy\",\"rus\":\"Уманский район\"},{\"slug\":\"zhashkivskyy\",\"rus\":\"Жашковский район\"},{\"slug\":\"mankivskyy\",\"rus\":\"Маньковский район\"},{\"slug\":\"monastyryshchenskyy\",\"rus\":\"Монастырищенский район\"},{\"slug\":\"khrystynivskyy\",\"rus\":\"Христиновский район\"},{\"slug\":\"cherkaskyy\",\"rus\":\"Черкасский район\"},{\"slug\":\"horodyshchenskyy\",\"rus\":\"Городищенский район\"},{\"slug\":\"kamyanetskyy\",\"rus\":\"Каменский район\"},{\"slug\":\"kanivskyy\",\"rus\":\"Каневский район\"},{\"slug\":\"korsun-shevchenkivskyy\",\"rus\":\"Корсунь-Шевченковский район\"},{\"slug\":\"smilyanskyy\",\"rus\":\"Смелянский район\"},{\"slug\":\"chyhyrynskyy\",\"rus\":\"Чигиринский район\"}],\"chernihiv\":[{\"slug\":\"koriukivskyy\",\"rus\":\"Корюковский район\"},{\"slug\":\"menskyy\",\"rus\":\"Менский район\"},{\"slug\":\"snovskyy\",\"rus\":\"Сновский район\"},{\"slug\":\"sosnytskyy\",\"rus\":\"Сосницкий район\"},{\"slug\":\"nizhynskyy\",\"rus\":\"Нежинский район\"},{\"slug\":\"baturyn\",\"rus\":\"Батурин\"},{\"slug\":\"bakhmatskyy\",\"rus\":\"Бахмацкий район\"},{\"slug\":\"bobrovytskyy\",\"rus\":\"Бобровицкий район\"},{\"slug\":\"borznyanskyy\",\"rus\":\"Борзнянский район\"},{\"slug\":\"nosivskyy\",\"rus\":\"Носовский район\"},{\"slug\":\"novhorod-siverskyy\",\"rus\":\"Новгород-Северский район\"},{\"slug\":\"koropskyy\",\"rus\":\"Коропский район\"},{\"slug\":\"semenivskyy\",\"rus\":\"Семеновский район\"},{\"slug\":\"prylutskyy\",\"rus\":\"Прилуцкий район\"},{\"slug\":\"varvynskyy\",\"rus\":\"Варвинский район\"},{\"slug\":\"ichnyanskyy\",\"rus\":\"Ичнянский район\"},{\"slug\":\"sribnyanskyy\",\"rus\":\"Сребнянский район\"},{\"slug\":\"talalaivskyy\",\"rus\":\"Талалаевский район\"},{\"slug\":\"chernihivskyy\",\"rus\":\"Черниговский район\"},{\"slug\":\"horodnyanskyy\",\"rus\":\"Городнянский район\"},{\"slug\":\"kozeletskyy\",\"rus\":\"Козелецкий район\"},{\"slug\":\"kulikivskyy\",\"rus\":\"Куликовский район\"},{\"slug\":\"ripkynskyy\",\"rus\":\"Репкинский район\"}],\"chernivtsi\":[{\"slug\":\"vyzhnytskyy\",\"rus\":\"Вижницкий район\"},{\"slug\":\"putylskyy\",\"rus\":\"Путильский район\"},{\"slug\":\"dnistrovskyy\",\"rus\":\"Днестровский район\"},{\"slug\":\"novodnistrovsk\",\"rus\":\"Новоднестровск\"},{\"slug\":\"sokyryanskyy\",\"rus\":\"Сокирянский район\"},{\"slug\":\"khotynskyy\",\"rus\":\"Хотинский район\"},{\"slug\":\"chernivetskyy\",\"rus\":\"Черновецкий район\"},{\"slug\":\"gertsaivskyy\",\"rus\":\"Герцаевский район\"},{\"slug\":\"glybotskyy\",\"rus\":\"Глыбоцкий район\"},{\"slug\":\"zastavnivskyy\",\"rus\":\"Заставневский район\"},{\"slug\":\"kitsmanskyy\",\"rus\":\"Кицманский район\"},{\"slug\":\"novoselytskyy\",\"rus\":\"Новоселицкий район\"},{\"slug\":\"storozhynetskyy\",\"rus\":\"Сторожинецкий район\"}],\"krym\":[{\"slug\":\"bakhchisarayskiy\",\"rus\":\"Бахчисарайский район\"},{\"slug\":\"belogorskiy\",\"rus\":\"Белогорский район\"},{\"slug\":\"nizhnegorskiy\",\"rus\":\"Нижнегорский район\"},{\"slug\":\"dzhankoy\",\"rus\":\"Джанко́йский район\"},{\"slug\":\"evpatoriyskiy\",\"rus\":\"Евпаторийский район\"},{\"slug\":\"chernomorskiy\",\"rus\":\"Черноморский район\"},{\"slug\":\"sakiyskiy\",\"rus\":\"Сакский район\"},{\"slug\":\"kerchenskiy\",\"rus\":\"Керченский район\"},{\"slug\":\"edykuyskiy\",\"rus\":\"Едикуйский район\"},{\"slug\":\"kurmanskiy\",\"rus\":\"Курманский район\"},{\"slug\":\"pervomayskiy\",\"rus\":\"Первомайский район\"},{\"slug\":\"perekopskiy\",\"rus\":\"Перекопский район\"},{\"slug\":\"rozdolnenskiy\",\"rus\":\"Раздольненский район\"},{\"slug\":\"simferopolskiy\",\"rus\":\"Симферопольский район\"},{\"slug\":\"feodosiyskiy\",\"rus\":\"Феодосийский район\"},{\"slug\":\"sudak\",\"rus\":\"Судак\"},{\"slug\":\"feodosiya\",\"rus\":\"Феодосия\"},{\"slug\":\"islamteretskiy\",\"rus\":\"Ислям-Терекский район\"},{\"slug\":\"ichkinskiy\",\"rus\":\"Ичкинский район\"},{\"slug\":\"yalta\",\"rus\":\"Ялтинский район\"},{\"slug\":\"alushta\",\"rus\":\"Алушта\"}]}\n";
        ObjectMapper mapper = new ObjectMapper();
        ourOblRegHashmap = mapper.readValue(json, new TypeReference<HashMap<String, List<Region>>>(){});
    }
    public void start(){
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.idcompass.com/?lang=ru&section=base");
        Cookie cookie = new Cookie("PHPSESSID", "8a9173a0139e2af65c21ea36a63376c4");
        driver.manage().addCookie(cookie);
        for (int i=0; i<25; i++) {
            WebElement el = driver.findElements(By.cssSelector("#regionsList .regionsTable tbody tr a")).get(i);
            String rez = el.getDomAttribute("href");
            int charPos = rez.lastIndexOf("=");
            String obl = rez.substring(charPos + 1);
            System.out.println("********************************************* oblast " + obl);
            el.click();
            this.getOblastsToHashMap();
            List<WebElement> regionsElements = driver.findElements(By.cssSelector(".district a"));
            for (int j=0; j<regionsElements.size(); j++) {
                WebElement regClick = driver.findElements(By.cssSelector(".district a")).get(j);
                String reg = regClick.getText();
                if (obl.equals("zakarpattia") && reg.equals("Чоп")) reg = "Чоп";
                if (obl.equals("chernivtsi") && reg.equals("Кельменецкий район")) reg = "Днестровский район";
                if (obl.equals("khmelnytskyi") && reg.equals("Полонский район")) reg = "Шепетовский район";
                if (obl.equals("ternopil") && reg.equals("Козовский район")) reg = "Тернопольский район";
                if (obl.equals("poltava") && reg.equals("Великобагачанский район")) reg = "Миргородский район";
                if (obl.equals("poltava") && reg.equals("Козельщинский район")) reg = "Кременчугский район";
                if (obl.equals("poltava") && reg.equals("Машевский район")) reg = "Полтавский район";
                if (obl.equals("poltava") && reg.equals("Оржицкий район")) reg = "Лубенский район";
                if (obl.equals("poltava") && reg.equals("Семёновский район")) reg = "Кременчугский район";
                if (obl.equals("poltava") && reg.equals("Чутовский район")) reg = "Полтавский район";
                if (obl.equals("odessa") && reg.equals("Ивановский район")) reg = "Березовский район";
                if (obl.equals("odessa") && reg.equals("Коминтерновский район")) reg = "Лиманский район";
                if (obl.equals("odessa") && reg.equals("Котовский район")) reg = "Подольский район";
                if (obl.equals("odessa") && reg.equals("Красноокнянский район")) reg = "Окнянский район";
                if (obl.equals("odessa") && reg.equals("Фрунзовский район")) reg = "Захарьевский район";
                if (obl.equals("mykolaiv") && reg.equals("Витовский район")) reg = "Николаевский район";
                if (obl.equals("mykolaiv") && reg.equals("Казанковский район")) reg = "Баштанский район";
                if (obl.equals("lviv") && reg.equals("Турковский район")) reg = "Самборский район";
                if (obl.equals("luhansk") && reg.equals("Краснодонский район")) reg = "Луганский район";
                if (obl.equals("luhansk") && reg.equals("Меловской район")) reg = "Луганский район";
                if (obl.equals("luhansk") && reg.equals("Перевальский район")) reg = "Луганский район";
                if (obl.equals("luhansk") && reg.equals("Сєвєродонецький район")) reg = "Луганский район";
                if (obl.equals("luhansk") && reg.equals("Свердловский район")) reg = "Луганский район";
                if (obl.equals("luhansk") && reg.equals("Славяносербский район")) reg = "Луганский район";
                if (obl.equals("luhansk") && reg.equals("Троицкий район")) reg = "Луганский район";
                if (obl.equals("kirovohrad") && reg.equals("Кировоградский район")) reg = "Кропивницкий район";
                if (obl.equals("kirovohrad") && reg.equals("Устиновский район")) reg = "Кропивницкий район";
                if (obl.equals("kiev") && reg.equals("Тетиевский район")) reg = "Белоцерковский район";
                if (obl.equals("kiev") && reg.equals("Киево-Святошинский район")) reg = "Фастовский район";
                if (obl.equals("zaporizhia") && reg.equals("Великобелозёрский район")) reg = "Васильевский район";
                if (obl.equals("donetsk") && reg.equals("Першотравневый район")) reg = "Мангушский район";
                if (obl.equals("donetsk") && reg.equals("Тельмановский район")) reg = "Кальмиусский район";
                if (obl.equals("dnipropetrovsk") && reg.equals("Межевский район")) reg = "Синельниковский район";
                if (obl.equals("dnipropetrovsk") && reg.equals("Солонянский район")) reg = "Днепровский район";
                if (obl.equals("crimea") && reg.equals("Армянск")) reg = "Перекопский район";
                if (obl.equals("crimea") && reg.equals("Кировский район")) reg = "Феодосийский район";
                if (obl.equals("crimea") && reg.equals("Красногвардейский район")) reg = "Курманский район";
                if (obl.equals("crimea") && reg.equals("Красноперекопск")) reg = "Перекопский район";
                if (obl.equals("crimea") && reg.equals("Красноперекопский район")) reg = "Перекопский район";
                if (obl.equals("crimea") && reg.equals("Ленинский район")) reg = "Керченский район";
                if (obl.equals("crimea") && reg.equals("Советский район")) reg = "Феодосийский район";
                String oblToSend = ourOblToOblHashMap.get(obl);
                String regToSend = "";
                for (Map.Entry<String, List<Region>> entry : ourOblRegHashmap.entrySet()){
                    if (Objects.equals(oblToOurOblHashMap.get(entry.getKey()), obl)) {
                        for (Region region : entry.getValue()){
                            String subRegMy = region.getRus().substring(0, 3);
                            if (Objects.equals(region.getRus(), reg) || reg.startsWith(subRegMy)) {
                                for (Region r : ourOblRegHashmap.get(oblToSend)) {
                                    if (reg.startsWith(subRegMy)) {
                                        regToSend = r.getSlug();
                                    }
                                }
                            }
                        }
                    }
                }
                WebDriverWait wait= (new WebDriverWait(driver, Duration.ofSeconds(20)));
                wait.until(ExpectedConditions.elementToBeClickable(regClick));
                regClick.click();
                parseRegion(driver.getPageSource(), oblToSend, regToSend);
                driver.navigate().back();
            }
            driver.navigate().back();
        }
        driver.quit();
    }
    public void getOblastsToHashMap() {
        try {
            String filePath = "https://www.idcompass.com/?lang=ru&section=base";
            Document doc = Jsoup.connect(filePath).cookie("PHPSESSID", "8a9173a0139e2af65c21ea36a63376c4").get();
            Elements paragraphs = doc.select("#regionsList .regionsTable tbody tr");
            for (Element paragraph : paragraphs) {
                String rez = paragraph.select("a").attr("href");
                int charPos = rez.lastIndexOf("=");
                String substringAfterLastChar = rez.substring(charPos + 1);
                if (substringAfterLastChar.equals("ivanofrankivsk")){
                    oblToOurOblHashMap.put("ivano-frankivsk", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "ivano-frankivsk");
                } else if (substringAfterLastChar.equals("kiev")) {
                    oblToOurOblHashMap.put("kyiv", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "kyiv");
                } else if (substringAfterLastChar.equals("vinnytsia")) {
                    oblToOurOblHashMap.put("vinnycya", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "vinnycya");
                } else if (substringAfterLastChar.equals("crimea")) {
                    oblToOurOblHashMap.put("krym", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "krym");
                } else if (substringAfterLastChar.equals("dnipropetrovsk")) {
                    oblToOurOblHashMap.put("dnipro", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "dnipro");
                } else if (substringAfterLastChar.equals("zakarpattia")) {
                    oblToOurOblHashMap.put("zakarpattya", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "zakarpattya");
                } else if (substringAfterLastChar.equals("zaporizhia")) {
                    oblToOurOblHashMap.put("zaporizhzhya", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "zaporizhzhya");
                } else if (substringAfterLastChar.equals("kirovohrad")) {
                    oblToOurOblHashMap.put("kropyvnytskyi", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "kropyvnytskyi");
                } else if (substringAfterLastChar.equals("luhansk")) {
                    oblToOurOblHashMap.put("lugansk", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "lugansk");
                } else if (substringAfterLastChar.equals("odessa")) {
                    oblToOurOblHashMap.put("odesa", substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, "odesa");
                } else {
                    oblToOurOblHashMap.put(substringAfterLastChar, substringAfterLastChar);
                    ourOblToOblHashMap.put(substringAfterLastChar, substringAfterLastChar);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void parseRegion(String region, String obl, String reg) {
        Document doc = Jsoup.parse(region);
        Elements paragraphs = doc.select("#pageContent #blocksContent .miniblock");
        for (Element paragraph : paragraphs) {
            if (!paragraph.hasClass("yellow")){
                Agrarian agrarian = new Agrarian();
                List<String> phones = new ArrayList<>();
                Set<SellType> sellsArray = new HashSet<>();
                agrarian.setPhones(phones);
                agrarian.setSells(sellsArray);
                agrarian.setOblast(obl);
                agrarian.setOldRegion(reg);

                Element imageElement = paragraph.select(".miniPic img").first();
                String imageUrl = imageElement.attr("src");
                imageUrl = imageUrl.replaceAll(" ", "");
                if (!imageUrl.equals("")) {
                    if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                        imageUrl = "https://www.idcompass.com" + imageUrl;
                        downloadImage(imageUrl);
                    }
                }
                agrarian.setTitle(paragraph.select("h3").text());
                agrarian.setAddress(paragraph.select("p").get(0).text());
                agrarian.setHead(paragraph.select("p").get(1).getElementsByIndexEquals(1).text());

                String sells2 = paragraph.select("p").get(2).getElementsByIndexEquals(1).text();
                String sells1 = sells2.replaceAll("\\*", "");
                String sells = sells1.replaceAll("\\?", ",");
                Pattern pSells = Pattern.compile("(?<=^|,|\\.|;|/)([^,;/.\\(]*(\\([^\\)]*\\))?[^,.;/\\(]*)*");
                Matcher mSells = pSells.matcher(sells);
                while (mSells.find()) {
                    String resS= "";
                    if (!mSells.group().trim().isEmpty()) {
                        resS =mSells.group().trim();
                    } else {
                        resS = mSells.group();
                    }
                    if (resS.isEmpty()) continue;
                    checkEnumType(resS);
//
//                    Рослинництво - зернових, технічних, кормових, овочевих, баштанних культур і картоплі, садівництво, виноградарство і квіткарство.
//
//                    Тваринництво! - скотарство, свинарство, вівчарство, птахівництво, рибництво, бджільництво та шовківництво. + кормова база
//
//                    Харчова промисловсть(основні) - Цукрова Борошномельна М'ясна Молочна Хлібопекарська Маслоробна Кондитерська Спиртова Макаронна
//                                                    Пивоварна Рибна Виноробна Круп'яна Консервна Тютюнова.


                    Pattern itemSellsPattern1 = Pattern.compile(".*зерн.*|.*пшен.*|.*пшон.*|соняшник|подсолнечник|.*жит.*|жыт|ячме.*|ячмі.*|греч.*|просо|рипак|ріпак|рапс|куку.*|кормові|комбікорми|овес|кукуру.*|технічні|технические|рожь|крупы|крупи|зенові|елеватор|силос|сіно|цукров.*|гірчи.*", Pattern.CASE_INSENSITIVE);
                    Matcher itemSellsMatcher1 = itemSellsPattern1.matcher(resS);
                    Pattern itemSellsPattern2 = Pattern.compile(".*твар.*|.*Твар.*|вівц.*|вівчарство|овц.*|Овц.*|баран.*|коров.*|коні|телят.*|Телят.*|свин.*|Свин.*|животнов.*|ВРХ|КРС|скотарст.*|худоб.*|зоопарк.*|овец.*|буйволи|молок.*|Молок.*|молоч.*|Молоч.*|мисливство|.*птиця|птиц.*|птахівн.*|перепілк.*|куре.*|курк.*|курч.*|бдж.*");
                    Matcher itemSellsMatcher2 = itemSellsPattern2.matcher(resS);
                    Pattern itemSellsPattern3 = Pattern.compile("риба|рыба|Риба|Рыба|рибу|Рибу|рибни.*|Рибни.*");
                    Matcher itemSellsMatcher3 = itemSellsPattern3.matcher(resS);
                    Pattern itemSellsPattern4 = Pattern.compile("Рослинництво.*|рослинництв.*|растениев*|растериеводство|росл.*|хвоя");
                    Matcher itemSellsMatcher4 = itemSellsPattern4.matcher(resS);
                    Pattern itemSellsPattern5 = Pattern.compile("м'ясо|мясо|Мясо|М'ясо|М’ясо|м’ясо|м\"ясо|Ковбас.*|ковбас.*|Ялович.*|ялович.*|ліверн.*|сосиски|сардельки|м'ясн.*");
                    Matcher itemSellsMatcher5 = itemSellsPattern5.matcher(resS);
                    Pattern itemSellsPattern6 = Pattern.compile("садівництво|сад.*|Садівництво|Сад.*|Яблуні|яблуні|фрукт.*|Фрукт.*|кавун|овочі|овоч.*|Овоч.*|Овощ.*|овощ.*|Томат.*|томат.*|картоп.*|Картоп.*|капуст.*|Капуст.*|Свекла|свекла|морков.*|Морков.*|огур.*|Огур.*|цибул.*|Цибул.*|буряк.*|Буряк.*|бобові|соя|Соя|горох|Горох|виноград|Виноград|гриб.*|Гриб.*|ягод.*|Ягод.*|клубни.*|Клубни.*|малин.*|Малин.*|суни.*|лохин.*|кісточк.*|Суни.*|Лохин.*|Кісточк.*");
                    Matcher itemSellsMatcher6 = itemSellsPattern6.matcher(resS);
                    Pattern itemSellsPattern7 = Pattern.compile("ліс.*|Ліс.*");
                    Matcher itemSellsMatcher7 = itemSellsPattern7.matcher(resS);
                    Pattern itemSellsPattern8 = Pattern.compile("олія.*|олій.*|Олія.*|Олій.*|консер.*|тютю.*|Консер.*|Тютю.*|пиво|Пиво|мука||");
                    Matcher itemSellsMatcher8 = itemSellsPattern8.matcher(resS);
                    boolean f = false;
                    if (itemSellsMatcher1.find()){
                        agrarian.getSells().add(SellType.ZERNO);
                        f = true;
                    }
                    if (itemSellsMatcher2.find()){
                        agrarian.getSells().add(SellType.TVARYNNYTSTVO);
                        f = true;
                    }
                    if (itemSellsMatcher3.find()){
                        agrarian.getSells().add(SellType.RYBA);
                        f = true;
                    }
                    if (itemSellsMatcher4.find()){
                        agrarian.getSells().add(SellType.ROSLYNNYTSTVO);
                        f = true;
                    }
                    if (itemSellsMatcher5.find()){
                        agrarian.getSells().add(SellType.MYASO);
                        f = true;
                    }
                    if (itemSellsMatcher6.find()){
                        agrarian.getSells().add(SellType.SADIVNYTSTVO);
                        f = true;
                    }

                    if (itemSellsMatcher7.find()){
                        agrarian.getSells().add(SellType.WOOD);
                        f = true;
                    }
                    if (itemSellsMatcher8.find()){
                        agrarian.getSells().add(SellType.HARCHOVA_PROMYSLOVIST);
                        f = true;
                    }
                    if (!f){
                        System.out.println(resS);
                    }
                }
                agrarian.setServices(paragraph.select("p").get(4).getElementsByIndexEquals(1).text());
                agrarian.setArea(paragraph.select(".miniBlockCut p").get(0).lastElementChild().text());

                String phone = paragraph.select(".miniBlockCut p").get(1).getElementsByIndexEquals(1).text();
                Pattern p = Pattern.compile("[\s+]?[\\d\\s-()]+");
                Matcher m = p.matcher(phone);
                while (m.find()) {
                    String res= "";
                    if (Character.isWhitespace(m.group().charAt(0))){
                        res = m.group().substring(1);
                    } else {
                        res = m.group();
                    }
                    if (res.isEmpty()) continue;
                    agrarian.getPhones().add(res);
//                    if (res.length()>20) {
//                        System.out.println(res + " " + agrarian.getTitle() + " " + agrarian.getOblast() + " " + agrarian.getOldRegion());
//                    }
                }
                agrarian.setEmail(paragraph.select(".miniBlockCut p").get(2).getElementsByIndexEquals(1).text());
                agrarian.setWebsite(paragraph.select(".miniBlockCut p").get(3).getElementsByIndexEquals(1).text());
                agrarian.setVillageCouncil(paragraph.select(".miniBlockCut p").get(4).getElementsByIndexEquals(1).text());

                agrarians.add(agrarian);
            } else {
                VillageCouncil villageCouncil = new VillageCouncil();
                List<String> phones = new ArrayList<>();
                Set<SellType> sellsArray = new HashSet<>();
                villageCouncil.setPhones(phones);
                villageCouncil.setSells(sellsArray);
                villageCouncil.setOblast(obl);
                villageCouncil.setOldRegion(reg);

                Element imageElement = paragraph.select(".miniPic img").first();
                String imageUrl = imageElement.attr("src");
                imageUrl = imageUrl.replaceAll(" ", "");
                if (!imageUrl.equals("")) {
                    if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                        imageUrl = "https://www.idcompass.com" + imageUrl;
                        downloadImage(imageUrl);
                    }
                }
                villageCouncil.setTitle(paragraph.select("h3").text());
                villageCouncil.setAddress(paragraph.select("p").get(0).text());
                villageCouncil.setHead(paragraph.select("p").get(1).getElementsByIndexEquals(1).text());

                String sells2 = paragraph.select("p").get(2).getElementsByIndexEquals(1).text();
                String sells1 = sells2.replaceAll("\\*", "");
                String sells = sells1.replaceAll("\\?", ",");
                Pattern pSells = Pattern.compile("(?<=^|,|\\.|;|/)([^,;/.\\(]*(\\([^\\)]*\\))?[^,.;/\\(]*)*");
                Matcher mSells = pSells.matcher(sells);
                while (mSells.find()) {
                    String resS= "";
                    if (!mSells.group().trim().isEmpty()) {
                        resS =mSells.group().trim();
                    } else {
                        resS = mSells.group();
                    }
                    if (resS.isEmpty()) continue;
                    Pattern itemSellsPattern1 = Pattern.compile(".*зерн.*|.*пшен.*|.*пшон.*|соняшник|Соняшник|Подсолнечник|подсолнечник|.*жит.*|жыт|ячме.*|ячмі.*|Ячме.*|Ячмі.*|греч.*|Греч.*|просо|рипак|ріпак|рапс|куку.*|\\bЗерн.*|Зерн.*|кормові|комбікорми|Пшен|Пшон|Жит|Жыт|Овес|овес|Просо|Рипак|Ріпак|Рапс|Кукуру.*|технічні|Технічні|Технические|технические|Рожь|рожь|крупы|крупи|зенові|елеватор|Силос|Сіно|силос|сіно|цукров.*|Цукров.*|гірчи.*|Гірчи.*");
                    Matcher itemSellsMatcher1 = itemSellsPattern1.matcher(resS);
                    Pattern itemSellsPattern2 = Pattern.compile(".*твар.*|.*Твар.*|вівц.*|вівчарство|овц.*|Овц.*|баран.*|коров.*|коні|телят.*|Телят.*|свин.*|Свин.*|животнов.*|ВРХ|КРС|скотарст.*|худоб.*|зоопарк.*|овец.*|буйволи|молок.*|Молок.*|молоч.*|Молоч.*|мисливство|.*птиця|птиц.*|птахівн.*|перепілк.*|куре.*|курк.*|курч.*|бдж.*");
                    Matcher itemSellsMatcher2 = itemSellsPattern2.matcher(resS);
                    Pattern itemSellsPattern3 = Pattern.compile("риба|рыба|Риба|Рыба|рибу|Рибу|рибни.*|Рибни.*");
                    Matcher itemSellsMatcher3 = itemSellsPattern3.matcher(resS);
                    Pattern itemSellsPattern4 = Pattern.compile("Рослинництво.*|рослинництв.*|растениев*|растериеводство|росл.*|хвоя");
                    Matcher itemSellsMatcher4 = itemSellsPattern4.matcher(resS);
                    Pattern itemSellsPattern5 = Pattern.compile("м'ясо|мясо|Мясо|М'ясо|М’ясо|м’ясо|м\"ясо|Ковбас.*|ковбас.*|Ялович.*|ялович.*|ліверн.*|сосиски|сардельки|м'ясн.*");
                    Matcher itemSellsMatcher5 = itemSellsPattern5.matcher(resS);
                    Pattern itemSellsPattern6 = Pattern.compile("садівництво|сад.*|Садівництво|Сад.*|Яблуні|яблуні|фрукт.*|Фрукт.*|кавун|овочі|овоч.*|Овоч.*|Овощ.*|овощ.*|Томат.*|томат.*|картоп.*|Картоп.*|капуст.*|Капуст.*|Свекла|свекла|морков.*|Морков.*|огур.*|Огур.*|цибул.*|Цибул.*|буряк.*|Буряк.*|бобові|соя|Соя|горох|Горох|виноград|Виноград|гриб.*|Гриб.*|ягод.*|Ягод.*|клубни.*|Клубни.*|малин.*|Малин.*|суни.*|лохин.*|кісточк.*|Суни.*|Лохин.*|Кісточк.*");
                    Matcher itemSellsMatcher6 = itemSellsPattern6.matcher(resS);
                    Pattern itemSellsPattern7 = Pattern.compile("ліс.*|Ліс.*");
                    Matcher itemSellsMatcher7 = itemSellsPattern7.matcher(resS);
                    Pattern itemSellsPattern8 = Pattern.compile("олія.*|олій.*|Олія.*|Олій.*|консер.*|тютю.*|Консер.*|Тютю.*|пиво|Пиво");
                    Matcher itemSellsMatcher8 = itemSellsPattern8.matcher(resS);
                    boolean f = false;
                    if (itemSellsMatcher1.find()){
                        villageCouncil.getSells().add(SellType.ZERNO);
                        f = true;
                    }
                    if (itemSellsMatcher2.find()){
                        villageCouncil.getSells().add(SellType.TVARYNNYTSTVO);
                        f = true;
                    }
                    if (itemSellsMatcher3.find()){
                        villageCouncil.getSells().add(SellType.RYBA);
                        f = true;
                    }
                    if (itemSellsMatcher4.find()){
                        villageCouncil.getSells().add(SellType.ROSLYNNYTSTVO);
                        f = true;
                    }
                    if (itemSellsMatcher5.find()){
                        villageCouncil.getSells().add(SellType.MYASO);
                        f = true;
                    }
                    if (itemSellsMatcher6.find()){
                        villageCouncil.getSells().add(SellType.SADIVNYTSTVO);
                        f = true;
                    }

                    if (itemSellsMatcher7.find()){
                        villageCouncil.getSells().add(SellType.WOOD);
                        f = true;
                    }
                    if (itemSellsMatcher8.find()){
                        villageCouncil.getSells().add(SellType.HARCHOVA_PROMYSLOVIST);
                        f = true;
                    }
                    if (!f){
                        System.out.println(resS);
                    }
                }
                villageCouncil.setServices(paragraph.select("p").get(4).getElementsByIndexEquals(1).text());
                villageCouncil.setArea(paragraph.select(".miniBlockCut p").get(0).lastElementChild().text());

                String phone = paragraph.select(".miniBlockCut p").get(1).getElementsByIndexEquals(1).text();
                Pattern p = Pattern.compile("[\s+]?[\\d\\s-()]+");
                Matcher m = p.matcher(phone);
                while (m.find()) {
                    String res= "";
                    if (Character.isWhitespace(m.group().charAt(0))){
                        res = m.group().substring(1);
                    } else {
                        res = m.group();
                    }
                    if (res.isEmpty()) continue;
                    villageCouncil.getPhones().add(res);
//                    if (res.length()>20) {
//                        System.out.println(res + " " + agrarian.getTitle() + " " + agrarian.getOblast() + " " + agrarian.getOldRegion());
//                    }
                }
                villageCouncil.setEmail(paragraph.select(".miniBlockCut p").get(2).getElementsByIndexEquals(1).text());
                villageCouncil.setWebsite(paragraph.select(".miniBlockCut p").get(3).getElementsByIndexEquals(1).text());
                villageCouncil.setVillageCouncil(paragraph.select(".miniBlockCut p").get(4).getElementsByIndexEquals(1).text());
                villageCouncils.add(villageCouncil);
            }
        }
    }

    public static void checkEnumType(String s){

    }

    public static void downloadImage(String imageUrl) {
        int charPos = imageUrl.lastIndexOf("/");
        String picName = imageUrl.substring(charPos + 1);
        try (InputStream in = new BufferedInputStream(new URL(imageUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/com/parser/parser/pictures/"+System.currentTimeMillis()+""+picName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
