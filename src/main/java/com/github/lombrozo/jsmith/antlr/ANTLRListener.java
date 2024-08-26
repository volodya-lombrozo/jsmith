package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.ANTLRv4Parser;
import com.github.lombrozo.jsmith.ANTLRv4ParserBaseListener;
import com.github.lombrozo.jsmith.antlr.rules.ActionBlock;
import com.github.lombrozo.jsmith.antlr.rules.AltList;
import com.github.lombrozo.jsmith.antlr.rules.Alternative;
import com.github.lombrozo.jsmith.antlr.rules.Atom;
import com.github.lombrozo.jsmith.antlr.rules.Block;
import com.github.lombrozo.jsmith.antlr.rules.BlockSuffix;
import com.github.lombrozo.jsmith.antlr.rules.CharacterRange;
import com.github.lombrozo.jsmith.antlr.rules.Ebnf;
import com.github.lombrozo.jsmith.antlr.rules.EbnfSuffix;
import com.github.lombrozo.jsmith.antlr.rules.Element;
import com.github.lombrozo.jsmith.antlr.rules.ElementOption;
import com.github.lombrozo.jsmith.antlr.rules.Identifier;
import com.github.lombrozo.jsmith.antlr.rules.LabeledAlt;
import com.github.lombrozo.jsmith.antlr.rules.LabeledElement;
import com.github.lombrozo.jsmith.antlr.rules.LexerAtom;
import com.github.lombrozo.jsmith.antlr.rules.LexerCharSet;
import com.github.lombrozo.jsmith.antlr.rules.LexerElement;
import com.github.lombrozo.jsmith.antlr.rules.LexerElements;
import com.github.lombrozo.jsmith.antlr.rules.LexerRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.ParserRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.PredicateOption;
import com.github.lombrozo.jsmith.antlr.rules.PredicateOptions;
import com.github.lombrozo.jsmith.antlr.rules.RecursionException;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.rules.RuleAltList;
import com.github.lombrozo.jsmith.antlr.rules.RuleBlock;
import com.github.lombrozo.jsmith.antlr.rules.RuleDefinition;
import com.github.lombrozo.jsmith.antlr.rules.Ruleref;
import com.github.lombrozo.jsmith.antlr.rules.TerminalDef;
import com.github.lombrozo.jsmith.antlr.representation.ProductionsChain;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public final class ANTLRListener extends ANTLRv4ParserBaseListener {

    private final Unparser unparser = new Unparser();
    private RuleDefinition current = new Root();

    public Unparser unparser() {
        return this.unparser;
    }

    // Every rule!
    @Override
    public void enterEveryRule(final ParserRuleContext ctx) {
        RuleDefinition parent = this.current;
        int size = 0;
        while (!(parent instanceof Root)) {
            parent = parent.parent();
            size++;
        }
        if (size > 50) {
            throw new RecursionException(
                String.format(
                    "Recursion detected in rule: %n%s%n",
                    new ProductionsChain(this.current).tree()
                )
            );
        }
        super.enterEveryRule(ctx);
    }

    @Override
    public void exitEveryRule(final ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
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
        final ParserRuleSpec rule = new ParserRuleSpec(ctx.RULE_REF().getText(), this.current);
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
    public void enterAltList(final ANTLRv4Parser.AltListContext ctx) {
        final RuleDefinition list = new AltList(this.current);
        this.current.append(list);
        this.current = list;
        super.enterAltList(ctx);
    }

    @Override
    public void exitAltList(final ANTLRv4Parser.AltListContext ctx) {
        this.current = this.current.parent();
        super.exitAltList(ctx);
    }

    @Override
    public void enterRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        final RuleDefinition list = new RuleAltList(this.current);
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
        final RuleDefinition alternative = new Alternative(this.current);
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
        final RuleDefinition element = new Element(this.current);
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
        final RuleDefinition atom = new Atom(this.current);
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
        final Ruleref ruleref = new Ruleref(this.current, text, this.unparser);
        this.current.append(ruleref);
        this.current = ruleref;
        super.enterRuleref(ctx);
    }

    @Override
    public void exitRuleref(final ANTLRv4Parser.RulerefContext ctx) {
        this.current = this.current.parent();
        super.exitRuleref(ctx);
    }

    @Override
    public void enterTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        ctx.STRING_LITERAL();
        this.current.append(new TerminalDef(this.current, this.unparser, ctx.getText()));
        super.enterTerminalDef(ctx);
    }

    @Override
    public void exitTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        // nothing to do yet, since "TerminalDef" is a leaf node that doesn't have children.
        super.exitTerminalDef(ctx);
    }
// --------------------
// EBNF and blocks

    @Override
    public void enterEbnf(final ANTLRv4Parser.EbnfContext ctx) {
        final Ebnf ebnf = new Ebnf(this.current);
        this.current.append(ebnf);
        this.current = ebnf;
        super.enterEbnf(ctx);
    }

    @Override
    public void exitEbnf(final ANTLRv4Parser.EbnfContext ctx) {
        this.current = this.current.parent();
        super.exitEbnf(ctx);
    }

    @Override
    public void enterEbnfSuffix(final ANTLRv4Parser.EbnfSuffixContext ctx) {
        this.current.append(
            new EbnfSuffix(
                this.current,
                Stream.of(ctx.QUESTION(0), ctx.STAR(), ctx.PLUS())
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Can't find appropriate terminal"))
                    .getText(),
                Optional.ofNullable(ctx.QUESTION(1)).map(ParseTree::getText).orElse(null)
            )
        );
        super.enterEbnfSuffix(ctx);
    }

    @Override
    public void exitEbnfSuffix(final ANTLRv4Parser.EbnfSuffixContext ctx) {
        // nothing to do yet, since "EbnfSuffix" is a leaf node that doesn't have children.
        super.exitEbnfSuffix(ctx);
    }

    // -------------
// Grammar Block
    @Override
    public void enterBlock(final ANTLRv4Parser.BlockContext ctx) {
        final Block block = new Block(this.current);
        this.current.append(block);
        this.current = block;
        super.enterBlock(ctx);
    }

    @Override
    public void exitBlock(final ANTLRv4Parser.BlockContext ctx) {
        this.current = this.current.parent();
        super.exitBlock(ctx);
    }

    @Override
    public void enterBlockSuffix(final ANTLRv4Parser.BlockSuffixContext ctx) {
        final BlockSuffix suffix = new BlockSuffix(this.current);
        this.current.append(suffix);
        this.current = suffix;
        super.enterBlockSuffix(ctx);
    }

    @Override
    public void exitBlockSuffix(final ANTLRv4Parser.BlockSuffixContext ctx) {
        this.current = this.current.parent();
        super.exitBlockSuffix(ctx);
    }


    //    LEXER RULES
    @Override
    public void enterLexerRuleSpec(final ANTLRv4Parser.LexerRuleSpecContext ctx) {
        final String text = ctx.TOKEN_REF().getText();
        final LexerRuleSpec rule = new LexerRuleSpec(this.current, text);
        this.unparser.withLexerRule(rule);
        this.current.append(rule);
        this.current = rule;
        super.enterLexerRuleSpec(ctx);
    }

    @Override
    public void exitLexerRuleSpec(final ANTLRv4Parser.LexerRuleSpecContext ctx) {
        this.current = this.current.parent();
        super.exitLexerRuleSpec(ctx);
    }

    @Override
    public void enterLexerAltList(final ANTLRv4Parser.LexerAltListContext ctx) {
        final RuleDefinition list = new AltList(this.current);
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
        final RuleDefinition alternative = new Alternative(this.current);
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
        final RuleDefinition elements = new LexerElements(this.current);
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
        final RuleDefinition element = new LexerElement(this.current);
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
        final RuleDefinition atom = new LexerAtom(this.current);
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


    // --------------------
// Rule Alts
    @Override
    public void enterLabeledElement(final ANTLRv4Parser.LabeledElementContext ctx) {
        final RuleDefinition element = new LabeledElement(this.current);
        this.current.append(element);
        this.current = element;
        super.enterLabeledElement(ctx);
    }

    @Override
    public void exitLabeledElement(final ANTLRv4Parser.LabeledElementContext ctx) {
        this.current = this.current.parent();
        super.exitLabeledElement(ctx);
    }


    @Override
    public void enterIdentifier(final ANTLRv4Parser.IdentifierContext ctx) {
        final RuleDefinition identifier = new Identifier(this.current);
        this.current.append(identifier);
        this.current = identifier;
        super.enterIdentifier(ctx);
    }

    @Override
    public void exitIdentifier(final ANTLRv4Parser.IdentifierContext ctx) {
        this.current = this.current.parent();
        super.exitIdentifier(ctx);
    }

    @Override
    public void enterActionBlock(final ANTLRv4Parser.ActionBlockContext ctx) {
        final RuleDefinition block = new ActionBlock(this.current);
        this.current.append(block);
        this.current = block;
        super.enterActionBlock(ctx);
    }

    @Override
    public void exitActionBlock(final ANTLRv4Parser.ActionBlockContext ctx) {
        this.current = this.current.parent();
        super.exitActionBlock(ctx);
    }

    @Override
    public void enterPredicateOptions(final ANTLRv4Parser.PredicateOptionsContext ctx) {
        final RuleDefinition options = new PredicateOptions(this.current);
        this.current.append(options);
        this.current = options;
        super.enterPredicateOptions(ctx);
    }

    @Override
    public void exitPredicateOptions(final ANTLRv4Parser.PredicateOptionsContext ctx) {
        this.current = this.current.parent();
        super.exitPredicateOptions(ctx);
    }

    @Override
    public void enterPredicateOption(final ANTLRv4Parser.PredicateOptionContext ctx) {
        final RuleDefinition option = new PredicateOption(this.current);
        this.current.append(option);
        this.current = option;
        super.enterPredicateOption(ctx);
    }

    @Override
    public void exitPredicateOption(final ANTLRv4Parser.PredicateOptionContext ctx) {
        this.current = this.current.parent();
        super.exitPredicateOption(ctx);
    }

    @Override
    public void enterElementOption(final ANTLRv4Parser.ElementOptionContext ctx) {
        final RuleDefinition option = new ElementOption(this.current);
        this.current.append(option);
        this.current = option;
        super.enterElementOption(ctx);
    }

    @Override
    public void exitElementOption(final ANTLRv4Parser.ElementOptionContext ctx) {
        this.current = this.current.parent();
        super.exitElementOption(ctx);
    }

    @Override
    public void enterRuleBlock(final ANTLRv4Parser.RuleBlockContext ctx) {
        final RuleDefinition block = new RuleBlock(this.current);
        this.current.append(block);
        this.current = block;
        super.enterRuleBlock(ctx);
    }

    @Override
    public void exitRuleBlock(final ANTLRv4Parser.RuleBlockContext ctx) {
        this.current = this.current.parent();
        super.exitRuleBlock(ctx);
    }

    @Override
    public void enterLabeledAlt(final ANTLRv4Parser.LabeledAltContext ctx) {
        final RuleDefinition alt = new LabeledAlt(this.current);
        this.current.append(alt);
        this.current = alt;
        super.enterLabeledAlt(ctx);
    }

    @Override
    public void exitLabeledAlt(final ANTLRv4Parser.LabeledAltContext ctx) {
        this.current = this.current.parent();
        super.exitLabeledAlt(ctx);
    }
}
