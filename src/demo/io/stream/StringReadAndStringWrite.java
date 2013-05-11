package demo.io.stream;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * StringReader StringWrite
 * 
 * @author fule
 * 
 */
public class StringReadAndStringWrite {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String string = "dasdasdasdas\n" + "sadsadasdas\n" + "asdasd";
		StringReader sReader = new StringReader(string);
		char[] ch = new char[32];
		int hasread = 0;
		while ((hasread = sReader.read(ch)) > 0) {
			System.out.println(new String(ch, 0, hasread));
		}
		sReader.close();

		StringWriter sWriter = new StringWriter(32);
		sWriter.write("121212\n");
		sWriter.write("121212");
		System.out.println(sWriter.toString());
		sWriter.close();
	}

}
