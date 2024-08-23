package com.github.lombrozo.jsmith.antlr;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Chain of {@link RuleDefinition} objects.
 * Used for logging of the current chain of {@link RuleDefinition} objects.
 * @since 0.1
 */
public final class ProductionsChain {

    private RuleDefinition child;

    public ProductionsChain(final RuleDefinition start) {
        this.child = start;
    }

   public String toTree() {
        return this.tree(this.child);
    }

    /**
     * Prints the tree of the {@link RuleDefinition} objects.
     * @param node The current node.
     * @return The tree of the {@link RuleDefinition} objects.
     */
    private String tree(final RuleDefinition node) {
        final String result;
        if (node == node.parent()) {
            result = String.format("%s\n", node);
        } else {
            final String prev = this.tree(node.parent());
            final int length = prev.split("\n").length;
            result = String.format(
                "%s%s└──%s%n",
                prev,
                Stream.generate(() -> " ")
                    .limit(2 + ((length - 1) * 5L))
                    .collect(Collectors.joining()),
                node
            );
        }
        return result;
    }
}
