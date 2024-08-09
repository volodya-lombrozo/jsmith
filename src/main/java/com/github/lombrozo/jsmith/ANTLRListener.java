package com.github.lombrozo.jsmith;

public final class ANTLRListener extends ANTLRv4ParserBaseListener {

    private final Unparser unparser = new Unparser("expr");

    @Override
    public void enterGrammarSpec(final ANTLRv4Parser.GrammarSpecContext ctx) {
        super.enterGrammarSpec(ctx);
        final UnparserRule expr = new UnparserRule("expr");
        this.unparser.withRule(expr);
//        ctx.grammarDecl();
//        ctx.prequelConstruct();
//        ctx.rules();
//        ctx.modeSpec();
//        ctx.EOF();
    }

    public Unparser unparser() {
        return this.unparser;
    }
}
