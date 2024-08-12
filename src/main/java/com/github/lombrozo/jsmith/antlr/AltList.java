package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Randomizer;
import java.util.ArrayList;
import java.util.List;

public final class AltList implements Generative {

    private final List<Alternative> alternatives;
    private final Randomizer randomizer;

    public AltList() {
        this(new ArrayList<>(0));
    }

    public AltList(final List<Alternative> alternatives) {
        this(alternatives, new Randomizer());
    }

    public AltList(final List<Alternative> alternatives, final Randomizer randomizer) {
        this.alternatives = alternatives;
        this.randomizer = randomizer;
    }

    public AltList withAlternative(final Alternative alternative) {
        this.alternatives.add(alternative);
        return this;
    }

    @Override
    public String generate() {
        final Alternative alternative = alternatives.get(
            this.randomizer.nextInt(this.alternatives.size()));
        return alternative.generate();
    }
}
