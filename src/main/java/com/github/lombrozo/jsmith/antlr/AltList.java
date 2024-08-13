package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Randomizer;
import java.util.ArrayList;
import java.util.List;

public final class AltList implements Generative {

    private final Generative parent;

    private final List<Generative> alternatives;
    private final Randomizer randomizer;

    public AltList(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    public AltList(final Generative parent, final List<Generative> alternatives) {
        this(parent, alternatives, new Randomizer());
    }

    public AltList(
        final Generative parent,
        final List<Generative> alternatives,
        final Randomizer randomizer
    ) {
        this.parent = parent;
        this.alternatives = alternatives;
        this.randomizer = randomizer;
    }


    @Override
    public Generative parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        return this.alternatives.get(
            this.randomizer.nextInt(this.alternatives.size())
        ).generate();
    }

    @Override
    public void append(final Generative generative) {
        this.alternatives.add(generative);
    }
}
