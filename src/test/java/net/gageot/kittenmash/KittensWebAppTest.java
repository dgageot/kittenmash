package net.gageot.kittenmash;

import com.google.inject.*;
import net.gageot.test.*;
import org.junit.*;

import java.util.*;

import static com.google.inject.util.Modules.*;
import static org.mockito.Mockito.*;

public class KittensWebAppTest extends JWebUnitTester<WebServer> {
	private final Random spyRandom = spy(new Random());

	@Override
	protected Module useModule() {
		return override(new KittenWebApp()).with(new AbstractModule() {
			@Override
			protected void configure() {
				bind(Random.class).toInstance(spyRandom);
			}
		});
	}

	@Before
	public void setUpMocks() {
		when(spyRandom.nextInt(10)).thenReturn(1, 2, 1, 2, 1, 2, 1, 2, 1, 2);
	}

	@Test
	public void canShowKittens() {
		beginAt("/");

		assertTextPresent("Kitten FaceMash");
		assertTextPresent("Which one's cuter?");
		assertImagePresent("/kitten/1", "Left cat");
		assertImagePresent("/kitten/2", "Right cat");
	}

	@Test
	public void canShowScores() {
		beginAt("/");

		assertTextInElement("leftScore", "Score: 0");
		assertTextInElement("rightScore", "Score: 0");
	}

	@Test
	public void canShowKitten() {
		beginAt("/kitten/1");

		assertDownloadedFileEquals("kitten/1.jpg");
	}

	@Test
	public void canShowAnotherKitten() {
		beginAt("/kitten/2");

		assertDownloadedFileEquals("kitten/2.jpg");
	}

	@Test
	public void canVote() {
		beginAt("/");

		clickLinkWithImage("/kitten/1");

		assertTextInElement("leftScore", "Score: 1");
		assertTextInElement("rightScore", "Score: 0");
	}

	@Test
	public void canVoteMultipleTimes() {
		beginAt("/");

		clickLinkWithImage("/kitten/1");
		clickLinkWithImage("/kitten/2");
		clickLinkWithImage("/kitten/1");

		assertTextInElement("leftScore", "Score: 2");
		assertTextInElement("rightScore", "Score: 1");
	}
}
