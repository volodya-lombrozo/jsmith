# Under the Hood

jsmith uses
an [ANTLR grammar]([Java8ReducedParser.g4](src%2Fmain%2Fresources%2Fgrammars%2FJava8ReducedParser.g4))
to generate random Java programs.
It scans the grammar and creates a random program generator based on the grammar
rules.
Later, this random program generator is used to produce random Java
programs.
In fact, jsmith can generate programs in any language, not just Java.
Below is an example of the `Arithmetic` grammar and an explanation of how to
generate random programs based on this grammar:

```antlr
grammar Arithmetic;

prog: stat+ ;

stat: expr NEWLINE
    | ID '=' expr NEWLINE
    | NEWLINE
    ;

expr: expr ('*' | '/' ) expr
    | expr ('+' | '-' ) expr
    | INT
    | ID
    | '(' expr ')'
    ;

ID  : [a-zA-Z]+ ;
INT : [0-9]+ ;
NEWLINE: '\r'? '\n' ;
WS  : [ \t]+ -> skip ;
```

```java
import com.github.lombrozo.jsmith.RandomScript;

public class App {
    public static void main(final String... args) {
        final RandomScript script = new RandomScript(
            new ResourceOf("grammars/Arithmetic.g4")
        );
        // 'stat' is the name of the start rule in the grammar
        for (int i = 0; i < 10; ++i) {
            final String code = script.generate("stat");
            System.out.println(code);
        }
    }
}
```

This code will generate random arithmetic expressions that are syntactically
correct:

```text
((78692*(414))*(68))*(705*((51777)))
JJ=((762)*08511*(4)*(36*3)+((87082)*(48)+501)+(1*41088)*(((24)))*(42*(8892))+((7604*(16)))*(93416*(4780)))+(((6355)*99559*2828+6853*(6*69501)+80409*37*17602*4*029))+(((1944*2+06*47*0))*((1418+82*(11)))*29874+((4805*10731*6439+7507)))+(((287*81457+659*7020*587*80*(32603+5587))*((8274*1738*12910*193))+515*65498*(6+89*62793)))+(((31*21710+(0710)*99*527+80509*46496*084*(3)*51*81218+(97024*693*81*09674)+94486*609+(538*6667)*453*(45973)+48*(36908*69924*(4589+3*3*4*9+4*663)))))
hf=158*49799
73*15*62865
50457
H=3*674*239*1*87
((281))
(((jmWCL/f/ahfrc/(fhtQ))))
d=GAw*IUFjX*Z*INgwx
0527*0106-482*49100*0-381*0292-866-7*79-40-59-9*30*5729*7-90283*021-49
```