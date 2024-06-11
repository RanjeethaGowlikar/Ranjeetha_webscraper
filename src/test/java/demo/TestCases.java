package demo;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    static ChromeDriver driver;
    public static WebDriverWait wait;

    @BeforeSuite
    public void createdriver() {
        System.out.println("Constructor: TestCases");

        WebDriverManager.chromedriver().timeout(30).browserVersion("125.0.6422.61").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testCase01() throws IOException {
        List<HashMap<String, String>> results= new ArrayList<>();
        Wrapperclass.navigate(driver, "https://www.scrapethissite.com/pages/");

        WebElement hockeyTeams = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//a[contains(text(), 'Searching and Pagination')]")));
        hockeyTeams.click();

        int pageCount = 0;
        while (pageCount < 4) {
            // Locate the rows of the table
            List<WebElement> rows = driver.findElements(By.cssSelector("tr.team"));

            // Iterate through rows and collect the data
            for (WebElement row : rows) {
                WebElement teamNameElement = row.findElement(By.cssSelector("td.name"));
                String teamName = teamNameElement.getText();
                String year = row.findElement(By.cssSelector("td.year")).getText();
                String winPercentageElement = row.findElement(By.cssSelector("td.pct")).getText();

                try {
                    float winPercentage = Float.parseFloat(winPercentageElement);
                    // If win% is less than 40% (0.40), store it in HashMap
                    if (winPercentage < 0.40) {
                        HashMap<String, String> details = new HashMap<>();
                        details.put("EpochTime", String.valueOf(System.currentTimeMillis()));
                        details.put("Team", teamName);
                        details.put("WinPercentage", winPercentageElement);
                        details.put("Year", year);
                        results.add(details);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing win percentage: " + winPercentageElement);
                    e.printStackTrace();
                }
            }

            // Go to the next page
            if (pageCount < 3) {
                WebElement nextPageButtonElement = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@aria-label, 'Next')]")));
                nextPageButtonElement.click();
            }

            pageCount++;
        }

        for (HashMap<String, String> team : results) {
            System.out.println(team);
            Assert.assertTrue(Float.parseFloat(team.get("WinPercentage")) < 0.40,"Win percentage is not less than 40%");
        }

        String fileName = "hockey-team-data.json";
        String outputDirectory ="output";
         Wrapperclass.saveDataAsJson(results, outputDirectory, fileName);


        // Assert file presence and non-emptiness
         File outputFile = new File(outputDirectory,fileName);
         Assert.assertTrue(outputFile.exists(), "JSON file should exist");
         Assert.assertTrue(outputFile.length() > 0, "JSON file should not be empty");
    }

    @Test
    public void testCase02() throws InterruptedException, IOException {
        List<HashMap<String, String>> results = new ArrayList<>();
        Wrapperclass.navigate(driver, "https://www.scrapethissite.com/pages/");
        WebElement OscarWinningFilms = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(), 'Oscar Winning Films: AJAX and Javascript')]")));
        OscarWinningFilms.click();

        List<WebElement> yearElements = driver.findElements(By.xpath("//a[contains(@class,'year-link')]"));
       
        for (int i = 0; i < yearElements.size(); i++) {
            yearElements = driver.findElements(By.xpath("//a[contains(@class,'year-link')]"));
            WebElement years = yearElements.get(i);
            String year = years.getText();
            years.click();
            Thread.sleep(4000);
            List<WebElement> movies = driver.findElements(By.cssSelector("tr.film"));
            //get the top five movies
            for (int j = 0; j < Math.min(5, movies.size()); j++) {
                WebElement movie = movies.get(j);

                String titleElement = movie.findElement(By.className("film-title")).getText();
                String nominationsElement = movie.findElement(By.className("film-nominations")).getText();
                String awardsElement = movie.findElement(By.className("film-awards")).getText();
                boolean isWinner =(j==0);
                HashMap<String, String> moviesData = new HashMap<>();

                moviesData.put("EpochTime", String.valueOf(System.currentTimeMillis()));
                moviesData.put("Year", year);
                moviesData.put("Title", titleElement);
                moviesData.put("Nomination", nominationsElement);
                moviesData.put("Awards", awardsElement);
             
                moviesData.put("isWinner", String.valueOf(isWinner));
                results.add(moviesData);
              
            }
            driver.navigate().back();
            Thread.sleep(3000);

            yearElements = driver.findElements(By.xpath("//a[contains(@class,'year-link')]"));
            years = yearElements.get(i);
            Thread.sleep(4000);
            yearElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'year-link')]")));
                for(HashMap<String,String> result: results){
                    //print the result
                    System.out.println(result);
                }
        }
       String outputDirectory ="output";
        String fileName = "oscar-winner-data.json";
        Wrapperclass.saveDataAsJson(results, outputDirectory,fileName );

        // Assert file presence and non-emptiness
        File outputFile = new File(outputDirectory,fileName);
        Assert.assertTrue(outputFile.exists(), "JSON file should exist");
        Assert.assertTrue(outputFile.length() > 0, "JSON file should not be empty");
    }

    


//Close the driver
@AfterSuite
public void endTest(){
System.out.println("End Test cases");
driver.quit();
}
}
