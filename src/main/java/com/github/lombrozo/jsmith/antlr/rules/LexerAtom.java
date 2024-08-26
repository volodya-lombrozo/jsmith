package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.Rand;
import java.util.ArrayList;
import java.util.List;

public final class LexerAtom implements RuleDefinition {

    private final RuleDefinition parent;

    private final List<RuleDefinition> elems;

    public LexerAtom(final RuleDefinition parent) {
        this(parent, new ArrayList<>(0));
    }

    public LexerAtom(final RuleDefinition parent, final List<RuleDefinition> elems) {
        this.parent = parent;
        this.elems = elems;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {

        return this.elems.get(new Rand().nextInt(this.elems.size())).generate();
    }

    @Override
    public void append(final RuleDefinition rule) {
        this.elems.add(rule);
    }
}
