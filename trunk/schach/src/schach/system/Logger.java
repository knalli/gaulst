package schach.system;

import java.util.ArrayList;
import java.util.List;

import schach.system.internal.ConsoleLogger;

public class Logger {
	private static List<ILogger> loggers = new ArrayList<ILogger>();
	
	public static void appendLogger(String name){
		if(name.equals("console"))
			loggers.add(new ConsoleLogger());
	}
	
	public static void info(String string){
		for(ILogger logger : loggers){
			logger.info(string);
		}
	}
	
	public static void error(String string){
		for(ILogger logger : loggers){
			logger.error(string);
		}
	}
	
	public static void warning(String string){
		for(ILogger logger : loggers){
			logger.warning(string);
		}
	}
	
	public static void debug(String string){
		for(ILogger logger : loggers){
			logger.debug(string);
		}
	}
	
	public static void test(String string){
		for(ILogger logger : loggers){
			logger.test(string);
		}
	}
}
