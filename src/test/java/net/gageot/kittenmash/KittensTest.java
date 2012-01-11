package net.gageot.kittenmash;

import org.junit.*;

import static org.fest.assertions.Assertions.*;

public class KittensTest {
	@Test
	public void canShowKittens() {
		Kittens kittens = new Kittens();

		assertThat(kittens.show()).isEqualTo("Kitten FaceMash");
	}
}
