package net.gageot.kittenmash;

import java.io.*;

import static net.gageot.kittenmash.WebServer.*;

public class KittenController {
	public void render(Answer answer, String kittenId) throws IOException {
		answer.serve(new File("kitten", kittenId + ".jpg"));
	}
}
