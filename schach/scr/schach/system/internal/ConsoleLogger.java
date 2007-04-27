package schach.system.internal;

import java.io.PrintStream;

import schach.system.ILogger;

public class ConsoleLogger implements ILogger {

	private void print(PrintStream out, String string){
		out.println(string);
	}
	
	public void debug(String string) {
		print(System.out, "DEBUG: "+string);
	}

	public void error(String string) {
		print(System.err, "ERROR: "+string);
	}

	public void info(String string) {
		print(System.out, "INFO: "+string);
	}

	public void warning(String string) {
		print(System.err, "WARNING: "+string);
	}

}
