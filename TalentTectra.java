import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class TalentTectra {
    public void scrap() {
        try {
            String url = "https://talent-tectra.com/s3/annonces";
            Document doc = Jsoup.connect(url).get();
            Elements jobs = doc.select(".card-body");
            for (Element job : jobs) {
                String title = job.select("h5").text();

                Element secteurActivity = job.select("p.mb-0").first();
                String sActivity = secteurActivity.text();  // Get the text of this <p>

// Get the next sibling <p> for experience
                Element experience = secteurActivity.nextElementSibling();
                String expe = experience.text();

// Get the next sibling <p> for niveauEtude
                Element niveauEtude = experience.nextElementSibling();  // Use experience here instead of secteurActivity
                String nEtude = niveauEtude.text();

// Get the next sibling <p> for fonction
                Element fonction = niveauEtude.nextElementSibling();  // Use niveauEtude here
                String function = fonction.text();

// Optionally, print the values to debug
                System.out.println("Secteur d'activité: " + sActivity);
                System.out.println("Experience: " + expe);
                System.out.println("Niveau d'etude: " + nEtude);
                System.out.println("Fonction: " + function);
                System.out.println("\n///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
