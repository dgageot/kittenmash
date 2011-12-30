package net.gageot.kittenmash;

import net.gageot.test.*;
import org.junit.*;

public class KittensTest extends JWebUnitTester<Kittens> {
	@Test
	public void canShowKittens() {
		beginAt("/");

		assertTextPresent("Kitten FaceMash");
	}
}
