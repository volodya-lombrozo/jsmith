package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Rand;
import java.util.ArrayList;
import java.util.List;

public final class LexerAtom implements Generative {

    private final Generative parent;

    private final List<Generative> elems;

    public LexerAtom(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public LexerAtom(final Generative parent, final List<Generative> elems) {
        this.parent = parent;
        this.elems = elems;
    }

    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {

        return this.elems.get(new Rand().nextInt(this.elems.size())).generate();
    }

    @Override
    public void append(final Generative generative) {
        this.elems.add(generative);
    }
}
