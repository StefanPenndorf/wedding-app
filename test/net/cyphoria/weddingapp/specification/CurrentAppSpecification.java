package net.cyphoria.weddingapp.specification;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
@RunWith(Cucumber.class)
@Cucumber.Options(format = {"pretty", "html:target/cucumber"}, monochrome = true, tags = "@current")
public class CurrentAppSpecification {


}
