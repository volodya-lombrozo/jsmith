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
package com.github.lombrozo.jsmith.random;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.List;

/**
 * This strategy is needed to choose child elements from the parent element.
 * Why can't we simply randomly choose from the list of children?
 * So in this case it will inevitably lead to the infinite recursion and {@link StackOverflowError}.
 * The problem is quite well described
 * <a href="https://eli.thegreenplace.net/2010/01/28/generating-random-sentences-from-a-context-free-grammar/">in this article</a>
 * So we might have different strategies how to choose rules from the list of rules.
 * @since 0.1
 */
public interface ChoosingStrategy {

    /**
     * Choose the rule from the list of rules.
     * @param parent Parent rule.
     * @param children List of children rules.
     * @return Chosen rule.
     */
    Rule choose(final Rule parent, List<Rule> children);

    /**
     * Copy the strategy and all its internal state.
     * Pay attention!
     * Strategy must be used only for single generation branch.
     * So, we need to copy the strategy for each new generation branch.
     * @return New instance of the strategy.
     */
    ChoosingStrategy copy();
}
