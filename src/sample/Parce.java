package sample;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Java Program to parse/read HTML documents from File using Jsoup library.
 * Jsoup is an open source library which allows Java developer to parse HTML
 * files and extract elements, manipulate data, change style using DOM, CSS and
 * JQuery like method.
 *
 * @author Javin Paul
 */

public class Parce {

    public static String nominal;
    public static String fact;

    public static String targetFile (String target) {
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File(target), "ISO-8859-1");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table = null;
        if (htmlFile != null) {
            table = htmlFile.select("table").get(1);
        }
        Elements find = null;
        if (table != null) {
            find = table.select("tr:contains(DESIGN CAPACITY)");
        }
        Elements findTwo = null;
        if (table != null) {
            findTwo = table.select("tr:contains(FULL CHARGE CAPACITY)");
        }

        Element count = null; // получение непосредственно цифр
        if (find != null) {
            count = find.select("td").get(1);
        }
        Element countTwo = null;
        if (findTwo != null) {
            countTwo = findTwo.select("td").get(1);
        }

        if (count != null) {
            nominal = count.toString().replaceAll("[^0-9]", "");
        }

        if (countTwo != null) {
            fact = countTwo.toString().replaceAll("[^0-9]", "");
        }

        return calculate(nominal, fact);
    }

    public static String calculate(String nominal, String fact) {
        int n = Integer.parseInt(nominal);
        int f = Integer.parseInt(fact);
        return String.valueOf((double) Math.round((double)(n - f) / n * 100.0));
    }

}

