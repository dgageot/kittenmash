package net.gageot.kittenmash;

import net.gageot.test.*;
import org.junit.*;

public class KittensTest extends JWebUnitTester<Kittens> {
	@Test
	public void canShowKittens() {
		beginAt("/");

		assertTextPresent("Kitten FaceMash");
		assertTextPresent("Which one's cuter?");
		assertImagePresent("/kitten/1", "Left cat");
		assertImagePresent("/kitten/2", "Right cat");
	}

	@Test
	public void canShowKitten() {
		beginAt("/kitten/1");

		assertDownloadedFileEquals("kitten/1.jpg");
	}

	@Test
	public void canAnotherKitten() {
		beginAt("/kitten/2");

		assertDownloadedFileEquals("kitten/2.jpg");
	}
}
