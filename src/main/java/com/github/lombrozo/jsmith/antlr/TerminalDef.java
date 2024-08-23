package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.Unparser;

/**
 * Terminal definition.
 * terminalDef
 *     : TOKEN_REF elementOptions?
 *     | STRING_LITERAL elementOptions?
 *     ;
 */
public final class TerminalDef implements RuleDefinition {

    private final RuleDefinition parent;

    private final Unparser unparser;
    private final String text;

    public TerminalDef(final RuleDefinition parent, final Unparser unparser, final String text) {
        this.parent = parent;
        this.unparser = unparser;
        this.text = text;
    }

    @Override
    public RuleDefinition parent() {
        return this.parent;
    }

    @Override
    public String generate() {
        final LexerRuleSpec rule = this.unparser.unlexerRule(this.text);
        //todo: fix null check - remove it!
        if (rule == null) {
            return this.text.replace("'", "");
        }
        return rule.generate();
    }

    @Override
    public void append(final RuleDefinition rule) {
        throw new UnsupportedOperationException("Terminal cannot have children yet");
    }
}
