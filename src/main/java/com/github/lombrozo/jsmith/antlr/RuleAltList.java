package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Randomizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule alternative list.
 * The ruleAltList definition in ANTLR grammar:
 * {@code
 * ruleAltList
 *     : labeledAlt (OR labeledAlt)*
 *     ;
 * }
 * @since 0.1
 */
public final class RuleAltList implements Generative {

    private final Generative parent;
    private final List<Generative> alternatives;

    private final Randomizer randomizer = new Randomizer();

    public RuleAltList(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public RuleAltList(final Generative parent, final List<Generative> alternatives) {
        this.parent = parent;
        this.alternatives = alternatives;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        if (this.alternatives.isEmpty()) {
            throw new IllegalStateException("RuleAltList should have at least one alternative");
        }
        return this.alternatives.get(this.randomizer.nextInt(this.alternatives.size())).generate();
    }

    @Override
    public void append(final Generative generative) {
        this.alternatives.add(generative);
    }

    @Override
    public String toString() {
        return String.format("ruleAltList(alternatives=%d)", this.alternatives.size());
    }
}
