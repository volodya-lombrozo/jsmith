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

import com.github.lombrozo.jsmith.antlr.semantic.VariableAssignment;
import com.github.lombrozo.jsmith.antlr.semantic.VariableDeclaration;
import com.github.lombrozo.jsmith.antlr.semantic.VariableInitialization;
import com.github.lombrozo.jsmith.antlr.semantic.VariableUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.runtime.Token;

/**
 * Semantic comments.
 * @since 0.1
 */
final class JsmithComments {

    /**
     * Comment tokens.
     */
    private final List<Token> comments;

    /**
     * Constructor.
     * @param comments Comment tokens.
     */
    JsmithComments(final List<Token> comments) {
        this.comments = Optional.ofNullable(comments).orElseGet(ArrayList::new);
    }

    /**
     * Check if a comment is an initialization.
     * @return True if a comment is an initialization.
     */
    boolean isInitialization() {
        return this.rules().stream().anyMatch(VariableInitialization.KEY::equals);
    }

    /**
     * Check if a comment is a variable assignment.
     * @return True if a comment is a variable assignment.
     */
    boolean isAssignment() {
        return this.rules().stream().anyMatch(VariableAssignment.KEY::equals);
    }

    /**
     * Check if a comment is a variable usage.
     * @return True if a comment is a variable usage.
     */
    boolean isUsage() {
        return this.rules().stream().anyMatch(VariableUsage.KEY::equals);
    }

    /**
     * Check if a comment is a variable declaration.
     * @return True if a comment is a variable declaration.
     */
    boolean isDeclaration() {
        return this.rules().stream().anyMatch(VariableDeclaration.KEY::equals);
    }

    /**
     * Get rules from comments.
     * @return List of rules.
     */
    private List<String> rules() {
        return this.comments.stream()
            .filter(Objects::nonNull)
            .map(Token::getText)
            .flatMap(JsmithComments::split)
            .filter(JsmithComments::isJsmit)
            .collect(Collectors.toList());
    }

    /**
     * Split text by space.
     * @param original Original text.
     * @return Stream of strings.
     */
    private static Stream<String> split(final String original) {
        return Arrays.stream(original.split(" ")).map(String::trim);
    }

    /**
     * Check if a text is a jsmit rule.
     * @param original Original text.
     * @return True if text is a jsmit rule.
     */
    private static boolean isJsmit(final String original) {
        return original.startsWith("$jsmith");
    }
}
