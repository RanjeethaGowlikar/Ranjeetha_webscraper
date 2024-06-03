package demo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Wrapperclass {
    //save data to a Json file
     public static void saveDataAsJson(List<HashMap<String, String>> data, String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        objectMapper.writeValue(new File(filePath), data);
    }
    //navigate to specific url
     public static void navigate(WebDriver driver, String url) {
        driver.get(url);
        System.out.println("Navigated to: " + url);
     }
 
}
