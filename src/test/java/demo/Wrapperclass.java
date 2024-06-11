package demo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
//import org.testng.reporters.Files;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Wrapperclass {
    //save data to a Json file
     public static void saveDataAsJson(List<HashMap<String, String>> results, String outputDirectory, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
       try{
        Path outputPath = Paths.get(outputDirectory,fileName);
        Files.createDirectories(outputPath.getParent());
        objectMapper.writeValue(outputPath.toFile(), results);
       }catch(IOException e){
        e.printStackTrace();
       }
      
    }
    //navigate to specific url
     public static void navigate(WebDriver driver, String url) {
        driver.get(url);
        System.out.println("Navigated to: " + url);
     }
 
}
