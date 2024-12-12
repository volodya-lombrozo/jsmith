/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Volodya Lombrozo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Params;
import com.github.lombrozo.jsmith.RandomScript;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.semantic.Scope;
import com.github.lombrozo.jsmith.antlr.view.Text;
import com.github.lombrozo.jsmith.random.ConvergenceStrategy;
import com.github.lombrozo.jsmith.random.Rand;
import java.util.Random;
import org.cactoos.io.ResourceOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class NaturalMachineTest {


    @Test
    void travers() {
        Params params = new Params(-167_712_196_876_944_930L);
        final Scope scope = new Scope(new Rand(params.seed()));
        final Context context = new Context(scope, new ConvergenceStrategy(params));
        final Rule prog = new RandomScript(
            params,
            new ResourceOf("grammars/Arithmetic.g4")
        ).rule("prog");
        final String original = prog.generate(context).text().output();

        Params another = new Params(-167_712_196_876_944_930L);
        final Rule root = new RandomScript(
            another,
            new ResourceOf("grammars/Arithmetic.g4")
        ).rule("prog");
        final Text travers = new NaturalMachine(another, root).travers();
        MatcherAssert.assertThat(
            "The output should be a valid arithmetic expression",
            travers.output(),
            Matchers.equalTo("\ngud=2492\r\n" +
                "(KNES)*((skzS))/UZF-gFCQ\n" +
                "80472-zFEw*(92885-59)-(4)\n")
        );
        MatcherAssert.assertThat(
            original,
            Matchers.equalTo(travers.output())
        );
    }

}