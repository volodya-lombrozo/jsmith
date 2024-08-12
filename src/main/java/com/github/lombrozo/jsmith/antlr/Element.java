package com.github.lombrozo.jsmith.antlr;

public final class Element implements Generative{
    private Atom atom;

    public void withAtom(final Atom atom) {
        this.atom = atom;
    }

    @Override
    public String generate() {
        return atom.generate();
    }
}
