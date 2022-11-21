
package test.flipkart;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/main/resources/features.web/A_Login.feature",
        glue = {"StepDefinition"},
        dryRun = false,
        monochrome = false,
        plugin = {"pretty", "html:target/cucumber-report/cucumber-html-report", "json:target/cucumber-report/TestRunner.json", "junit:target/cucumber-report/cucumber.xml"}
)
public class Runner{


}
