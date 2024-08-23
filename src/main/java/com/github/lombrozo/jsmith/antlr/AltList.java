package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Rand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Alternative ANTLR rule.
 * The rule definition:
 * {@code
 * altList
 *     : alternative (OR alternative)*
 *     ;
 * }
 */
public final class AltList implements RuleDefinition {

    private final RuleDefinition parent;

    private final List<RuleDefinition> alternatives;
    private final Rand rand;

    AltList(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    AltList(final RuleDefinition parent, RuleDefinition... alternatives) {
        this(parent, Arrays.asList(alternatives));
    }

    private AltList(final RuleDefinition parent, final List<RuleDefinition> alternatives) {
        this(parent, alternatives, new Rand());
    }

    private AltList(
        final RuleDefinition parent,
        final List<RuleDefinition> alternatives,
        final Rand rand
    ) {
        this.parent = parent;
        this.alternatives = alternatives;
        this.rand = rand;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        String result = "";
        final int size = this.alternatives.size();
        if (size >= 1) {
            result = this.alternatives.get(this.rand.nextInt(size)).generate();
        }
        return result;
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.alternatives.add(rule);
    }
}
