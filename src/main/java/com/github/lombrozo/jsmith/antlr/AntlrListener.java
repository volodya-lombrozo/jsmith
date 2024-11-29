/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Volodya Lombrozo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.lombrozo.jsmith.antlr;

import com.github.lombrozo.jsmith.ANTLRv4Lexer;
import com.github.lombrozo.jsmith.ANTLRv4Parser;
import com.github.lombrozo.jsmith.ANTLRv4ParserBaseListener;
import com.github.lombrozo.jsmith.antlr.rules.Action;
import com.github.lombrozo.jsmith.antlr.rules.ActionBlock;
import com.github.lombrozo.jsmith.antlr.rules.ActionScopeName;
import com.github.lombrozo.jsmith.antlr.rules.AltList;
import com.github.lombrozo.jsmith.antlr.rules.Alternative;
import com.github.lombrozo.jsmith.antlr.rules.ArgActionBlock;
import com.github.lombrozo.jsmith.antlr.rules.Atom;
import com.github.lombrozo.jsmith.antlr.rules.Block;
import com.github.lombrozo.jsmith.antlr.rules.BlockSet;
import com.github.lombrozo.jsmith.antlr.rules.BlockSuffix;
import com.github.lombrozo.jsmith.antlr.rules.CharacterRange;
import com.github.lombrozo.jsmith.antlr.rules.DelegateGrammar;
import com.github.lombrozo.jsmith.antlr.rules.DelegateGrammars;
import com.github.lombrozo.jsmith.antlr.rules.Ebnf;
import com.github.lombrozo.jsmith.antlr.rules.EbnfSuffix;
import com.github.lombrozo.jsmith.antlr.rules.Element;
import com.github.lombrozo.jsmith.antlr.rules.ElementOption;
import com.github.lombrozo.jsmith.antlr.rules.ElementOptions;
import com.github.lombrozo.jsmith.antlr.rules.Identifier;
import com.github.lombrozo.jsmith.antlr.rules.LabeledAlt;
import com.github.lombrozo.jsmith.antlr.rules.LabeledElement;
import com.github.lombrozo.jsmith.antlr.rules.LexerAlt;
import com.github.lombrozo.jsmith.antlr.rules.LexerAltList;
import com.github.lombrozo.jsmith.antlr.rules.LexerAtom;
import com.github.lombrozo.jsmith.antlr.rules.LexerBlock;
import com.github.lombrozo.jsmith.antlr.rules.LexerCharSet;
import com.github.lombrozo.jsmith.antlr.rules.LexerCommand;
import com.github.lombrozo.jsmith.antlr.rules.LexerCommandExpr;
import com.github.lombrozo.jsmith.antlr.rules.LexerCommandName;
import com.github.lombrozo.jsmith.antlr.rules.LexerCommands;
import com.github.lombrozo.jsmith.antlr.rules.LexerElement;
import com.github.lombrozo.jsmith.antlr.rules.LexerElements;
import com.github.lombrozo.jsmith.antlr.rules.LexerRuleBlock;
import com.github.lombrozo.jsmith.antlr.rules.LexerRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.Literal;
import com.github.lombrozo.jsmith.antlr.rules.NotSet;
import com.github.lombrozo.jsmith.antlr.rules.Option;
import com.github.lombrozo.jsmith.antlr.rules.OptionValue;
import com.github.lombrozo.jsmith.antlr.rules.OptionsSpec;
import com.github.lombrozo.jsmith.antlr.rules.ParserRuleSpec;
import com.github.lombrozo.jsmith.antlr.rules.PredicateOption;
import com.github.lombrozo.jsmith.antlr.rules.PredicateOptions;
import com.github.lombrozo.jsmith.antlr.rules.Root;
import com.github.lombrozo.jsmith.antlr.rules.Rule;
import com.github.lombrozo.jsmith.antlr.rules.RuleAltList;
import com.github.lombrozo.jsmith.antlr.rules.RuleBlock;
import com.github.lombrozo.jsmith.antlr.rules.Ruleref;
import com.github.lombrozo.jsmith.antlr.rules.Safe;
import com.github.lombrozo.jsmith.antlr.rules.SetElement;
import com.github.lombrozo.jsmith.antlr.rules.TerminalDef;
import com.github.lombrozo.jsmith.antlr.rules.Traced;
import com.github.lombrozo.jsmith.antlr.semantic.ScopeRule;
import com.github.lombrozo.jsmith.antlr.semantic.UniqueRule;
import com.github.lombrozo.jsmith.antlr.semantic.VariableDeclaration;
import com.github.lombrozo.jsmith.antlr.semantic.VariableInitialization;
import com.github.lombrozo.jsmith.antlr.semantic.VariableTarget;
import com.github.lombrozo.jsmith.antlr.semantic.VariableUsage;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * ANTLR listener.
 * The listener is used to parse ANTLR grammar and build a tree of rules.
 * @since 0.1
 * @checkstyle ClassFanOutComplexityCheck (1000 lines)
 */
@SuppressWarnings({
    "PMD.UselessOverridingMethod",
    "PMD.ExcessivePublicCount",
    "PMD.TooManyMethods",
    "PMD.AvoidFieldNameMatchingMethodName",
    "PMD.GodClass"
})
public final class AntlrListener extends ANTLRv4ParserBaseListener {

    /**
     * Unparser with a collection of parser rules.
     */
    private final Unparser unparser;

    /**
     * Unlexer with a collection of lexer rules.
     */
    private final Unlexer unlexer;

    /**
     * Token stream.
     */
    private final BufferedTokenStream tokens;

    /**
     * All declared identifiers.
     */
    private final Set<String> identifiers;

    /**
     * Current rule.
     */
    private Rule current;

    /**
     * Constructor.
     * @param tokens Token stream.
     * @param unparser Unparser.
     * @param unlexer Unlexer.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    public AntlrListener(
        final BufferedTokenStream tokens,
        final Unparser unparser,
        final Unlexer unlexer
    ) {
        this(tokens, unparser, unlexer, new Root());
    }

    /**
     * Constructor.
     * @param tokens Token stream.
     * @param unparser Unparser.
     * @param unlexer Unlexer.
     * @param root Current rule.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    private AntlrListener(
        final BufferedTokenStream tokens,
        final Unparser unparser,
        final Unlexer unlexer,
        final Rule root
    ) {
        this.tokens = tokens;
        this.unparser = unparser;
        this.unlexer = unlexer;
        this.current = new Traced(root);
        this.identifiers = new JavaKeywords().toSet();
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
        final String name = ctx.RULE_REF().getText();
        final JsmithComments comments = new JsmithComments(
            this.tokens.getHiddenTokensToRight(ctx.getStart().getTokenIndex(), ANTLRv4Lexer.COMMENT)
        );
        final Rule rule;
        if (comments.has(ScopeRule.COMMENT)) {
            rule = new ScopeRule(new ParserRuleSpec(name, this.current));
        } else {
            rule = new ParserRuleSpec(name, this.current);
        }
        this.unparser.with(name, rule);
        this.down(rule);
        super.enterParserRuleSpec(ctx);
    }

    @Override
    public void exitParserRuleSpec(final ANTLRv4Parser.ParserRuleSpecContext ctx) {
        this.up();
        super.exitParserRuleSpec(ctx);
    }

    @Override
    public void enterAltList(final ANTLRv4Parser.AltListContext ctx) {
        this.down(new AltList(this.current));
        super.enterAltList(ctx);
    }

    @Override
    public void exitAltList(final ANTLRv4Parser.AltListContext ctx) {
        this.up();
        super.exitAltList(ctx);
    }

    @Override
    public void enterRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        this.down(new RuleAltList(this.current));
        super.enterRuleAltList(ctx);
    }

    @Override
    public void exitRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        this.up();
        super.exitRuleAltList(ctx);
    }

    @Override
    public void enterAlternative(final ANTLRv4Parser.AlternativeContext ctx) {
        this.down(new Alternative(this.current));
        super.enterAlternative(ctx);
    }

    @Override
    public void exitAlternative(final ANTLRv4Parser.AlternativeContext ctx) {
        this.up();
        super.exitAlternative(ctx);
    }

    @Override
    public void enterElement(final ANTLRv4Parser.ElementContext ctx) {
        Rule res = new Element(this.current);
        final JsmithComments comments = new JsmithComments(
            this.tokens.getHiddenTokensToLeft(ctx.getStart().getTokenIndex(), ANTLRv4Lexer.COMMENT)
        );
        if (comments.has(VariableUsage.COMMENT)) {
            res = new VariableUsage(res);
        }
        if (comments.has(VariableDeclaration.COMMENT)) {
            res = new VariableDeclaration(res);
        }
        if (comments.has(VariableTarget.COMMENT)) {
            res = new VariableTarget(res);
        }
        this.down(res);
        super.enterElement(ctx);
    }

    @Override
    public void exitElement(final ANTLRv4Parser.ElementContext ctx) {
        this.up();
        super.exitElement(ctx);
    }

    @Override
    public void enterAtom(final ANTLRv4Parser.AtomContext ctx) {
        this.down(new Atom(this.current));
        super.enterAtom(ctx);
    }

    @Override
    public void exitAtom(final ANTLRv4Parser.AtomContext ctx) {
        this.up();
        super.exitAtom(ctx);
    }

    @Override
    public void enterRuleref(final ANTLRv4Parser.RulerefContext ctx) {
        this.down(new Ruleref(this.current, ctx.getText(), this.unparser));
        super.enterRuleref(ctx);
    }

    @Override
    public void exitRuleref(final ANTLRv4Parser.RulerefContext ctx) {
        this.up();
        super.exitRuleref(ctx);
    }

    @Override
    public void enterTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        final JsmithComments comments = new JsmithComments(
            this.tokens.getHiddenTokensToLeft(
                ctx.getStart().getTokenIndex(), ANTLRv4Lexer.COMMENT
            )
        );
        Rule rule = new TerminalDef(this.current, this.unlexer, ctx.getText());
        if (comments.has(UniqueRule.COMMENT)) {
            rule = new UniqueRule(rule, this.identifiers);
        }
        this.current.append(rule);
        super.enterTerminalDef(ctx);
    }

    @Override
    public void exitTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        super.exitTerminalDef(ctx);
    }

    @Override
    public void enterEbnf(final ANTLRv4Parser.EbnfContext ctx) {
        this.down(new Ebnf(this.current));
        super.enterEbnf(ctx);
    }

    @Override
    public void exitEbnf(final ANTLRv4Parser.EbnfContext ctx) {
        this.up();
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
        super.exitEbnfSuffix(ctx);
    }

    @Override
    public void enterBlock(final ANTLRv4Parser.BlockContext ctx) {
        this.down(new Block(this.current));
        super.enterBlock(ctx);
    }

    @Override
    public void exitBlock(final ANTLRv4Parser.BlockContext ctx) {
        this.up();
        super.exitBlock(ctx);
    }

    @Override
    public void enterBlockSuffix(final ANTLRv4Parser.BlockSuffixContext ctx) {
        this.down(new BlockSuffix(this.current));
        super.enterBlockSuffix(ctx);
    }

    @Override
    public void exitBlockSuffix(final ANTLRv4Parser.BlockSuffixContext ctx) {
        this.up();
        super.exitBlockSuffix(ctx);
    }

    @Override
    public void enterLexerRuleSpec(final ANTLRv4Parser.LexerRuleSpecContext ctx) {
        final String name = ctx.TOKEN_REF().getText();
        final LexerRuleSpec rule = new LexerRuleSpec(this.current, name);
        this.unlexer.with(name, rule);
        this.down(rule);
        super.enterLexerRuleSpec(ctx);
    }

    @Override
    public void exitLexerRuleSpec(final ANTLRv4Parser.LexerRuleSpecContext ctx) {
        this.up();
        super.exitLexerRuleSpec(ctx);
    }

    @Override
    public void enterLexerAltList(final ANTLRv4Parser.LexerAltListContext ctx) {
        this.down(new LexerAltList(this.current));
        super.enterLexerAltList(ctx);
    }

    @Override
    public void exitLexerAltList(final ANTLRv4Parser.LexerAltListContext ctx) {
        this.up();
        super.exitLexerAltList(ctx);
    }

    @Override
    public void enterLexerAlt(final ANTLRv4Parser.LexerAltContext ctx) {
        this.down(new LexerAlt(this.current));
        super.enterLexerAlt(ctx);
    }

    @Override
    public void exitLexerAlt(final ANTLRv4Parser.LexerAltContext ctx) {
        this.up();
        super.exitLexerAlt(ctx);
    }

    @Override
    public void enterLexerElements(final ANTLRv4Parser.LexerElementsContext ctx) {
        this.down(new LexerElements(this.current));
        super.enterLexerElements(ctx);
    }

    @Override
    public void exitLexerElements(final ANTLRv4Parser.LexerElementsContext ctx) {
        this.up();
        super.exitLexerElements(ctx);
    }

    @Override
    public void enterLexerElement(final ANTLRv4Parser.LexerElementContext ctx) {
        final Rule element = new LexerElement(this.current);
        this.down(element);
        super.enterLexerElement(ctx);
        if (Objects.nonNull(ctx.QUESTION())) {
            element.append(new EbnfSuffix("?"));
        }
    }

    @Override
    public void exitLexerElement(final ANTLRv4Parser.LexerElementContext ctx) {
        this.up();
        super.exitLexerElement(ctx);
    }

    @Override
    public void enterLexerAtom(final ANTLRv4Parser.LexerAtomContext ctx) {
        final Rule atom = new LexerAtom(this.current);
        if (ctx.LEXER_CHAR_SET() != null) {
            atom.append(new LexerCharSet(atom, ctx.LEXER_CHAR_SET().getText()));
        } else if (ctx.DOT() != null) {
            atom.append(new Literal(ctx.DOT().getText()));
        }
        this.down(atom);
        super.enterLexerAtom(ctx);
    }

    @Override
    public void exitLexerAtom(final ANTLRv4Parser.LexerAtomContext ctx) {
        this.up();
        super.exitLexerAtom(ctx);
    }

    @Override
    public void enterAction_(final ANTLRv4Parser.Action_Context ctx) {
        this.down(new Action(this.current));
        super.enterAction_(ctx);
    }

    @Override
    public void exitAction_(final ANTLRv4Parser.Action_Context ctx) {
        this.up();
        super.exitAction_(ctx);
    }

    @Override
    public void enterCharacterRange(final ANTLRv4Parser.CharacterRangeContext ctx) {
        this.current.append(new CharacterRange(this.current, ctx.getText()));
        super.enterCharacterRange(ctx);
    }

    @Override
    public void exitCharacterRange(final ANTLRv4Parser.CharacterRangeContext ctx) {
        super.exitCharacterRange(ctx);
    }

    @Override
    public void enterLabeledElement(final ANTLRv4Parser.LabeledElementContext ctx) {
        this.down(new LabeledElement(this.current));
        super.enterLabeledElement(ctx);
    }

    @Override
    public void exitLabeledElement(final ANTLRv4Parser.LabeledElementContext ctx) {
        this.up();
        super.exitLabeledElement(ctx);
    }

    @Override
    public void enterIdentifier(final ANTLRv4Parser.IdentifierContext ctx) {
        this.down(new Identifier(this.current));
        super.enterIdentifier(ctx);
    }

    @Override
    public void exitIdentifier(final ANTLRv4Parser.IdentifierContext ctx) {
        this.up();
        super.exitIdentifier(ctx);
    }

    @Override
    public void enterActionBlock(final ANTLRv4Parser.ActionBlockContext ctx) {
        this.down(new ActionBlock(this.current));
        super.enterActionBlock(ctx);
    }

    @Override
    public void exitActionBlock(final ANTLRv4Parser.ActionBlockContext ctx) {
        this.up();
        super.exitActionBlock(ctx);
    }

    @Override
    public void enterPredicateOptions(final ANTLRv4Parser.PredicateOptionsContext ctx) {
        this.down(new PredicateOptions(this.current));
        super.enterPredicateOptions(ctx);
    }

    @Override
    public void exitPredicateOptions(final ANTLRv4Parser.PredicateOptionsContext ctx) {
        this.up();
        super.exitPredicateOptions(ctx);
    }

    @Override
    public void enterPredicateOption(final ANTLRv4Parser.PredicateOptionContext ctx) {
        this.down(new PredicateOption(this.current));
        super.enterPredicateOption(ctx);
    }

    @Override
    public void exitPredicateOption(final ANTLRv4Parser.PredicateOptionContext ctx) {
        this.up();
        super.exitPredicateOption(ctx);
    }

    @Override
    public void enterElementOption(final ANTLRv4Parser.ElementOptionContext ctx) {
        this.down(new ElementOption(this.current));
        super.enterElementOption(ctx);
    }

    @Override
    public void exitElementOption(final ANTLRv4Parser.ElementOptionContext ctx) {
        this.up();
        super.exitElementOption(ctx);
    }

    @Override
    public void enterRuleBlock(final ANTLRv4Parser.RuleBlockContext ctx) {
        this.down(new RuleBlock(this.current));
        super.enterRuleBlock(ctx);
    }

    @Override
    public void exitRuleBlock(final ANTLRv4Parser.RuleBlockContext ctx) {
        this.up();
        super.exitRuleBlock(ctx);
    }

    @Override
    public void enterLabeledAlt(final ANTLRv4Parser.LabeledAltContext ctx) {
        final JsmithComments comments = new JsmithComments(
            this.tokens.getHiddenTokensToLeft(
                ctx.getStart().getTokenIndex(), ANTLRv4Lexer.COMMENT
            )
        );
        final Rule res;
        final LabeledAlt main = new LabeledAlt(this.current, ctx.getText());
        if (comments.has(VariableInitialization.COMMENT)) {
            res = new VariableInitialization(main);
        } else {
            res = main;
        }
        this.down(res);
        super.enterLabeledAlt(ctx);
    }

    @Override
    public void exitLabeledAlt(final ANTLRv4Parser.LabeledAltContext ctx) {
        this.up();
        super.exitLabeledAlt(ctx);
    }

    @Override
    public void enterActionScopeName(final ANTLRv4Parser.ActionScopeNameContext ctx) {
        this.down(new ActionScopeName(this.current));
        super.enterActionScopeName(ctx);
    }

    @Override
    public void exitActionScopeName(final ANTLRv4Parser.ActionScopeNameContext ctx) {
        this.up();
        super.exitActionScopeName(ctx);
    }

    @Override
    public void enterArgActionBlock(final ANTLRv4Parser.ArgActionBlockContext ctx) {
        this.down(new ArgActionBlock(this.current));
        super.enterArgActionBlock(ctx);
    }

    @Override
    public void exitArgActionBlock(final ANTLRv4Parser.ArgActionBlockContext ctx) {
        this.up();
        super.exitArgActionBlock(ctx);
    }

    @Override
    public void enterDelegateGrammar(final ANTLRv4Parser.DelegateGrammarContext ctx) {
        this.down(new DelegateGrammar(this.current));
        super.enterDelegateGrammar(ctx);
    }

    @Override
    public void exitDelegateGrammar(final ANTLRv4Parser.DelegateGrammarContext ctx) {
        this.up();
        super.exitDelegateGrammar(ctx);
    }

    @Override
    public void enterDelegateGrammars(final ANTLRv4Parser.DelegateGrammarsContext ctx) {
        this.down(new DelegateGrammars(this.current));
        super.enterDelegateGrammars(ctx);
    }

    @Override
    public void exitDelegateGrammars(final ANTLRv4Parser.DelegateGrammarsContext ctx) {
        this.up();
        super.exitDelegateGrammars(ctx);
    }

    @Override
    public void enterElementOptions(final ANTLRv4Parser.ElementOptionsContext ctx) {
        this.down(new ElementOptions(this.current));
        super.enterElementOptions(ctx);
    }

    @Override
    public void exitElementOptions(final ANTLRv4Parser.ElementOptionsContext ctx) {
        this.up();
        super.exitElementOptions(ctx);
    }

    @Override
    public void enterLexerBlock(final ANTLRv4Parser.LexerBlockContext ctx) {
        this.down(new LexerBlock(this.current));
        super.enterLexerBlock(ctx);
    }

    @Override
    public void exitLexerBlock(final ANTLRv4Parser.LexerBlockContext ctx) {
        this.up();
        super.exitLexerBlock(ctx);
    }

    @Override
    public void enterNotSet(final ANTLRv4Parser.NotSetContext ctx) {
        this.down(new NotSet(this.current));
        super.enterNotSet(ctx);
    }

    @Override
    public void exitNotSet(final ANTLRv4Parser.NotSetContext ctx) {
        this.up();
        super.exitNotSet(ctx);
    }

    @Override
    public void enterLexerCommands(final ANTLRv4Parser.LexerCommandsContext ctx) {
        this.down(new LexerCommands(this.current));
        super.enterLexerCommands(ctx);
    }

    @Override
    public void exitLexerCommands(final ANTLRv4Parser.LexerCommandsContext ctx) {
        this.up();
        super.exitLexerCommands(ctx);
    }

    @Override
    public void enterLexerCommand(final ANTLRv4Parser.LexerCommandContext ctx) {
        this.down(new LexerCommand(this.current));
        super.enterLexerCommand(ctx);
    }

    @Override
    public void exitLexerCommand(final ANTLRv4Parser.LexerCommandContext ctx) {
        this.up();
        super.exitLexerCommand(ctx);
    }

    @Override
    public void enterLexerCommandName(final ANTLRv4Parser.LexerCommandNameContext ctx) {
        this.down(new LexerCommandName(this.current));
        super.enterLexerCommandName(ctx);
    }

    @Override
    public void exitLexerCommandName(final ANTLRv4Parser.LexerCommandNameContext ctx) {
        this.up();
        super.exitLexerCommandName(ctx);
    }

    @Override
    public void enterLexerCommandExpr(final ANTLRv4Parser.LexerCommandExprContext ctx) {
        this.down(new LexerCommandExpr(this.current));
        super.enterLexerCommandExpr(ctx);
    }

    @Override
    public void exitLexerCommandExpr(final ANTLRv4Parser.LexerCommandExprContext ctx) {
        this.up();
        super.exitLexerCommandExpr(ctx);
    }

    @Override
    public void enterLexerRuleBlock(final ANTLRv4Parser.LexerRuleBlockContext ctx) {
        this.down(new LexerRuleBlock(this.current));
        super.enterLexerRuleBlock(ctx);
    }

    @Override
    public void exitLexerRuleBlock(final ANTLRv4Parser.LexerRuleBlockContext ctx) {
        this.up();
        super.exitLexerRuleBlock(ctx);
    }

    @Override
    public void enterOptionsSpec(final ANTLRv4Parser.OptionsSpecContext ctx) {
        this.down(new OptionsSpec(this.current));
        super.enterOptionsSpec(ctx);
    }

    @Override
    public void exitOptionsSpec(final ANTLRv4Parser.OptionsSpecContext ctx) {
        this.up();
        super.exitOptionsSpec(ctx);
    }

    @Override
    public void enterOption(final ANTLRv4Parser.OptionContext ctx) {
        this.down(new Option(this.current));
        super.enterOption(ctx);
    }

    @Override
    public void exitOption(final ANTLRv4Parser.OptionContext ctx) {
        this.up();
        super.exitOption(ctx);
    }

    @Override
    public void enterOptionValue(final ANTLRv4Parser.OptionValueContext ctx) {
        this.down(new OptionValue(this.current));
        super.enterOptionValue(ctx);
    }

    @Override
    public void exitOptionValue(final ANTLRv4Parser.OptionValueContext ctx) {
        this.up();
        super.exitOptionValue(ctx);
    }

    @Override
    public void enterBlockSet(final ANTLRv4Parser.BlockSetContext ctx) {
        this.down(new BlockSet(this.current));
        super.enterBlockSet(ctx);
    }

    @Override
    public void exitBlockSet(final ANTLRv4Parser.BlockSetContext ctx) {
        this.up();
        super.exitBlockSet(ctx);
    }

    @Override
    public void enterSetElement(final ANTLRv4Parser.SetElementContext ctx) {
        final SetElement set = new SetElement(this.current);
        if (ctx.LEXER_CHAR_SET() != null) {
            set.append(new LexerCharSet(set, ctx.LEXER_CHAR_SET().getText()));
        }
        if (ctx.STRING_LITERAL() != null) {
            set.append(new Literal(ctx.STRING_LITERAL().getText()));
        }
        this.down(set);
        super.enterSetElement(ctx);
    }

    @Override
    public void exitSetElement(final ANTLRv4Parser.SetElementContext ctx) {
        this.up();
        super.exitSetElement(ctx);
    }

    /**
     * Go down in the generation tree.
     * @param rule Rule to go down.
     */
    private void down(final Rule rule) {
        this.current.append(new Traced(new Safe(rule)));
        this.current = rule;
    }

    /**
     * Go up in the generation tree.
     * @checkstyle MethodNameCheck (5 lines)
     */
    @SuppressWarnings("PMD.ShortMethodName")
    private void up() {
        this.current = this.current.parent();
    }
}
