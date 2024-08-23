package com.github.lombrozo.jsmith.antlr;

public interface RuleDefinition {

    RuleDefinition parent();

    String generate();

    void append(final RuleDefinition rule);

    class Empty implements RuleDefinition {

        @Override
        public RuleDefinition parent() {
            return this;
        }

        @Override
        public String generate() {
            return "";
        }

        @Override
        public void append(final RuleDefinition ignore) {
        }

        @Override
        public String toString() {
            return "empty";
        }
    }

}
