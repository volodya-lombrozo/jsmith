package com.github.lombrozo.jsmith.antlr;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ProductionsChain}.
 * @since 0.1
 */
class ProductionsChainTest {

    @Test
    void printsChain() {
        MatcherAssert.assertThat(
            "We expect that the tree will be printed correctly",
            new ProductionsChain(
                new EbnfSuffix(
                    new LexerElement(
                        new LexerElements(
                            new Generative.Empty()
                        )
                    ),
                    "+"
                )
            ).toTree(),
            Matchers.equalTo(
                String.join(
                    "\n",
                    "empty",
                    "  └──lexerElements",
                    "       └──lexerElement",
                    "            └──ebnfSuffix(+)\n"
                )
            )
        );
    }

}