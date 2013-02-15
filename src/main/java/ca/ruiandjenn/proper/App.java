package ca.ruiandjenn.proper;

import org.apache.commons.lang.StringUtils;

public class App 
{
    public static void main( String[] args )
    {
    	boolean verbose = false;
    	String command;
    	
    	if (args.length == 2) {
    		if ("-v".equals(args[0])) {
    			verbose = true;
    			command = args[1];
    		}
    	}
    	else if (args.length != 1) {
    		System.err.println("Usage: ca.ruiandjenn.proper.App [-v] <command>");
    		System.exit(-1);
    	}
    	
    	command = args[1];
    	
    	for (Command c : Command.commands) {
    		if (StringUtils.equals(command, c.getName())) {
    			c.setVerbose(verbose);
    			c.run();
    			break;
    		}
    	}

    }
}

