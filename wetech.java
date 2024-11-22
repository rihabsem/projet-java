import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;
import java.time.Duration;

public class wetech {
    public void scrap() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\DELL\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            System.out.println("//");
            driver.get("https://www.wetech.ma/offres-emploi/");

            boolean hasMorePages = true;

            while (hasMorePages) {
                // Wait for the page to be fully loaded
                wait.until((WebDriver d) -> {
                    JavascriptExecutor js = (JavascriptExecutor) d;
                    String readyState = js.executeScript("return document.readyState").toString();
                    return readyState.equals("complete");
                });
                System.out.println("Page is fully loaded!");

                // Scroll the page to load all job offers
                scrollPage(driver);

                // Locate job offers after scrolling
                List<WebElement> jobOffers = driver.findElements(By.className("offreUrl"));

                // Iterate through each job offer on the page
                for (int i = 0; i < jobOffers.size(); i++) {
                    try {
                        WebElement offer = jobOffers.get(i);

                        // Scroll to the element to ensure it's in view
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", offer);

                        // Debug: Print element details
                        System.out.println("Attempting to click: " + offer.getText());

                        // Wait until the element is clickable before clicking
                        wait.until(ExpectedConditions.elementToBeClickable(offer));

                        // Click using JavaScript
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", offer);

                        // Wait for the job details page to load
                        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("/html/body/div[2]/div/div[1]/div[1]/div/div/div/div[2]/h1")));
                        String title = titleElement.getText();
                        System.out.println("Title: " + title);

                        WebElement descriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("/html/body/div[2]/div/div[1]/div[2]/div/div/p")));
                        String description = descriptionElement.getText();
                        System.out.println("Job description: " + description);

                        WebElement requirementsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("/html/body/div[2]/div/div[1]/div[3]/div/div/p")));
                        String requirements = requirementsElement.getText();
                        System.out.println("Requirements: " + requirements);
                        System.out.println("\n///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\n");

                        // After scraping the job details, navigate back to the main list
                        driver.navigate().back();

                        // Wait for the job offers to be visible again after returning
                        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("offreUrl")));
                    } catch (StaleElementReferenceException e) {
                        System.out.println("StaleElementReferenceException encountered. Retrying...");
                        // Re-locate job offers after a StaleElementReferenceException
                        jobOffers = driver.findElements(By.className("offreUrl"));
                        i--;  // Retry the same index
                    } catch (Exception e) {
                        System.out.println("Error clicking element: " + e.getMessage());
                    }
                }

                // Navigate to the next page
                try {
                    WebElement nextLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("/html/body/div[2]/div/div[1]/div/div[16]/ul/li[7]/a")));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextLink);
                    String nextPageUrl = nextLink.getAttribute("href");

                    // Load next page
                    driver.get(nextPageUrl);
                } catch (Exception e) {
                    System.out.println("No more pages available.");
                    hasMorePages = false; // Exit the loop when no more pages are available
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void scrollPage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(2000); // Wait for the page to load more content
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long newHeight = (Long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) {
                break; // Break the loop if we've reached the end of the page
            }
            lastHeight = newHeight;
        }
    }
}
