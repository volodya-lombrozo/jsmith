package com.github.lombrozo.jsmith;

import java.util.ArrayList;
import java.util.List;

public final class AltList {

    private final List<Alternative> alternatives;

    public AltList() {
        this(new ArrayList<>(0));
    }

    public AltList(final List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    public AltList withAlternative(final Alternative alternative) {
        this.alternatives.add(alternative);
        return this;
    }
}
