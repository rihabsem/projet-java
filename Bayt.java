import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;
import java.time.Duration;
public class Bayt {
    public void scrap(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\DELL\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Open the website
            driver.get("https://www.bayt.com/en/morocco/jobs/?jobid=5201745&page=2&_gl=1*lq5vlk*_up*MQ.._ga*MTI3NzA0MTI4MC4xNzMxMTE1Mzc4_ga_1NKPLGNKKD*MTczMTExNTM3OC4xLjAuMTczMTExNTM3OC4wLjAuMA..");


            // Create a WebDriverWait instance for handling waits
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            boolean hasMorePages = true;


            while (hasMorePages) {
                // Scrape the offers on the current page


                // Locate job listings (replace with the actual selector for the offers)
                List<WebElement> jobOffers = driver.findElements(By.className("has-pointer-d"));


                // Iterate over each offer
                for (int i = 0; i < jobOffers.size(); i++) {
                    try {
                        // Refresh jobOffers list in case it becomes stale after clicking

                        jobOffers = driver.findElements(By.className("has-pointer-d"));


                        WebElement offer = jobOffers.get(i);

                        // Wait until the element is clickable
                        wait.until(ExpectedConditions.elementToBeClickable(offer));

                        // Scroll the element into view
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", offer);

                        //get that title
                        // Wait for details to load

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jobViewJobTitle")));
                        String title = driver.findElement(By.id("jobViewJobTitle")).getText();

                        // Extract job type
                        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".t-small.u-stretch")));
                        //String type = driver.findElement(By.cssSelector(".t-small.u-stretch")).getText();

                        // Extract job description

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[contains(@class,'box is-wide row is-reversed-d is-collapsed-m no-wrap')]/div[contains(@id,'jsMiniJobView')]/div[contains(@id,'formToggleSheet')]/div[contains(@id,'view_inner')]/div[contains(@class,'card p0 is-primary-d u-shadow')]/div[contains(@class,'card-content p10t-m')]")));
                        String paragraph = driver.findElement(By.xpath("/html/body/section[contains(@class,'box is-wide row is-reversed-d is-collapsed-m no-wrap')]/div[contains(@id,'jsMiniJobView')]/div[contains(@id,'formToggleSheet')]/div[contains(@id,'view_inner')]/div[contains(@class,'card p0 is-primary-d u-shadow')]/div[contains(@class,'card-content p10t-m')]")).getText();

                        String[] lines = paragraph.split("\\r?\\n");


                        String date = lines[0];


                        StringBuilder description = new StringBuilder();

                        for (int j = 4; j < lines.length; j++) {

                            description.append(lines[j] + "\n");
                        }

                        // Print or save the details
                        System.out.println("Title: " + title);
                        System.out.println("date de disposition : " + date);
                        System.out.println("description : " + description);

                        System.out.println("\n///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");

                        // Go back to the main list
                        driver.navigate().back();

                        // Wait for the main job list to reload
                        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("has-pointer-d")));

                    } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                        // Handle the case where the element is not clickable
                        System.out.println("Element not clickable. Trying again.");
                        // You could also wait and retry if needed, or add custom logic.
                    }

                }

                try {
                    WebElement nextLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(@class,'pagination-next')]/a[contains(@class,'jsAjaxLoad')]"))); // Replace with actual class or ID of the "Next" button
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextLink);
                    String nextPageUrl = nextLink.getAttribute("href");
                    driver.get(nextPageUrl);
                } catch (Exception e) {
                    System.out.println("No more pages available.");
                    hasMorePages = false;  // Exit the loop if "Next" button is not found or clickable
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the driver
            driver.quit();
        }
    }
}
