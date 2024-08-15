package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.ANTLRv4Parser;
import com.github.lombrozo.jsmith.ANTLRv4ParserBaseListener;
import com.github.lombrozo.jsmith.UnlexerRule;
import com.github.lombrozo.jsmith.Unparser;
import com.github.lombrozo.jsmith.UnparserRule;
import java.util.Optional;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class ANTLRListener extends ANTLRv4ParserBaseListener {

    private final Unparser unparser = new Unparser("expr");
    private Generative current = new Root();

    public Unparser unparser() {
        return this.unparser;
    }


    @Override
    public void enterGrammarSpec(final ANTLRv4Parser.GrammarSpecContext ctx) {
        super.enterGrammarSpec(ctx);
    }

    @Override
    public void enterGrammarDecl(final ANTLRv4Parser.GrammarDeclContext ctx) {
        super.enterGrammarDecl(ctx);
    }

    @Override
    public void enterRules(final ANTLRv4Parser.RulesContext ctx) {
        super.enterRules(ctx);
    }

    @Override
    public void enterRuleSpec(final ANTLRv4Parser.RuleSpecContext ctx) {
        super.enterRuleSpec(ctx);
    }

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
    public void exitAltList(final ANTLRv4Parser.AltListContext ctx) {
        this.current = this.current.parent();
        super.exitAltList(ctx);
    }

    @Override
    public void enterRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        final Generative list = new AltList(this.current);
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
        final String text = ctx.getText();
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
        this.current.append(new TerminalDef(this.current, this.unparser, ctx.getText()));
        super.enterTerminalDef(ctx);
    }

    @Override
    public void exitTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        // nothing to do yet, since "TerminalDef" is a leaf node that doesn't have children.
        super.exitTerminalDef(ctx);
    }


//    LEXER RULES

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
    public void exitLexerAltList(final ANTLRv4Parser.LexerAltListContext ctx) {
        this.current = this.current.parent();
        super.exitLexerAltList(ctx);
    }

    @Override
    public void enterLexerAlt(final ANTLRv4Parser.LexerAltContext ctx) {
        final Generative alternative = new Alternative(this.current);
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
    public void enterLexerElements(final ANTLRv4Parser.LexerElementsContext ctx) {
        final Generative elements = new Elements(this.current);
        this.current.append(elements);
        this.current = elements;
        super.enterLexerElements(ctx);
    }

    @Override
    public void exitLexerElements(final ANTLRv4Parser.LexerElementsContext ctx) {
        this.current = this.current.parent();
        super.exitLexerElements(ctx);
    }

    @Override
    public void enterLexerElement(final ANTLRv4Parser.LexerElementContext ctx) {
        final Generative element = new LexerElement(this.current);
        this.current.append(element);
        this.current = element;
        super.enterLexerElement(ctx);
    }

    @Override
    public void exitLexerElement(final ANTLRv4Parser.LexerElementContext ctx) {
        this.current = this.current.parent();
        super.exitLexerElement(ctx);
    }

    @Override
    public void enterLexerAtom(final ANTLRv4Parser.LexerAtomContext ctx) {
        final Generative atom = new LexerAtom(this.current);
        if (ctx.LEXER_CHAR_SET() != null) {
            final String text = ctx.LEXER_CHAR_SET().getText();
            atom.append(new LexerCharSet(atom, text));
        }
//        Todo?
//        else if (ctx.DOT() != null) {
//            atom.append(new LexerCharSet(atom, ctx.DOT().getText()));
//        }
        this.current.append(atom);
        this.current = atom;
        super.enterLexerAtom(ctx);
    }


    @Override
    public void exitLexerAtom(final ANTLRv4Parser.LexerAtomContext ctx) {
        this.current = this.current.parent();
        super.exitLexerAtom(ctx);
    }


    @Override
    public void enterCharacterRange(final ANTLRv4Parser.CharacterRangeContext ctx) {
        final String text = ctx.getText();
        this.current.append(new CharacterRange(this.current, text));
        super.enterCharacterRange(ctx);
    }

    @Override
    public void exitCharacterRange(final ANTLRv4Parser.CharacterRangeContext ctx) {
        // nothing to do yet, since "CharacterRange" is a leaf node that doesn't have children.
        super.exitCharacterRange(ctx);
    }

    @Override
    public void enterEbnfSuffix(final ANTLRv4Parser.EbnfSuffixContext ctx) {
        final TerminalNode question = ctx.QUESTION(0);
        final TerminalNode star = ctx.STAR();
        final TerminalNode plus = ctx.PLUS();
        final TerminalNode secondq = ctx.QUESTION(1);
        final TerminalNode terminal = Optional.ofNullable(question)
            .orElse(Optional.ofNullable(star)
                .orElse(Optional.ofNullable(plus).orElseThrow(
                    () -> new IllegalStateException("Can't find appropriate terminal"))));
        final String text = terminal.getText();
        final String question1 = Optional.ofNullable(secondq).map(ParseTree::getText).orElse("");
        this.current.append(
            new EbnfSuffix(
                this.current,
                text,
                question1
            )
        );
        super.enterEbnfSuffix(ctx);
    }

    @Override
    public void exitEbnfSuffix(final ANTLRv4Parser.EbnfSuffixContext ctx) {
        // nothing to do yet, since "EbnfSuffix" is a leaf node that doesn't have children.
        super.exitEbnfSuffix(ctx);
    }
}
