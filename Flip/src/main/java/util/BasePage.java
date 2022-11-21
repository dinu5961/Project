package util;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BasePage {

    public static WebDriver driver;
    public static Properties prop;


    public static String propertyValue(String key) throws IOException {


        prop = new Properties();
        FileInputStream fis=new FileInputStream("src/main/java/config/confi.fli.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }
        public void initialization() throws InterruptedException, IOException {

        String env=propertyValue("env");
        //String browserName=propertyValue("browser");
        Scanner scanner=new Scanner(System.in);
        System.out.println("Browser name");
        String browserName=scanner.next();
        if (browserName.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver","/Drivers/chromedriver.exe");
            System.setProperty("webdriver.chrome.silentOutput", "true");
            ChromeOptions option = new ChromeOptions();
            option.addArguments("disable-infobars");
            option.addArguments("start-maximized");
            option.setPageLoadStrategy(PageLoadStrategy.NONE);
            option.addExtensions(new File("/download/buster.crx"));
            driver = new ChromeDriver(option);
        } else if (browserName.equals("FF")) {
            System.setProperty("webdriver.gecko.driver", "./Drivers/geckodriver.exe");// update for gecko or any
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        //driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
       // driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
        driver.get("https://www.flipkart.com");
        Thread.sleep(5000);
    }


//        properties prop=new properties();
//        FileInputStream fis=new FileInputStream("src/main/java/config/confi.fli.properties");
//        prop.load(fis);
//        return prop.getProperty(key);
//



    public void ExplicitWaits(String locator) {
        WebDriverWait wait = new WebDriverWait(driver, 120);
        wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locator))));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
    }

    public void FluentWaits(String locator, String text) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout((Duration.ofSeconds(60)))
                .pollingEvery(Duration.ofSeconds(20))
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(locator)), text));
    }

    public void scrollUsingActions() {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
    }

    public void ScrollusingRobot() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PAGE_UP);
        robot.keyRelease(KeyEvent.VK_PAGE_UP);

    }

    public static Object scrollElementIntoView(WebDriver driver, WebElement element) {
        return ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //scrolling
    public void Scroll() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

    }

    public void JsClick(WebElement s) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", s);

    }

    public Boolean dateDifference(String contractenddate) throws java.text.ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentdate = df.format(today);
        int diff = currentdate.compareTo(contractenddate);
        if (diff >= 0)
            System.out.println("End date is less than or equal to Current date ");
        return true;
    }

    public Boolean VerifyPageHeader(String header) throws InterruptedException {
        try {
            ExplicitWaits("//div[contains(@class,'container')]//div//strong[contains(text(),'" + header + "')]");
            WebElement pageTitle = driver.findElement(By.xpath("//div[contains(@class,'container')]//div//strong[contains(text(),'" + header + "')]"));
            Thread.sleep(3000);
            String pageHeading = pageTitle.getText().toLowerCase();
            String url = driver.getCurrentUrl();
            Thread.sleep(3000);
            String urlpath[] = url.split("#/");
            urlpath[1] = urlpath[1].replaceAll("-", " ");
            if (pageHeading.equals(urlpath[1]) || pageHeading.contains(urlpath[1])) {
                System.out.println("Page Header is matched to url page");
            } else {//special case for work items
                StringBuilder sb = new StringBuilder(urlpath[1]);
                sb.deleteCharAt(urlpath[1].length() - 1);
                if (pageHeading.contains(sb))
                    System.out.println("Page Header is  matched to url page");
                else
                    System.out.println("Page Header is  not matched to url page");
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return true;
    }


    public void readJson() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("/src/Jsonfiles/Reportsinput.json"));
        JSONObject jsonObject = (JSONObject) obj;

    }


    public void openreports() throws InterruptedException {
        //driver.get("https://mypresidio-shadow.presidio.com/#/report?id=44f2cf83-d211-40e0-9ae6-65593e45d709");
        //driver.get("https://mypresidio-shadow.presidio.com/#/account");
    }

    public void radiobuttonSelected() {
        String str = driver.findElement(By.xpath("26110162")).getAttribute("checked");
        if (str.equalsIgnoreCase("true")) {
            System.out.println("Checkbox selected");
        }
    }

    public void teardown() {
        driver.close();
    }


    public void Pagination(int i) {
        driver.findElement(By.xpath("//li[contains(@class,'page-item')]//a[text()='" + i + "']")).click();
    }

    public void selectReportContractendDateDuration(int i, String duration) throws InterruptedException {
        driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='Contract End Date']")).click();
        driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
        Thread.sleep(3000);
    }

    public void selectReportLastDateSupportDuration(int i, String duration) throws InterruptedException {
        driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='Last Date of Support']")).click();
        driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
        Thread.sleep(3000);
    }


    public void selectReportCriteriaDateDuration(String DateValue, int i, String duration) throws IOException, ParseException, InterruptedException {
        switch (DateValue) {
            case "Contract End Date":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='Contract End Date']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "Line Item Status Date":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='Line Item Status Date']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "Last Date of Support":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='Last Date of Support']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "End of Sale":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='End of Sale']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "End of Software Maintenance":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='End of Software Maintenance']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "End of Service Attach":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='End of Service Attach']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "End of Service Contract Renewal":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='End of Service Contract Renewal']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;
            case "Order Date":
                driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='Order Date']")).click();
                driver.findElement(By.xpath("//div[@class='mx-datepicker-sidebar']//button[@data-index='" + i + "' and text()='" + duration + "']")).click();
                Thread.sleep(2000);
                break;

            default:
        }
    }


    public void selectDropDown(String Value, String value) throws IOException, ParseException, InterruptedException {
        Actions act = new Actions(driver);
        switch (Value) {
            case "Manufacturer":
            case "Requester":
            case "Type":
            case "Status":
            case "Ship To":
            case "Send To":
                ExplicitWaits("//select[contains(@class,'input-dropdown') and contains(@placeholder,'" + Value + "')]");
                Select select = new Select(driver.findElement(By.xpath("//select[contains(@class,'input-dropdown') and contains(@placeholder,'" + Value + "')]")));
                List<WebElement> options = select.getOptions();
                System.out.println(options);
                for (WebElement option : options) {
                    if (option.getText().equals(value)) {
                        select.selectByVisibleText(value);
                    }
                }
                break;
            case "Select Contact":
                ExplicitWaits("//select[contains(@class,'input-dropdown') and contains(@xvalue,'address.contact')]");
                select = new Select(driver.findElement(By.xpath("//select[contains(@class,'input-dropdown') and contains(@xvalue,'address.contact')]")));
                options = select.getOptions();
                System.out.println(options);
                for (WebElement option : options) {
                    if (option.getText().equals(value)) {
                        select.selectByVisibleText(value);
                    }
                }
                break;
            case "SelectAddress":
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(1000);
                    act.moveToElement(driver.findElement(By.xpath("//div[contains(@class,'col')]//div[contains(@class,'input-group mb')]"))).click().build().perform();
                    act.sendKeys(value);
                }
                List<WebElement> optionsList = driver.findElements(By.xpath("//div[contains(@class,'col')]//div[contains(@class,'input-group mb')]//select[contains(@class,'card-input-dropdown')]//option"));
                for (WebElement option : optionsList) {
                    if (option.getText().equals(value)) {
                        //act.moveToElement(driver.findElement(By.xpath("//div[contains(@class,'col')]//div[contains(@class,'input-group mb')]//select[contains(@class,'card-input-dropdown')]//option[contains(text(),'" + value + "')]"))).doubleClick().build().perform();
                        act.moveToElement(driver.findElement(By.xpath("//div[contains(@class,'col')]//div[contains(@class,'input-group mb')]//select[contains(@class,'card-input-dropdown')]//option[contains(text(),'" + value + "')]"))).sendKeys(Keys.ENTER);
                    }
                }
                break;
            case "shippingMethodType":
            case "newAddress.state":
                select = new Select(driver.findElement(By.xpath("//select[contains(@class,'input-dropdown') and contains(@value,'" + Value + "')]")));
                options = select.getOptions();
                System.out.println(options);
                for (WebElement option : options) {
                    if (option.getText().equals(value)) {
                        select.selectByVisibleText(value);
                    }
                }
                break;
            case "ContactType":
                ExplicitWaits("//div[contains(@class,'input-group')]//select//option[contains(text(),'Contact Type')]");
                act.moveToElement(driver.findElement(By.xpath("//div[@class='col-4']//div[contains(@class,'input-group')]//select"))).click().build().perform();
                select = new Select(driver.findElement(By.xpath("//div[@class='col-4']//div[contains(@class,'input-group')]//select")));
                options = select.getOptions();
                System.out.println(options);
                for (WebElement option : options) {
                    if (option.getText().equals(value)) {
                        select.selectByVisibleText(value);
                    }
                }
                break;

            case "Country":
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(3000);
                    act.moveToElement(driver.findElement(By.xpath("//div[@class='col-3'][4]//div[@class='input-group mb-2']"))).click().build().perform();
                    act.sendKeys(value);
                }
                optionsList = driver.findElements(By.xpath("//div[@class='col-3'][4]//div[@class='input-group mb-2']//select[contains(@class,'card-input-dropdown')]//option"));
                for (WebElement option : optionsList) {
                    if (option.getText().equals(value)) {
                        act.moveToElement(driver.findElement(By.xpath("//div[@class='col-3'][4]//select[contains(@class,'card-input-dropdown')]//option[contains(text(),'" + value + "')]"))).doubleClick().build().perform();
                    }
                }
                break;
            case "Contact":
                for (int i = 0; i < 6; i++) {
                    Thread.sleep(3000);
                    act.moveToElement(driver.findElement(By.xpath("//div[@class='col-4']//div[@class='input-group mb-2']//select"))).click().build().perform();
                }

                optionsList = driver.findElements(By.xpath("//div[@class='col-4']//div[@class='input-group']//select[contains(@class,'card-input-dropdown')]//option"));
                for (WebElement option : optionsList) {
                    if (option.getText().equals(value))
                        act.moveToElement(driver.findElement(By.xpath("//div[@class='col-4']//div[@class='input-group mb-2']//select[contains(@class,'input-group form-control card-input-dropdown')]//option[contains(text(),'" + value + "')]"))).click().build().perform();
                    driver.findElement(By.xpath("//div[@class='col-4']//select[contains(@class,'input-group form-control card-input-dropdown')]//option[contains(text(),'" + value + "')]")).sendKeys(Keys.ENTER);
                }
                break;
            default:
        }

    }


    public void uploadFile() throws AWTException {
        WebElement fileInput = driver.findElement(By.name("UploadFile"));
        fileInput.sendKeys("C:\\Users\\sree.harsha\\PresidioProject\\src\\Files\\VendorContractTemplate.xlsx");
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
    }

    public void selectByValue(String value) throws InterruptedException {
        Actions act = new Actions(driver);
        for (int j = 0; j < 2; j++) {
            Thread.sleep(3000);
            act.moveToElement(driver.findElement(By.xpath("//select[contains(@class,'form-control')]"))).click().build().perform();
        }
        act.sendKeys("ENTERGY");
        List<WebElement> options = driver.findElements(By.xpath("//select[contains(@class,'form-control')]//option"));
        for (WebElement option : options) {
            if (option.getText().equals(value))
                driver.findElement(By.xpath("//select[contains(@class,'form-control')]//option[contains(text(),'" + value + "')]")).click();
        }
    }

    public void uploadFileInContract() throws AWTException {
        Robot robot = new Robot();
        Actions act = new Actions(driver);
        act.moveToElement(driver.findElement(By.xpath("//label[@for='customFile']"))).click().build().perform();
        robot.setAutoDelay(1000);
        //StringSelection stringSelection = new StringSelection("C:\\Users\\sree.harsha\\PresidioProject\\src\\Files\\VendorContractTemplate.xlsx");
        StringSelection stringSelection = new StringSelection("\\src\\Files\\VendorContractTemplate");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        robot.setAutoDelay(500);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void enterTextBox(String Control, String Value) {
        Actions action = new Actions(driver);
        switch (Control) {
            case "Request":
            case "User Name":
            case "Cisco CCO ID":
            case "RMA Number":
            case "Previous":
            case "Invoice":
            case "PO":
            case "Order":
            case "Title":
            case "Part Number":
            case "OrderSerial":
            case "Quote":
            case "Requisitions":
            case "Pricing Request":
            case "Manufacturer Part":
            case "Order Name:":
            case "Name":
            case "Street address":
            case "City":
            case "Zip Code":
                WebElement element = driver.findElement(By.xpath("//div[contains(@class,'group')]//input[contains(@placeholder,'" + Control + "')]"));
                action.moveToElement(element).build().perform();
                element.clear();
                element.sendKeys(Value);
                break;
            case "Company Name":
            case "First Name":
            case "Email":
            case "Last Name":
            case "Confirm Password*":
            case "Customer Account":
                element = driver.findElement(By.xpath("//div[contains(@class,'row')]//input[contains(@placeholder,'" + Control + "')]"));
                action.moveToElement(element).perform();
                element.clear();
                element.sendKeys(Value);
                break;
            case "Serial":
                element = driver.findElement(By.xpath("//div[contains(@class,'group')]//textarea[contains(@placeholder,'" + Control + "')]"));
                action.moveToElement(element).perform();
                element.clear();
                element.sendKeys(Value);
                break;
            case "Comments":
            case "Question":
            case "Description":
                element = driver.findElement(By.xpath("//div[contains(@class,'row')]//textarea[contains(@placeholder,'" + Control + "')]"));
                action.moveToElement(element).perform();
                element.clear();
                element.sendKeys(Value);
                break;
            case "Serial Number:*":
            case "Manufacturer":
            case "Password*":
                element = driver.findElement(By.xpath("//div[contains(@class,'row')]//input[@placeholder='" + Control + "']"));
                action.moveToElement(element).perform();
                element.sendKeys(Value);
                break;
            case "Search":
                element = driver.findElement(By.xpath("//div[contains(@class,'search')]//input[contains(@placeholder,'" + Control + "')]"));
                action.moveToElement(element).perform();
                element.clear();
                element.sendKeys(Value);
                break;

        }
    }

    public void selectDropDownByIndex(String Value) {
        switch (Value) {
            case "HierarchyName":
                ExplicitWaits("//div[@class='form-group ml-2']//div[contains(@class,'input-group')]//select");
                Select select = new Select(driver.findElement(By.xpath("//div[@class='form-group ml-2']//div[contains(@class,'input-group')]//select")));
                List<WebElement> options = select.getOptions();
                System.out.println(options);
                for (WebElement option : options) {
                    if (option.getText() != null) {
                        select.selectByIndex(1);
                    }
                }
                break;

        }

    }

    public void downloadLink() {
        driver.findElement(By.xpath("//div[contains(@class,'text-right')]/img[contains(@class,'cursor-pointer')]")).click();
    }

    public Boolean VerifyPageTitle(String header) throws InterruptedException {
        try {
            ExplicitWaits("//div[contains(@class,'container')]//div[contains(text(),'" + header + "')]");
            WebElement pageTitle = driver.findElement(By.xpath("//div[contains(@class,'container')]//div[contains(text(),'" + header + "')]"));
            return pageTitle.isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        return true;
    }

    public void printLink() throws InterruptedException {
        Actions act = new Actions(driver);

        act.moveToElement(driver.findElement(By.xpath("//tr[1]//td//img[contains(@src,'Print')]"))).click().build().perform();
        Thread.sleep(1000);

    }


    public void exportLink() throws InterruptedException {
        Actions act = new Actions(driver);

        act.moveToElement(driver.findElement(By.xpath("//tr[1]//td//img[contains(@src,'Export')]"))).click().build().perform();
        Thread.sleep(1000);

    }

    public void selectDropdownInSearch(String value) throws InterruptedException {
        Actions act = new Actions(driver);
        for (int j = 0; j < 2; j++) {
            Thread.sleep(3000);
            act.moveToElement(driver.findElement(By.xpath("//select[contains(@class,'form-control')]"))).click().build().perform();
        }
        List<WebElement> options = driver.findElements(By.xpath("//select[contains(@class,'form-control')]//option"));
        for (WebElement option : options) {
            if (option.getText().equals(value))
                driver.findElement(By.xpath("//select[contains(@class,'form-control')]//option[contains(text(),'" + value + "')]")).click();
        }
    }

    public void sortTheHeaders(int count, int j) {
        for (int i = count; i <= j; i++) {
            driver.findElement(By.xpath("//thead[@class='table-head ']//tr//th[" + i + "]")).click();
        }
    }

    public void selectReportCriteriaDateDurationToEmpty(String DateValue) throws IOException, ParseException, InterruptedException {
        Actions act = new Actions(driver);
        switch (DateValue) {
            case "Contract End Date":
            case "Line Item Status Date":
            case "Last Date of Support":
            case "End of Sale":
            case "End of Software Maintenance":
            case "End of Service Attach":
            case "End of Service Contract Renewal":
            case "Order Date":
                try {
                    ExplicitWaits("//div[@class='mx-datepicker mx-datepicker-range']//div//input[contains(@placeholder,'" + DateValue + "')]/../i[@class='mx-icon-calendar']");
                    driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//div//input[contains(@placeholder,'" + DateValue + "')]/../i[@class='mx-icon-calendar']")).click();
                    driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//div//input[contains(@placeholder,'" + DateValue + "')]/../i[@class='mx-icon-calendar']")).sendKeys(Keys.ENTER);
                } catch (ElementNotInteractableException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }

    public void verifyReportCriteriaDateDuration(String DateValue) throws IOException, ParseException, InterruptedException {
        switch (DateValue) {
            case "Contract End Date":
            case "Line Item Status Date":
            case "Last Date of Support":
            case "End of Sale":
            case "End of Software Maintenance":
            case "End of Service Attach":
            case "End of Service Contract Renewal":
            case "Order Date":
                driver.navigate().refresh();
                String datavalue = driver.findElement(By.xpath("//div[@class='mx-datepicker mx-datepicker-range']//input[@placeholder='" + DateValue + "']")).getText();
                System.out.println(datavalue);
                break;
        }
    }

    public void LoginUsingApi() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","/Drivers/chromedriver.exe");
        RestAssured.baseURI = prop.getProperty("apiurl");
        RequestSpecification request = RestAssured.given();
        String payload = "{\"username\":\"arjun@test.com\",\"password\":\"1234\",\"googleCaptcha\":\"\"}";
        request.header("Content-Type", "application/json");
        Response responsefromspecification = request.body(payload).post("/v1.0/user/login");
        responsefromspecification.prettyPrint();
        String accesstype = responsefromspecification.getBody().jsonPath().get("type");
        String accesstoken = responsefromspecification.getBody().jsonPath().get("access_token");
        String refreshtoken = responsefromspecification.getBody().jsonPath().get("refresh_token");
        ChromeOptions option = new ChromeOptions();
        option.addArguments("disable-infobars");
        option.addArguments("start-maximized");
        option.setPageLoadStrategy(PageLoadStrategy.NONE);
        driver=new ChromeDriver(option);
        driver.get("https://mypresidio-qa.presidio.com/#/redirect?token_type=" + accesstype + "&access_token=" + accesstoken + "&refresh_token=" + refreshtoken + "&url=/");
        Thread.sleep(20000);
    }
}