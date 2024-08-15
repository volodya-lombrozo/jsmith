package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Randomizer;
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
public final class AltList implements Generative {

    private final Generative parent;

    private final List<Generative> alternatives;
    private final Randomizer randomizer;

    AltList(final Generative parent) {
        this(parent, new ArrayList<>(0));
    }

    AltList(final Generative parent, Generative... alternatives) {
        this(parent, Arrays.asList(alternatives));
    }

    private AltList(final Generative parent, final List<Generative> alternatives) {
        this(parent, alternatives, new Randomizer());
    }

    private AltList(
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
        String result = "";
        final int size = this.alternatives.size();
        if (size >= 1) {
            result = this.alternatives.get(this.randomizer.nextInt(size)).generate();
        }
        return result;
    }

    @Override
    public void append(final Generative generative) {
        this.alternatives.add(generative);
    }
}
