package hr.fer.zemris.nenr.fuzzy.command.parser;

import hr.fer.zemris.nenr.fuzzy.command.IFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.command.SetOperatorCommand;
import hr.fer.zemris.nenr.fuzzy.command.WriteCommand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistCompositionSetCommand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistCylindricalSetCommand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistDomainCommand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistExpressionSetComand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistFunctionSetCommand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistProjectionSetCommand;
import hr.fer.zemris.nenr.fuzzy.command.persist.PersistSimpleSetCommand;
import hr.fer.zemris.nenr.fuzzy.command.test.EquivalenceTestCommand;
import hr.fer.zemris.nenr.fuzzy.command.test.ReflexivityTestCommand;
import hr.fer.zemris.nenr.fuzzy.command.test.SymmetryTestCommand;
import hr.fer.zemris.nenr.fuzzy.command.test.TransitivityTestCommand;
import hr.fer.zemris.nenr.fuzzy.domain.CartesianDomain;
import hr.fer.zemris.nenr.fuzzy.domain.EnumeratedDomain;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.domain.IntegerDomain;
import hr.fer.zemris.nenr.fuzzy.domain.RealDomain;
import hr.fer.zemris.nenr.fuzzy.set.operator.AlgebraicT;
import hr.fer.zemris.nenr.fuzzy.set.operator.HammacherS;
import hr.fer.zemris.nenr.fuzzy.set.operator.HammacherT;
import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;
import hr.fer.zemris.nenr.fuzzy.set.operator.MamdaniMin;
import hr.fer.zemris.nenr.fuzzy.set.operator.ZadehNot;
import hr.fer.zemris.nenr.fuzzy.set.operator.ZadehS;
import hr.fer.zemris.nenr.fuzzy.set.operator.ZadehT;

/**
 * A class which contains static methods for parsing
 * commands for a fuzzy engine.
 * @author Luka Sterbic
 * @version 0.3
 */
public class FuzzyCommandParser {
	
	/**
	 * Parse the given line
	 * @param line string representation of the command
	 * @return an IFuzzyCommand object which encapsulates the given command
	 * @throws FuzzyParserException if an error arises while parsing the command
	 */
	public static IFuzzyCommand parse(String line) throws FuzzyParserException {
		if(line == null) {
			throw new FuzzyParserException("The command string must not be null!");
		}
		
		IFuzzyCommand command = null;
		
		line = line.replaceAll(",\\s+", ",");
		line = line.replaceAll("write", "w write");
		line = line.replaceAll("test", "t test ");
		line = line.replaceAll("(\\s+)?\\+(\\s+)?", "+");
		line = line.replaceAll("(\\s+)?\\*(\\s+)?", "*");
		line = line.replaceAll("(\\s+)?->(\\s+)?", ">");		
		line = line.replaceAll("(\\s+)?!(\\s+)?", "!");		
		
		if(line.startsWith("set operator")) {
			line = line.substring(0, 12) + " " + line.charAt(12) + " " + line.substring(13);
		}
		
		String[] parts = line.split("\\s+");
		
		try {
		switch(parts[1]) {
			case "domain":
				IDomain domain = parseDomain(parts);
				command = new PersistDomainCommand(domain);
				break;
				
			case "fuzzyset":
				command = parseSet(parts);
				break;
				
			case "operator":
				command = parseOperator(parts);
				break;
				
			case "write":
				command = new WriteCommand(parts[2]);
				break;
				
			case "test":
				command = parseTest(parts);
				break;
	
			default:
				throw new FuzzyParserException("Unknown fuzzy command!");
			}
		} catch(FuzzyParserException ex) {
			throw ex;
		} catch(Exception ex) {
			throw new FuzzyParserException(ex.getMessage());
		}
		
		return command;
	}
	
	private static IBinaryOperator getOperatorForName(String opName) throws FuzzyParserException {
		switch(opName.substring(1, opName.length() - 1)) {
		case "max-min":
			return new ZadehT("*");
			
		case "max-product":
			return new AlgebraicT("*");
			
		default:
			throw new FuzzyParserException("Unknown transitivity operator!");
		}
	}
	
	private static IFuzzyCommand parseTest(String[] tokens) throws FuzzyParserException {
		String[] parts = tokens[3].split(",");
		String setName = parts[0];
		
		IBinaryOperator operator = null;
		
		if(parts.length > 1) {
			operator = getOperatorForName(parts[1]);
		}
		
		switch(tokens[2]) {
		case "Symmetric":
			return new SymmetryTestCommand(setName);
			
		case "Reflexive":
			return new ReflexivityTestCommand(setName);
			
		case "Transitive":
			return new TransitivityTestCommand(setName, operator);
			
		case "FuzzyEquivalence":
			return new EquivalenceTestCommand(setName, operator);

		default:
			throw new FuzzyParserException("Unknown test!");
		}
	}

	private static IFuzzyCommand parseOperator(String[] tokens) throws FuzzyParserException {
		if(!tokens[0].equals("set") || !tokens[3].equals("to")) {
			throw new FuzzyParserException("Illegal command format!");
		}
		
		String sign = tokens[2];
		String opID = tokens[4];
		IOperator operator;
		
		switch(opID) {
		case "ZadehS":
			operator = new ZadehS(sign);
			break;
			
		case "ZadehT":
			operator = new ZadehT(sign);
			break;
			
		case "ZadehNot":
			operator = new ZadehNot(sign);
			break;
			
		case "Mamdani":
			operator = new MamdaniMin(sign);
			break;

		default:
			try {
				double v = Double.parseDouble(opID.substring(11, opID.length() - 1));
				
				if(opID.startsWith("HammacherS")) {
					operator = new HammacherS(sign, v);
					break;
				} else if(opID.startsWith("HammacherT")) {
					operator = new HammacherT(sign ,v);
					break;
				}
			} catch(Exception ignorable) {}
			
			throw new FuzzyParserException("Unknown operator!");
		}
			
		return new SetOperatorCommand(operator);
	}

	private static IFuzzyCommand parseSet(String[] tokens) throws FuzzyParserException {
		String name = tokens[0];
		
		if(!name.endsWith(":")) {
			throw new FuzzyParserException("Illegal fuzzy set name!");
		}
		
		name = name.substring(0, name.length() - 1);
		
		if(tokens[2].equals("expr")) {
			return new PersistExpressionSetComand(name, tokens[3]);
		} else if(tokens[2].startsWith("expr")) {
			return new PersistExpressionSetComand(name, tokens[2].substring(4));
		} else if(tokens[2].equals("cylindrical_extension")) {
			if(!tokens[4].equals("@")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			return new PersistCylindricalSetCommand(name, tokens[3], tokens[5]);
		} else if(tokens[2].equals("project")) {
			if(!tokens[4].equals("@")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			return new PersistProjectionSetCommand(name, tokens[3], tokens[5]);
		} else if(tokens[2].equals("composition")) {
			if(!tokens[4].equals("using")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			String[] sets = tokens[3].split(",");
			IBinaryOperator minOperator = getOperatorForName(tokens[5]);
			
			return new PersistCompositionSetCommand(name, sets[0], sets[1], minOperator);
		} else {
			String domain = tokens[3];
			String isWhat = tokens[5];
			
			if(!tokens[2].equals("@") || !tokens[4].equals("is")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			if(isWhat.startsWith("{")) {
				isWhat = isWhat.substring(1, isWhat.length() - 1);
				return new PersistSimpleSetCommand(name, domain, isWhat);
			} else {
				return new PersistFunctionSetCommand(name, domain, isWhat);
			}
		}
	}

	/**
	 * Parse the given tokens to a domain object
	 * @param tokens the tokens
	 * @return a domain object
	 * @throws FuzzyParserException in case of parsing errors
	 */
	private static IDomain parseDomain(String[] tokens) throws FuzzyParserException {
		String name = tokens[0];
		String type = tokens[2];
		
		if(!name.endsWith(":")) {
			throw new FuzzyParserException("Illegal domain name!");
		}
		
		name = name.substring(0, name.length() - 1);
		IDomain domain = null;
		
		switch(type) {
		case "enumerated":
			if(!tokens[3].startsWith("{") || !tokens[3].endsWith("}")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			String[] enums = tokens[3].substring(1, tokens[3].length() - 1).split(",");
			domain = new EnumeratedDomain(name, enums);
			break;
			
		case "integer":
			int fromInt = Integer.parseInt(tokens[3]);
			int toInt = Integer.parseInt(tokens[5]);
			int stepInt = Integer.parseInt(tokens[7]);
			
			if(!tokens[4].equals("to") || !tokens[6].equals("step")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			domain = new IntegerDomain(name, fromInt, toInt, stepInt);
			break;
			
		case "real":
			double fromReal = Double.parseDouble(tokens[3]);
			double toReal = Double.parseDouble(tokens[5]);
			double stepReal = Double.parseDouble(tokens[7]);
			
			if(!tokens[4].equals("to") || !tokens[6].equals("step")) {
				throw new FuzzyParserException("Illegal command format!");
			}
			
			domain = new RealDomain(name, fromReal, toReal, stepReal);
			break;
			
		case "cartesian":
			String[] domains = tokens[3].split(",");
			domain = new CartesianDomain(name, domains);
			break;

		default:
			throw new FuzzyParserException("Unknown domain type!");
		}
		
		return domain;
	}

}
