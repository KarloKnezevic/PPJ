package hr.unizg.fer.zemris.ppj.maheri.tests.la;

import static org.junit.Assert.*;

import java.util.Map;

import hr.unizg.fer.zemris.ppj.maheri.lexergen.RegDefResolver;

import org.junit.Test;

public class RegDefResolverTest {
	@Test
	public void basicRegexShouldWork() {
		String[] array = { "{abc} a|b|c", "{abcd} {abc}|d", "{slovoBrojSlovo} {abcd}(0|1|2){abcd}" };
		RegDefResolver r = new RegDefResolver(array);
		Map<String, String> result = r.getResolved();
		assertEquals("(a|b|c)|d", result.get("abcd"));
		assertEquals("a|b|c", result.get("abc"));
		assertEquals("((a|b|c)|d)(0|1|2)((a|b|c)|d)", result.get("slovoBrojSlovo"));
	}

	@Test
	public void escapedBracesInRHSAreInterpreted() {
		String[] array = { "{ab} a|b", "{zagrada1} \\{", "{zagrada2} \\}", "{slovoUZagradama} {zagrada1}{ab}{zagrada2}" };
		RegDefResolver r = new RegDefResolver(array);
		Map<String, String> result = r.getResolved();
		assertEquals("{", result.get("zagrada1"));
		assertEquals("}", result.get("zagrada2"));
		assertEquals("({)(a|b)(})", result.get("slovoUZagradama"));
	}

	@Test
	public void escapedBackslashesInRHSStaysEscaped() {
		String[] array = { "{ab} a|b", "{backslash} \\\\", "{sve} {ab}|{backslash}",
				"{7backslasheva} \\\\\\\\\\\\\\\\\\\\\\\\\\\\" };
		RegDefResolver r = new RegDefResolver(array);
		Map<String, String> result = r.getResolved();
		assertEquals("\\\\", result.get("backslash"));
		assertEquals("(a|b)|(\\\\)", result.get("sve"));
		assertEquals("\\\\\\\\\\\\\\\\\\\\\\\\\\\\", result.get("7backslasheva"));
	}

	@Test
	public void shouldntBeFooledByFakeEscapes() {
		String[] array = { "{ab} a|b", "{backslash} \\\\", "{backslashReference_1} {backslash}{ab}",
				"{backslashBrace_1} {backslash}\\}", "{backslashReference_2} \\\\{ab}", "{backslashBrace_2} \\\\\\}",
				"{sve} {backslashBrace_1}{backslashBrace_2}{backslashReference_1}{backslashReference_2}" };
		RegDefResolver r = new RegDefResolver(array);
		Map<String, String> result = r.getResolved();
		assertEquals("((\\\\)})(\\\\})((\\\\)(a|b))(\\\\(a|b))", result.get("sve"));
	}

	@Test
	public void escapesInLHSStayEscaped() {
		String[] array = { "{\\}} [desnaZagrada]", "{\\{} [lijevaZagrada]", "{-@-} -@-",
				"{sve} {\\{}{-@-}{\\}}" };
		RegDefResolver r = new RegDefResolver(array);
		Map<String, String> result = r.getResolved();

		assertEquals("([lijevaZagrada])(-@-)([desnaZagrada])", result.get("sve"));

	}
}
