package net.gageot.kittenmash;

import net.sourceforge.jwebunit.junit.*;
import org.junit.*;

public class KittensTest {
	@Test
	public void canShowKittens() throws Exception {
		Kittens.main(null);

		WebTester webTester = new WebTester();
		webTester.setBaseUrl("http://localhost:8080/");
		webTester.beginAt("/");

		webTester.assertTextPresent("Kitten FaceMash");
	}
}
