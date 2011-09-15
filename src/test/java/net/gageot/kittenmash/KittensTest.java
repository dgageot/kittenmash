package net.gageot.kittenmash;

import static org.fest.assertions.Assertions.*;
import org.junit.Test;

public class KittensTest {
	@Test
	public void canShowKittens() {
		Kittens kittens = new Kittens();

		assertThat(kittens.show()).isEqualTo("Kitten FaceMash");
	}
}
