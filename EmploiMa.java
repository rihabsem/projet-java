import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
public class EmploiMa {
    public void scrap() {
        String baseUrl = "https://www.emploi.ma/recherche-jobs-maroc";
        try {
            int currentPage = 1;
            boolean hasNextPage = true;


            // Loop through pages until there are no more "Next" links
            while (hasNextPage) {
                String url = baseUrl + "?page=" + currentPage;
                Document document = Jsoup.connect(url).get();


                System.out.println("Scraping Page: " + currentPage);


                // Select job postings on the current page
                Elements jobs = document.select(".card-job-detail");


                // Loop through each job posting
                for (Element job : jobs) {
                    // Extract job title
                    String title = job.select("h3 > a").text();


                    // Extract experience level and competence
                    String niveauEtude = job.select(".card-job-description > li:contains(\"Niveau d´études requis\") strong").text();
                    String niveauExperience = job.select(".card-job-description > li:contains(\"Niveau d'expérience\") strong").text();
                    String competence = job.select("ul > li:contains(Compétences clés) strong").text();
                    System.out.println("Secteur d'activité: " + niveauEtude);
                    System.out.println("Experience: " + niveauExperience);
                    System.out.println("Competence: " + competence);
                    System.out.println("\n///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");

                }
                Element nextPageLink = document.selectFirst(".pager-next a"); // Replace with the correct selector for your site
                if (nextPageLink != null) {
                    currentPage++;
                } else {
                    hasNextPage = false;
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
