package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.antlr.Context;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class IdentifierTest {
    @Test
    void generatesIdentifier() throws WrongPathException {
        Identifier testId = new Identifier(new Root(), "EXAMPLE_REF");
        MatcherAssert.assertThat(testId.generate(new Context()).text().output(), Matchers.equalTo("EXAMPLE_REF"));
    }

}
