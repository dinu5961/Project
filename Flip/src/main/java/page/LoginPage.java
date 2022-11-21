package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import util.BasePage;

import java.io.IOException;

public class LoginPage extends BasePage {

    public LoginPage(){

        PageFactory.initElements(driver,this);
    }

    public void openBrowser() throws InterruptedException, IOException {

        initialization();
    }
    public static void userName() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@href='/account/login?ret=/']")).click();
        String username = prop.getProperty("username");
        driver.findElement(By.xpath("//*[@class='_2IX_2- VJZDxU']")).sendKeys(username);
        String password = prop.getProperty("password");
        driver.findElement(By.xpath("//*[@type='password']")).sendKeys(password);
    }
    public static void login(){


       WebElement click= driver.findElement(By.xpath("(//*[text()='Login'])[3]"));

       click.click();
    }

    }


