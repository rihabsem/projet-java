
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rekrut {
    private static final Logger LOGGER = Logger.getLogger(Rekrut.class.getName());
    private static final int MAX_RETRIES = 3; // Retry up to 3 times on timeout
    private final String baseUrl = "https://www.rekrute.com/offres.html";
    private final String urlParams = "?workExperienceId%5B0%5D=1&workExperienceId%5B1%5D=2";

    public Rekrut() {
        // Default constructor
    }

    /**
     * Scrapes job data from the specified website.
     *
     * @return A map where the key is the job title and the value is the job details.
     */
    public Map<String, JobDetails> scrapeJobs() {
        Map<String, JobDetails> jobMap = new HashMap<>();
        int currentPage = 1;
        boolean hasNextPage = true;

        try {
            while (hasNextPage) {
                String url = constructUrl(currentPage);
                LOGGER.info("Scraping Page: " + currentPage);

                Document doc = null;
                int attempt = 0;

                // Retry mechanism for timeout errors
                while (attempt < MAX_RETRIES) {
                    try {
                        doc = Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                                .timeout(10000) // 10 seconds timeout
                                .get();
                        break; // Exit loop on successful connection
                    } catch (IOException e) {
                        attempt++;
                        LOGGER.warning("Timeout occurred (Attempt " + attempt + "/" + MAX_RETRIES + ")");
                        if (attempt == MAX_RETRIES) {
                            LOGGER.severe("Failed to fetch page " + currentPage + " after " + MAX_RETRIES + " attempts. Skipping...");
                            return jobMap; // Return what has been scraped so far
                        }
                    }
                }

                // Process the jobs found on the page
                Elements jobs = doc.select(".section");
                if (jobs.isEmpty()) {
                    LOGGER.warning("No jobs found on page " + currentPage);
                    break;
                }

                for (Element job : jobs) {
                    String title = job.select("h2 > a.titreJob").text().trim();
                    String activity = job.select(".holder > .info > ul > li:contains(Secteur d\\'activité) a").text().trim();
                    String fonction = job.select(".holder > .info > ul > li:contains(Fonction) a").text().trim();
                    String experience = job.select(".holder > .info > ul > li:contains(Expérience requise) a").text().trim();
                    String niveauEtude = job.select(".holder > .info > ul > li:contains(Niveau d\\'étude demandé) a").text().trim();

                    if (title.isEmpty()) {
                        LOGGER.warning("Job title is missing on page " + currentPage);
                        continue;
                    }

                    JobDetails jobDetails = new JobDetails(experience, fonction, activity, niveauEtude);
                    jobMap.put(title, jobDetails);
                }

                // Determine if there is a next page
                Element nextPageElement = doc.selectFirst(".next");
                hasNextPage = nextPageElement != null && !nextPageElement.hasClass("disabled");
                if (hasNextPage) {
                    currentPage++;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while scraping jobs: ", e);
        }

        return jobMap;
    }

    /**
     * Constructs the URL for a given page.
     *
     * @param page The current page number.
     * @return The full URL for the page.
     */
    private String constructUrl(int page) {
        if (page == 1) {
            return baseUrl + "?s=1&p=1&o=1" + urlParams;
        } else {
            return baseUrl + "?p=" + page + "&s=1&o=1" + urlParams;
        }
    }
}
