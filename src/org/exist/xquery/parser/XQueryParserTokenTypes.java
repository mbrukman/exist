// $ANTLR 2.7.4: "XQuery.g" -> "XQueryParser.java"$

	package org.exist.xquery.parser;

	import antlr.debug.misc.*;
	import java.io.StringReader;
	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Iterator;
	import java.util.Stack;
	import org.exist.storage.BrokerPool;
	import org.exist.storage.DBBroker;
	import org.exist.storage.analysis.Tokenizer;
	import org.exist.EXistException;
	import org.exist.dom.DocumentSet;
	import org.exist.dom.DocumentImpl;
	import org.exist.dom.QName;
	import org.exist.security.PermissionDeniedException;
	import org.exist.security.User;
	import org.exist.xquery.*;
	import org.exist.xquery.value.*;
	import org.exist.xquery.functions.*;

public interface XQueryParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int QNAME = 4;
	int PREDICATE = 5;
	int FLWOR = 6;
	int PARENTHESIZED = 7;
	int ABSOLUTE_SLASH = 8;
	int ABSOLUTE_DSLASH = 9;
	int WILDCARD = 10;
	int PREFIX_WILDCARD = 11;
	int FUNCTION = 12;
	int UNARY_MINUS = 13;
	int UNARY_PLUS = 14;
	int XPOINTER = 15;
	int XPOINTER_ID = 16;
	int VARIABLE_REF = 17;
	int VARIABLE_BINDING = 18;
	int ELEMENT = 19;
	int ATTRIBUTE = 20;
	int TEXT = 21;
	int VERSION_DECL = 22;
	int NAMESPACE_DECL = 23;
	int DEF_NAMESPACE_DECL = 24;
	int DEF_FUNCTION_NS_DECL = 25;
	int GLOBAL_VAR = 26;
	int FUNCTION_DECL = 27;
	int PROLOG = 28;
	int ATOMIC_TYPE = 29;
	int MODULE = 30;
	int ORDER_BY = 31;
	int POSITIONAL_VAR = 32;
	int BEFORE = 33;
	int AFTER = 34;
	int MODULE_DECL = 35;
	int LITERAL_xpointer = 36;
	int LPAREN = 37;
	int RPAREN = 38;
	int NCNAME = 39;
	int LITERAL_module = 40;
	int LITERAL_namespace = 41;
	int EQ = 42;
	int STRING_LITERAL = 43;
	int SEMICOLON = 44;
	int XQUERY = 45;
	int VERSION = 46;
	int LITERAL_declare = 47;
	int LITERAL_default = 48;
	int LITERAL_function = 49;
	int LITERAL_variable = 50;
	int LITERAL_element = 51;
	int DOLLAR = 52;
	int LCURLY = 53;
	int RCURLY = 54;
	int LITERAL_import = 55;
	int LITERAL_at = 56;
	int LITERAL_as = 57;
	int COMMA = 58;
	int LITERAL_empty = 59;
	int QUESTION = 60;
	int STAR = 61;
	int PLUS = 62;
	int LITERAL_item = 63;
	int LITERAL_for = 64;
	int LITERAL_let = 65;
	int LITERAL_some = 66;
	int LITERAL_every = 67;
	int LITERAL_if = 68;
	int LITERAL_where = 69;
	int LITERAL_return = 70;
	int LITERAL_in = 71;
	int COLON = 72;
	int LITERAL_order = 73;
	int LITERAL_by = 74;
	int LITERAL_ascending = 75;
	int LITERAL_descending = 76;
	int LITERAL_greatest = 77;
	int LITERAL_least = 78;
	int LITERAL_satisfies = 79;
	int LITERAL_then = 80;
	int LITERAL_else = 81;
	int LITERAL_or = 82;
	int LITERAL_and = 83;
	int LITERAL_cast = 84;
	int LT = 85;
	int GT = 86;
	int LITERAL_eq = 87;
	int LITERAL_ne = 88;
	int LITERAL_lt = 89;
	int LITERAL_le = 90;
	int LITERAL_gt = 91;
	int LITERAL_ge = 92;
	int NEQ = 93;
	int GTEQ = 94;
	int LTEQ = 95;
	int LITERAL_is = 96;
	int LITERAL_isnot = 97;
	int ANDEQ = 98;
	int OREQ = 99;
	int LITERAL_to = 100;
	int MINUS = 101;
	int LITERAL_div = 102;
	int LITERAL_idiv = 103;
	int LITERAL_mod = 104;
	int LITERAL_union = 105;
	int UNION = 106;
	int LITERAL_intersect = 107;
	int LITERAL_except = 108;
	int SLASH = 109;
	int DSLASH = 110;
	int LITERAL_text = 111;
	int LITERAL_node = 112;
	int SELF = 113;
	int XML_COMMENT = 114;
	int XML_PI = 115;
	int LPPAREN = 116;
	int RPPAREN = 117;
	int AT = 118;
	int PARENT = 119;
	int LITERAL_child = 120;
	int LITERAL_self = 121;
	int LITERAL_attribute = 122;
	int LITERAL_descendant = 123;
	// "descendant-or-self" = 124
	// "following-sibling" = 125
	int LITERAL_following = 126;
	int LITERAL_parent = 127;
	int LITERAL_ancestor = 128;
	// "ancestor-or-self" = 129
	// "preceding-sibling" = 130
	int DOUBLE_LITERAL = 131;
	int DECIMAL_LITERAL = 132;
	int INTEGER_LITERAL = 133;
	int LITERAL_comment = 134;
	// "processing-instruction" = 135
	// "document-node" = 136
	int END_TAG_START = 137;
	int QUOT = 138;
	int ATTRIBUTE_CONTENT = 139;
	int ELEMENT_CONTENT = 140;
	int XML_COMMENT_END = 141;
	int XML_PI_END = 142;
	int LITERAL_document = 143;
	int LITERAL_collection = 144;
	int LITERAL_preceding = 145;
	int XML_PI_START = 146;
	int LETTER = 147;
	int DIGITS = 148;
	int HEX_DIGITS = 149;
	int NMSTART = 150;
	int NMCHAR = 151;
	int WS = 152;
	int EXPR_COMMENT = 153;
	int PREDEFINED_ENTITY_REF = 154;
	int CHAR_REF = 155;
	int NEXT_TOKEN = 156;
	int CHAR = 157;
	int BASECHAR = 158;
	int IDEOGRAPHIC = 159;
	int COMBINING_CHAR = 160;
	int DIGIT = 161;
	int EXTENDER = 162;
}
