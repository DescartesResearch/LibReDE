package tools.descartes.librede.models.observation.functions;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Test;

import tools.descartes.librede.testutils.LibredeTest;

public class ErlangCEquationTest extends LibredeTest {

	@Test
	public void testOneServer() {
		ErlangCEquation eqn = new ErlangCEquation();
		assertThat(eqn.calculateValue(1, 0)).isEqualTo(0.0, offset(1e-9));
		assertThat(eqn.calculateValue(1, 0.5)).isEqualTo(0.5, offset(1e-9));
		assertThat(eqn.calculateValue(1, 1)).isEqualTo(1.0, offset(1e-9));
	}
	
	@Test
	public void testTwoServers() {
		ErlangCEquation eqn = new ErlangCEquation();
		assertThat(eqn.calculateValue(2, 0)).isEqualTo(0.0, offset(1e-3));
		assertThat(eqn.calculateValue(2, 0.25)).isEqualTo(0.1, offset(1e-3));
		assertThat(eqn.calculateValue(2, 0.5)).isEqualTo(1 / 3.0, offset(1e-3));
		assertThat(eqn.calculateValue(2, 0.75)).isEqualTo(0.642857142857143, offset(1e-3));
		assertThat(eqn.calculateValue(2, 1)).isEqualTo(1.0, offset(1e-3));
	}
	
	@Test
	public void testFifteenServers() {
		ErlangCEquation eqn = new ErlangCEquation();
		assertThat(eqn.calculateValue(15, 0)).isEqualTo(0.0, offset(1e-3));
		assertThat(eqn.calculateValue(15, 0.25)).isEqualTo(9.77920896491434e-06, offset(1e-3));
		assertThat(eqn.calculateValue(15, 0.5)).isEqualTo(0.0112924361348896, offset(1e-3));
		assertThat(eqn.calculateValue(15, 0.75)).isEqualTo(0.217989042733931, offset(1e-3));
		assertThat(eqn.calculateValue(15, 1)).isEqualTo(1.0, offset(1e-2));
	}
	
}
