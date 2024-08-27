package com.github.lombrozo.jsmith.antlr.rules;

import com.github.lombrozo.jsmith.Rand;
import java.util.Collections;

public interface Multiplier {

    String generate(final RuleDefinition rule);

    final class One implements Multiplier {

        @Override
        public String generate(final RuleDefinition rule) {
            return rule.generate();
        }
    }

    final class ZeroOrOne implements Multiplier {

        private final Rand rand;

        public ZeroOrOne(final Rand rand) {
            this.rand = rand;
        }

        @Override
        public String generate(final RuleDefinition rule) {
            final String result;
            if (this.rand.flip()) {
                result = rule.generate();
            } else {
                result = "";
            }
            return result;
        }
    }

    final class OneOrMore implements Multiplier {

        private final Rand rand;
        private final int limit;

        public OneOrMore(final Rand rand) {
            this(rand, 5);
        }

        public OneOrMore(final Rand rand, final int limit) {
            this.rand = rand;
            this.limit = limit;
        }

        @Override
        public String generate(final RuleDefinition rule) {
            return new Several(
                Collections.nCopies(this.rand.nextInt(this.limit) + 1, rule)
            ).generate();
        }
    }

    final class ZeroOrMore implements Multiplier {

        private final Rand rand;
        private final int limit;

        public ZeroOrMore(final Rand rand) {
            this(rand, 5);
        }

        public ZeroOrMore(final Rand rand, final int limit) {
            this.rand = rand;
            this.limit = limit;
        }

        @Override
        public String generate(final RuleDefinition rule) {
            return new Several(
                Collections.nCopies(this.rand.nextInt(this.limit), rule)
            ).generate();
        }
    }
}
