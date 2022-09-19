package adapters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ExternalWebsitesAdapter {
    public String getTodayHolidayName() throws IOException {
        Document unusualHolidayDocument = Jsoup.connect("https://bimkal.pl/kalendarz-swiat").get();
        Elements unusualHolidayElements = unusualHolidayDocument.getElementsByClass("nietypowe");
        return unusualHolidayElements.get(0).text().split(",")[0];
    }
    public String getTodayNameDay() throws IOException{
        Document nameDayDocument = Jsoup.connect("https://imienniczek.pl/dzis").get();
        Elements nameDayElements = nameDayDocument.getElementsByClass("main_imi");
        return nameDayElements.get(0).text().replace(" ", ", ");
    }
}
