package com.github.lombrozo.jsmith;

import org.antlr.v4.runtime.tree.TerminalNode;

public final class ANTLRListener extends ANTLRv4ParserBaseListener {

    private final Unparser unparser = new Unparser("expr");
    private UnparserRule current;

    private AltList currentAltList;

    private Alternative currentAlternative;

    private Element currentElement;
    private Atom currentAtom;

    @Override
    public void enterGrammarSpec(final ANTLRv4Parser.GrammarSpecContext ctx) {
        final UnparserRule expr = new UnparserRule("expr");
        super.enterGrammarSpec(ctx);
//        ctx.grammarDecl();
//        ctx.prequelConstruct();
//        ctx.rules();
//        ctx.modeSpec();
//        ctx.EOF();
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
        final String text = ctx.RULE_REF().getText();
        final UnparserRule rule = new UnparserRule(text);
        this.unparser.withRule(rule);
        this.current = rule;
        super.enterParserRuleSpec(ctx);
    }

    @Override
    public void enterRuleBlock(final ANTLRv4Parser.RuleBlockContext ctx) {
        // Rule description body
        super.enterRuleBlock(ctx);
    }

    @Override
    public void enterRuleAltList(final ANTLRv4Parser.RuleAltListContext ctx) {
        final AltList altList = new AltList();
        this.currentAltList = altList;
        this.current.append(altList);
        super.enterRuleAltList(ctx);
    }

    @Override
    public void enterAlternative(final ANTLRv4Parser.AlternativeContext ctx) {
        final Alternative alternative = new Alternative();
        this.currentAlternative = alternative;
        this.currentAltList.withAlternative(alternative);
        super.enterAlternative(ctx);
    }

    @Override
    public void enterElement(final ANTLRv4Parser.ElementContext ctx) {
        final Element element = new Element();
        currentElement = element;
        currentAlternative.withElement(element);
        super.enterElement(ctx);
    }

    @Override
    public void enterAtom(final ANTLRv4Parser.AtomContext ctx) {
        final Atom atom = new Atom();
        this.currentAtom = atom;
        this.currentElement.withAtom(atom);
        super.enterAtom(ctx);
    }

    @Override
    public void enterRuleref(final ANTLRv4Parser.RulerefContext ctx) {
        final String text = ctx.RULE_REF().getText();
        this.currentAtom.with(new Reference(text));
        super.enterRuleref(ctx);
    }

    @Override
    public void enterTerminalDef(final ANTLRv4Parser.TerminalDefContext ctx) {
        TerminalNode terminalNode = ctx.TOKEN_REF();
        if (terminalNode == null) {
            terminalNode = ctx.STRING_LITERAL();
        }
        this.currentAtom.with(new Terminal(terminalNode.getText()));
        super.enterTerminalDef(ctx);
    }

    public Unparser unparser() {
        return this.unparser;
    }
}
