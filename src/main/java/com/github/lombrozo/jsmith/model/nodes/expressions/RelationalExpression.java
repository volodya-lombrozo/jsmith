package com.github.lombrozo.jsmith.model.nodes.expressions;

import com.github.lombrozo.jsmith.model.STKey;
import com.github.lombrozo.jsmith.model.ScopeTable;
import com.github.lombrozo.jsmith.model.nodes.Node;
import com.github.lombrozo.jsmith.utils.RandomGen;

import java.util.ArrayList;
import java.util.List;

//relationalExpression
//        :	shiftExpression
//        |	relationalExpression '<' shiftExpression
//        |	relationalExpression '>' shiftExpression
//        |	relationalExpression '<=' shiftExpression
//        |	relationalExpression '>=' shiftExpression
//        |	relationalExpression 'instanceof' referenceType
//        ;

public class RelationalExpression implements Node {

    private ShiftExpression shiftExpression;
    private RelationalExpression relationalExpression;

    private static List<String> operators =  new ArrayList<String>() {{
        add("<");
        add(">");
        add("<=");
        add(">=");
//        add("instanceof");
    }};
    private boolean unary;
    private String operator;

    RelationalExpression(STKey key, ScopeTable scopeTable) {
        this.unary = true;

        this.shiftExpression = new ShiftExpression(key, scopeTable);

        if (!this.unary) {
            this.relationalExpression = new RelationalExpression(key, scopeTable);
            this.operator = this.operators.get(RandomGen.getNextInt(this.operators.size()));
        }
    }

    @Override
    public String produce() {
        if (this.unary) {
            return this.verify(shiftExpression.produce());
        } else {
            String prod = this.relationalExpression.produce() + this.operator + this.shiftExpression.produce();
            return this.verify(prod);
        }
    }
}
