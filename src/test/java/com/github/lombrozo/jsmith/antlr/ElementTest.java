package com.github.lombrozo.jsmith.antlr;

import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link Element}.
 *
 * @since 0.1
 */
final class ElementTest {

    @Test
    void generatesUsingAtomBranch() {
        final Root root = new Root();
        final RuleDefinition element = new Element(root);
        final Atom atom = new Atom(root);
        atom.append(new Literal("1"));
        element.append(atom);
        //todo: assert
        System.out.println(element.generate());
    }

    @Test
    void generatesUsingAtomBranchAndEbnfSuffix() {
        final Root root = new Root();
        final RuleDefinition element = new Element(root);
        final Atom atom = new Atom(root);
        atom.append(new Literal("1"));
        element.append(atom);
        element.append(new EbnfSuffix("*"));
        //todo: assert
        System.out.println(element.generate());
    }
}