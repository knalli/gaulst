package schach.system;

public interface ILogger {
	public void info(String string);
	public void debug(String string);
	public void error(String string);
	public void warning(String string);
	public void test(String string);
}
