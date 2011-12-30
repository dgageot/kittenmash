package net.gageot.kittenmash;

import net.gageot.test.*;
import org.junit.*;

public class KittensTest extends JWebUnitTester<Kittens> {
	@Test
	public void canShowKittens() {
		beginAt("/");

		assertTextPresent("Kitten FaceMash");
	}

	@Test
	public void canShowKitten() {
		beginAt("/kitten/1");

		assertDownloadedFileEquals("kitten/1.jpg");
	}
}
