package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.ANTLRv4Parser;
import com.github.lombrozo.jsmith.ANTLRv4ParserBaseListener;
import com.github.lombrozo.jsmith.UnlexerRule;
import com.github.lombrozo.jsmith.Unparser;
import com.github.lombrozo.jsmith.UnparserRule;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class ANTLRListener extends ANTLRv4ParserBaseListener {

    private final Unparser unparser = new Unparser("expr");
    private Generative current = new Root();

    @Override
    public void enterParserRuleSpec(final ANTLRv4Parser.ParserRuleSpecContext ctx) {
        final UnparserRule rule = new UnparserRule(ctx.RULE_REF().getText(), this.current);
        this.current.append(rule);
        this.current = rule;
        this.unparser.withParserRule(rule);
        super.enterParserRuleSpec(ctx);
    }

    @Override
    public void exitParserRuleSpec(final ANTLRv4Parser.ParserRuleSpecContext ctx) {
        this.current = this.current.parent();
        super.exitParserRuleSpec(ctx);
    }

    @Override
    public void enterLexerRuleSpec(final ANTLRv4Parser.LexerRuleSpecContext ctx) {
        final String text = ctx.TOKEN_REF().getText();
        final UnlexerRule rule = new UnlexerRule(this.current, text);
        this.current.append(rule);
        this.current = rule;
        this.unparser.withLexerRule(rule);
        super.enterLexerRuleSpec(ctx);
    }

    @Override
    public void exitLexerRuleSpec(final ANTLRv4Parser.LexerRuleSpecContext ctx) {
        this.current = this.current.parent();
        super.exitLexerRuleSpec(ctx);
    }

    @Override
    public void enterLexerAltList(final ANTLRv4Parser.LexerAltListContext ctx) {
        final Generative list = new AltList(this.current);
        this.current.append(list);
        this.current = list;
        super.enterLexerAltList(ctx);
    }

    @Override
    public void exitAltList(final ANTLRv4Parser.AltListContext ctx) {
        this.current = this.current.parent();
        super.exitAltList(ctx);
    }

    @Override
    public void enterLexerAlt(final ANTLRv4Parser.LexerAltContext ctx) {
        final Alternative alternative = new Alternative(this.current);
        this.current.append(alternative);
        this.current = alternative;
        super.enterLexerAlt(ctx);
    }

    @Override
    public void exitLexerAlt(final ANTLRv4Parser.LexerAltContext ctx) {
        this.current = this.current.parent();
        super.exitLexerAlt(ctx);
    }

    @Override
    public void enterRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        final AltList list = new AltList(this.current);
        this.current.append(list);
        this.current = list;
        super.enterRuleAltList(ctx);
    }

    @Override
    public void exitRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        this.current = this.current.parent();
        super.exitRuleAltList(ctx);
    }

    @Override
    public void enterAlternative(final ANTLRv4Parser.AlternativeContext ctx) {
        final Generative alternative = new Alternative(this.current);
        this.current.append(alternative);
        this.current = alternative;
        super.enterAlternative(ctx);
    }

    @Override
    public void exitAlternative(final ANTLRv4Parser.AlternativeContext ctx) {
        this.current = this.current.parent();
        super.exitAlternative(ctx);
    }

    @Override
    public void enterElement(final ANTLRv4Parser.ElementContext ctx) {
        final Generative element = new Element(this.current);
        this.current.append(element);
        this.current = element;
        super.enterElement(ctx);
    }

    @Override
    public void exitElement(final ANTLRv4Parser.ElementContext ctx) {
        this.current = this.current.parent();
        super.exitElement(ctx);
    }

    @Override
    public void enterAtom(final ANTLRv4Parser.AtomContext ctx) {
        final Generative atom = new Atom(this.current);
        this.current.append(atom);
        this.current = atom;
        super.enterAtom(ctx);
    }

    @Override
    public void exitAtom(final ANTLRv4Parser.AtomContext ctx) {
        this.current = this.current.parent();
        super.exitAtom(ctx);
    }

    @Override
    public void enterRuleref(final ANTLRv4Parser.RulerefContext ctx) {
        final String text = ctx.RULE_REF().getText();
        this.current.append(new Reference(this.current, text, this.unparser));
        super.enterRuleref(ctx);
    }

    @Override
    public void exitRuleref(final ANTLRv4Parser.RulerefContext ctx) {
        //nothing to do yet, since "Ruleref" is a leaf node that doesn't have children.
        super.exitRuleref(ctx);
    }

    @Override
    public void enterTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        TerminalNode terminalNode = ctx.TOKEN_REF();
        if (terminalNode == null) {
            terminalNode = ctx.STRING_LITERAL();
        }
        this.current.append(new Terminal(this.current, terminalNode.getText()));
        super.enterTerminalDef(ctx);
    }

    @Override
    public void exitTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        // nothing to do yet, since "TerminalDef" is a leaf node that doesn't have children.
        super.exitTerminalDef(ctx);
    }

    public Unparser unparser() {
        return this.unparser;
    }
}
