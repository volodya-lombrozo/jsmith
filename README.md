# jsmith

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.volodya-lombrozo/jsmith/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.volodya-lombrozo/jsmith)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/volodya-lombrozo/jsmith/blob/main/LICENSE.txt)
[![Hits-of-Code](https://hitsofcode.com/github/volodya-lombrozo/jsmith?branch=main&label=Hits-of-Code)](https://hitsofcode.com/github/volodya-lombrozo/jsmith/view?branch=main&label=Hits-of-Code)
[![codecov](https://codecov.io/gh/volodya-lombrozo/jsmith/branch/main/graph/badge.svg)](https://codecov.io/gh/volodya-lombrozo/jsmith)

Jsmith is a random Java program generator. The project is largely inspired by
[csmith](https://github.com/csmith-project/csmith), a tool for the C language.
The primary purpose of the library is to provide classes for generating random
Java programs to test Java compilers or translators.

If you need to generate random Java programs for any other purpose, you can also
give Jsmith a try.

## How to Add

The library is available on Maven Central. To add it to your project, add the
following snippet to your `pom.xml`:

```xml

<dependency>
  <groupId>com.github.volodya-lombrozo</groupId>
  <artifactId>jsmith</artifactId>
  <version>0.0.3</version>
</dependency>
```

## How to Use

The library provides a set of classes for generating random Java programs. To
generate a random class you can use the following code:

```java
import com.github.lombrozo.jsmith.RandomJavaClass;

public class App {
    public static void main(String... args) {
        RandomJavaClass clazz = new RandomJavaClass();
        // Generate source code of the class
        String code = clazz.src();
        System.out.println("Generated code:" + code);
    }
}
```

## Under the Hood

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

```antlrv4
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

## Semantic Aware Generation

### Not Implemented Yet

The library can generate the simple Java programs that syntactically correct.
However, these programs are not always semantically correct.
It means that
if you try to compile them, you most probably will get a compilation error.

#### Semantic Labels

To generate semantically correct programs, we can enhance the grammar with
labels:

```antlrv4
grammar Arithmetic;

prog: stat+ ;

stat: expr NEWLINE
    | ID '=' expr NEWLINE #ID_VariableDeclaration
    | NEWLINE
    ;

expr: expr ('*' | '/' ) expr
    | expr ('+' | '-' ) expr
    | INT
    | ID #ID_VariableUsage
    | '(' expr ')'
    ;

ID  : [a-zA-Z]+ ;
INT : [0-9]+ ;
NEWLINE: '\r'? '\n' ;
WS  : [ \t]+ -> skip ;
```

Pay attention to the `#ID_VariableDeclaration` and `#ID_VariableUsage` labels.
These labels will allow us to generate semantically correct programs.
For example, we can generate a program that declares a variable and uses it:

```txt
A = 123
B = (A+123)
C = (B*123)
```

In other words, by using labels, we can guide program generation to produce
semantically correct programs.

#### Semantic Specifications

Another possible approach is to use semantic specifications.
Semantic specification is a set of rules that define the semantics of the
generated program.
In other words, we can define a syntax generation by using ANTLR grammar
and semantic generation by using semantic specifications.

For example, we can define the following semantic specification for the
Arithmetic grammar:

```yaml
- name: VariableDeclaration
  match:
    rule: stat
    for:
      - name: ID
  action: declare_variable

- name: VariableUsage
  match:
    rule: expr
    for:
      - name: ID
  action: use_variable
```

Of course, they are just the first examples of how we can generate semantically
correct programs.
We need to define more complex semantic specifications to generate more complex
programs and try both approaches to see which one is more effective.

## How to Contribute

Fork repository, make changes, send us a pull request. We will review your
changes and apply them to the `main` branch shortly, provided they don't violate
our quality standards. To avoid frustration,
before sending us your pull request, please run full Maven build:

```bash
$ mvn clean install -Pqulice
```

You will need [Maven 3.3+](https://maven.apache.org) and Java 8+ installed.