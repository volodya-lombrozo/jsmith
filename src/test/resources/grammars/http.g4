/*
HTTP grammar.
The MIT License (MIT).

Copyright (c) 2024, Martin Mirchev.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

/*
* This grammar is based on the HTTP/1.1 specification (RFC 7230, RFC 7231).
*/
grammar http;

/*
 HTTP-message = start‑line *( header‑field CRLF ) CRLF [ message‑body ]
 */
http_message
    : start_line (header_field CRLF)* EOF // CRLF message_body
    ;

/*
 start-line = request‑line / status‑line
 */
start_line
    : request_line // status_line
    ;

/*
 request-line = method SP request‑target SP HTTP‑version CRLF
 */
request_line
    : method SP request_target SP http_version CRLF
    ;

/*
 method = token ; "GET" ; → RFC 7231 – Section 4.3.1 ; "HEAD" ; → RFC 7231 – Section 4.3.2 ; "POST"
 ; → RFC 7231 – Section 4.3.3 ; "PUT" ; → RFC 7231 – Section 4.3.4 ; "DELETE" ; → RFC 7231 – Section
 4.3.5 ; "CONNECT" ; → RFC 7231 – Section 4.3.6 ; "OPTIONS" ; → RFC 7231 – Section 4.3.7 ; "TRACE"
 ; → RFC 7231 – Section 4.3.8
 */
method
    : 'GET'
    | 'HEAD'
    | 'POST'
    | 'PUT'
    | 'DELETE'
    | 'CONNECT'
    | 'OPTIONS'
    | 'TRACE'
    ;

/*
 request-target = origin-form / absolute-form / authority-form / asterisk-form
 */
request_target
    : origin_form // absolute_form | authority_form | asterisk_form
    ;

/*
 origin-form = absolute-path  [ "?"  query ]
 */
origin_form
    : absolute_path (QuestionMark query)?
    ;

/*
 absolute-path = 1*( "/" segment )
 */
absolute_path
    : (Slash segment)+
    ;

/*
 segment = *pchar
 */
segment
    : pchar*
    ;

/*
 query = *( pchar /  "/" /  "?" )
 */
query
    : (pchar | Slash | QuestionMark)*
    ;

/*
 HTTP-version = HTTP-name '/' DIGIT  "." DIGIT
 */
http_version
    : http_name Slash DIGIT Dot DIGIT
    ;

/*
 HTTP-name = %x48.54.54.50 ; "HTTP", case-sensitive
 */
http_name
    : 'HTTP'
    ;

/*
 header-field = field-name  ":"  OWS  field-value  OWS
 */
header_field
    : field_name Colon ows field_value ows
    ;

/*
 field-name = token
 */
field_name
    : token
    ;

/*
 token
 */
token
    : tchar+
    ;

/*
 tchar = "!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" /
 "|" / "~" / DIGIT / ALPHA
 */
tchar
    : hexdig
    | ExclamationMark
    | DollarSign
    | Hashtag
    | Percent
    | Ampersand
    | SQuote
    | Star
    | Plus
    | Minus
    | Dot
    | Caret
    | Underscore
    | BackQuote
    | VBar
    | Tilde
    | DIGIT
    | ALPHA
    ;

/*
 field-value = *( field-content / obs-fold )
 */
field_value
    : (field_content | obs_fold)*
    ;

/*
 field-content = field-vchar [ 1*( SP / HTAB ) field-vchar ]
 */
field_content
    : field_vchar ((SP | HTAB)+ field_vchar)*
    ;

/*
 OWS = *( SP / HTAB ) ; optional whitespace
 */
ows : (SP | HTAB)*;

/*
 field-vchar = VCHAR / obs-text
 */
field_vchar
    : vchar
    | obs_text
    ;

/*
 obs-text = %x80-FF
 */
obs_text
    : OBS_TEXT
    ;

/*
 obs-fold = CRLF 1*( SP / HTAB ) ; see RFC 7230 – Section 3.2.4
 */
obs_fold
    : CRLF (SP | HTAB)+
    ;

/*
 message-body = *OCTET
 */
message_body: OCTET*;

/*
 pchar = unreserved / pct‑encoded / sub‑delims / ":" / "@"
 */
pchar
    : unreserved
    | pct_encoded
    | sub_delims
    | hexdig
    | Colon
    | At
    ;

/*
 pct-encoded = "%"  HEXDIG HEXDIG
 */
pct_encoded
    : Percent hexdig hexdig
    ;

hexdig
    : DIGIT
    | 'A'
    | 'B'
    | 'C'
    | 'D'
    | 'E'
    | 'F'
    ;

/*
 unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
 */
unreserved
    : ALPHA
    | DIGIT
    | Minus
    | Dot
    | Underscore
    | Tilde
    ;

/*
 sub-delims = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
 */
sub_delims
    : ExclamationMark
    | DollarSign
    | Ampersand
    | SQuote
    | LColumn
    | RColumn
    | Star
    | Plus
    | Period
    | SemiColon
    | Equals
    ;

vchar
    : hexdig
    | LColumn
    | RColumn
    | SemiColon
    | Equals
    | Period
    | Minus
    | Dot
    | Underscore
    | Tilde
    | QuestionMark
    | Slash
    | ExclamationMark
    | Colon
    | At
    | DollarSign
    | Hashtag
    | Ampersand
    | Percent
    | SQuote
    | Star
    | Plus
    | Caret
    | BackQuote
    | VBar
    | ALPHA
    | DIGIT
    | VCHAR
    ;

LColumn
    : '('
    ;

RColumn
    : ')'
    ;

SemiColon
    : ';'
    ;

Equals
    : '='
    ;

Period
    : ','
    ;

Minus
    : '-'
    ;

Dot
    : '.'
    ;

Underscore
    : '_'
    ;

Tilde
    : '~'
    ;

QuestionMark
    : '?'
    ;

Slash
    : '/'
    ;

ExclamationMark
    : '!'
    ;

Colon
    : ':'
    ;

At
    : '@'
    ;

DollarSign
    : '$'
    ;

Hashtag
    : '#'
    ;

Ampersand
    : '&'
    ;

Percent
    : '%'
    ;

SQuote
    : '\''
    ;

Star
    : '*'
    ;

Plus
    : '+'
    ;

Caret
    : '^'
    ;

BackQuote
    : '`'
    ;

VBar
    : '|'
    ;

/*
 DIGIT = %x30‑39 ; 0-9
 */
DIGIT
    : [0-9]
    ;

/*
 HEXDIG = DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
 */
//HEXDIG
//    : DIGIT
//    | 'A'
//    | 'B'
//    | 'C'
//    | 'D'
//    | 'E'
//    | 'F'
//    ;

/*
 ALPHA = %x41‑5A / %x61‑7A ; A‑Z / a‑z
 */
ALPHA
    : [A-Za-z]
    ;

/*
 VCHAR = %x21-7E ; visible (printing) characters
 */
VCHAR
    : '\u0021' .. '\u007e'
    ;

OBS_TEXT
    : '\u0080' ..'\u00ff'
    ;

/*
 SP = %x20 ; space
 */
SP
    : ' '
    ;

/*
 HTAB = %x09 ; horizontal tab
 */
HTAB
    : '\t'
    ;

/*
 CRLF= CR LF ; Internet standard newline
 */
CRLF
    : '\r\n' | '\n'
    ;

OCTET
    : .
    ;
