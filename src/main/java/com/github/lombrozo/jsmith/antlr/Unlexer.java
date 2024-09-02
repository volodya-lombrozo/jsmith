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

import com.github.lombrozo.jsmith.antlr.rules.LexerRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Unlexer.
 * This class is used to store and to retrieve lexer rules.
 * @since 0.1
 */
public final class Unlexer {

    /**
     * All lexer rules.
     */
    private final Map<String, LexerRuleSpec> rules;

    /**
     * Constructor.
     */
    public Unlexer() {
        this(new HashMap<>(0));
    }

    /**
     * Constructor.
     * @param rules Rules.
     */
    private Unlexer(final Map<String, LexerRuleSpec> rules) {
        this.rules = rules;
    }

    /**
     * Add a new lexer rule.
     * @param rule Lexer rule.
     * @return This unlexer.
     */
    public Unlexer with(final LexerRuleSpec rule) {
        this.rules.put(rule.key(), rule);
        return this;
    }

    /**
     * Find a lexer rule by its name.
     * @param rule Rule name.
     * @return Lexer rule.
     */
    public Optional<Rule> find(final String rule) {
        return Optional.ofNullable(this.rules.get(rule));
    }
}
