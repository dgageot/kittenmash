package net.gageot.kittenmash;

import org.simpleframework.http.*;

import java.io.*;
import java.nio.file.*;

public class KittenController {
	public void render(Response resp, String kittenId) throws IOException {
		Files.copy(Paths.get("kitten", kittenId + ".jpg"), resp.getOutputStream());
	}
}
