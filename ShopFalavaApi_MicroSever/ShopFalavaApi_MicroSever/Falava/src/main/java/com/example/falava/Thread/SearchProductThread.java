package com.example.falava.Thread;

import com.example.falava.Modal.Results;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class SearchProductThread implements Runnable {
    List<Results> ListResults = new ArrayList<Results>();

    String searchKey;

    private static String turn_to_number(String value) {
        return value.substring(0, value.indexOf(" ₫")).replace(".", "").replace(" ", "");
    }

    private static String turn_to_link(String value) {
        return value.replace("/url?url=", "");
    }

    private static String Cvst(List<Results> listPrice) {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger sts = new AtomicInteger();
        AtomicInteger asm = new AtomicInteger();

        for (int f = 0; f < listPrice.size(); f++) {
            for (int s = f+1; s < listPrice.size(); s++) {
                if(listPrice.get(f).getPrice().replace(".000", "").length() == listPrice.get(s).getPrice().replace(".000", "").length()){
                    count.addAndGet(1);
                }
            }
            if (count.intValue() > sts.intValue()) {
                asm.set(listPrice.get(f).getPrice().replace(".000", "").length());
            }
            count.set(0);
        }
        return String.valueOf(asm.get());
    }


    public SearchProductThread(String searchKey) {
        this.searchKey = searchKey;
    }

    public Stream<Results> getListProduct() {
        return ListResults
                .parallelStream().filter(e -> !Objects.equals(e.getImage(), ""))
                .filter(e -> e.getPrice().replace(".000", "").length() == Integer.parseInt(Cvst(ListResults)));
    }

    @Override
    public void run() {
        try {
            VisitPage(searchKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String SearchImage(String str, String key, String checking) {
        int k = 0;
        int j = 0;
        int length = 0;
        while (k != -1) {
            k = str.indexOf(key, k);
            j = str.indexOf(checking, k);
            length = j - k;
            if (k != -1) {
                int endIdx = k + length + checking.length();
                if (str.length() >= endIdx) {
                    String tas = str.substring(k, endIdx);
                    return tas.substring(tas.indexOf("https://encrypted"));
                } else {
                    String tas = str.substring(k, endIdx - (endIdx - str.length()));
                    return tas.substring(tas.indexOf("https://encrypted"));
                }
                // Advance Idx
                /*                k += key.length();*/
            }
        }
        return "";
    }


    private void VisitPage(String key) {
        try {
            Document doc = Jsoup.connect("https://www.google.com/search?q=".concat(key).concat("&source=lnms&tbm=shop")).get();
            Elements scripts = doc.getElementsByTag("script");
            Elements results = doc.select("div.sh-pr__product-results-grid.sh-pr__product-results");
/*            System.out.println(scripts);*/

            for (Element e : results.select("div.sh-dgr__content")) {
                if (e.select("a").attr("href").contains("/url?url=")) {
                    String SlugLink = turn_to_link(e.select("a").attr("href"));
                    String ValuePrice = turn_to_number(e.select("span.a8Pemb.OFFNJ").text());
                    String price = ValuePrice.substring(0, ValuePrice.length() - 3).concat(".").concat("000");
                    String Title = e.select("h3.tAxDx").text();
                    ListResults.add(new Results(price, Title, SlugLink, SearchImage(scripts.toString(), Title, "CAE")));
                }
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION -- " + key);
        }
    }
}