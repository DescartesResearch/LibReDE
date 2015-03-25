package tools.descartes.librede.testutils;

import org.junit.BeforeClass;

import tools.descartes.librede.*;

public class LibredeTest {

	@BeforeClass
	public static void register() {
		Librede.init();
	}
}
