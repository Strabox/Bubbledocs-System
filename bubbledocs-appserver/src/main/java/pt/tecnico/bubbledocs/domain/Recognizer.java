package pt.tecnico.bubbledocs.domain;

import java.util.regex.Pattern;

import pt.tecnico.bubbledocs.exceptions.BadContentExpressionException;
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public class Recognizer {
	final static String ZERO = "0";
	 final static String NEGATIVE = "-(?!0)\\d+";
	 final static String POSITIVE = "(?!0)\\d+";
	 final static String LITERAL = "(" + NEGATIVE + "|" + ZERO + "|" + POSITIVE + ")";

	 final static String CELL = POSITIVE + ";" + POSITIVE;
	 final static String PARSE_CELL = ";";
	 final static String RANGE = CELL + ":" + CELL;
	 final static String PARSE_RANGE = ":";
	 final static String BINARY_OPERATOR = "(ADD|SUB|MUL|DIV)";
	 final static String ARGUMENT = "(" + LITERAL + "|" + CELL + ")";
	 final static String BINARY_FUNCTION = "=" + BINARY_OPERATOR + "\\(" + ARGUMENT + "," + ARGUMENT + "\\)";
	 final static String PARSE_BINARY_FUNCTION = "[=(,)]";
	 final static String RANGE_OPERATOR = "(AVG|PRD)";
	 final static String RANGE_FUNCTION = "=" + RANGE_OPERATOR + "\\(" + RANGE+ "\\)";
	 final static String PARSE_RANGE_FUNCTION = "[=()]";
	 
	 public static Content recogniseBinaryFunction(String pattern, SpreadSheet sheet) throws BubbleDocsException{
		 if(!Pattern.matches(BINARY_FUNCTION, pattern)){
			 throw new BadContentExpressionException();
		 }
		 String[] tokens = pattern.split(PARSE_BINARY_FUNCTION);
		 String operand = tokens[0];
		 
		 BinaryFunction bf;
		 Content leftarg;
		 Content rightarg;
		 try{
			 if(Pattern.matches(CELL, tokens[1])){
				 String[] left = tokens[1].split(PARSE_CELL);
				 int x = Integer.parseInt(left[0]);
				 int y = Integer.parseInt(left[1]);
				 leftarg = new Reference(sheet.forceGetSingleCell(x, y));
			 }
			 else{
				leftarg = new Literal(Integer.parseInt(tokens[1]));
			 }
		 }
		 catch(Exception e){
			 throw new BadContentExpressionException();
		 }
		 try{
			 if(Pattern.matches(CELL, tokens[2])){
				 String[] right = tokens[2].split(PARSE_CELL);
				 int x = Integer.parseInt(right[0]);
				 int y = Integer.parseInt(right[1]);
				 rightarg = new Reference(sheet.forceGetSingleCell(x, y));
			 }
			 else{
				rightarg = new Literal(Integer.parseInt(tokens[2]));				 
			 }
		 }
		 catch(Exception e){
			 throw new BadContentExpressionException();
		 }
		 if(operand.equals("ADD"))
			 bf = new ADD(leftarg,rightarg);
		 else if(operand.equals("SUB"))
			 bf = new SUB(leftarg,rightarg);
		 else if(operand.equals("MUL"))
			 bf = new MUL(leftarg,rightarg);
		 else if(operand.equals("DIV"))
			 bf = new DIV(leftarg,rightarg);
		 else throw new BadContentExpressionException();
		 return bf;
	}
}
