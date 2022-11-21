package StepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page.LoginPage;

import java.io.IOException;

public class LoginFlip {

      LoginPage l=new LoginPage();
    @Given("user is in login page")
    public void user_is_in_login_page() throws InterruptedException, IOException {
       l.openBrowser();

    }
    @When("user enter username and password")
    public void user_enter_username_and_password() throws InterruptedException {
        l.userName();


    }
    @When("Click on login button")
    public void click_on_login_button() {
        l.login();

    }
    @Then("User should be navigate to home page")
    public void user_should_be_navigate_to_home_page() {

    }

}
