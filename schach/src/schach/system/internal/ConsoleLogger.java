package schach.system.internal;

import java.io.PrintStream;

import schach.system.ILogger;

public class ConsoleLogger implements ILogger {
	
	private boolean onlytest = false;
	private boolean notest = false;

	private void print(PrintStream out, String string){
		out.println(string);
	}
	
	public void debug(String string) {
		if(!onlytest)
			print(System.out, "DEBUG: "+string);
	}

	public void error(String string) {
		if(!onlytest)
			print(System.err, "ERROR: "+string);
	}

	public void info(String string) {
		if(!onlytest)
			print(System.out, "INFO: "+string);
	}

	public void warning(String string) {
		if(!onlytest)
			print(System.err, "WARNING: "+string);
	}

	public void test(String string) {
		if(notest)
			print(System.out, "TEST: "+string);
	}

	public void disableAllExceptTest() {
		onlytest = true;
		notest  = false;
	}

	public void noTest() {
		notest = true;
	}
}
