import java.util.List;
import java.util.ArrayList;

public class BracketTest {

	enum BufferState {EMPTY, FLUSH_NEEDED, NON_EMPTY};
	
	public interface BracketBuffer {
		void		init();
		BufferState	increment(char bracket);
		String		flush();
	}

	public static String getValidBrackets(String input) {
		String result = new String();
		BracketBuffer scanner = getScanner();
		BufferState state = BufferState.NON_EMPTY;
		
		while (input.length() > 0) {
			for (int i = 0; i < input.length(); i++) {
				state = scanner.increment(input.charAt(i));
				if (state == BufferState.FLUSH_NEEDED) {
					result += scanner.flush();
				}
			}
			input = scanner.flush();
			scanner.init();
		}		
		return result;	
	}
	
	
	
	public static BracketBuffer getScanner() {
		BracketBuffer result = new BracketBuffer() {
			int counter;
			List<Character> buffer;
			
			public void init() {
				this.counter = 0;
				this.buffer = new ArrayList<>();
			}
			
			private BufferState getState() {
				if (buffer.size() == 0) {
					return BufferState.EMPTY;
				} else {
					if (counter == 0) {
						return BufferState.FLUSH_NEEDED;
					} else { 
						// assert counter >= 0. the buffer will always be empty if counter is trying to go beyond zero
						return BufferState.NON_EMPTY;
					}
				} 
			}
			
			public BufferState increment(char bracket) {
				if (bracket == ')') {
					counter--;
				} else if (bracket == '(') {
					counter++;
				}
				if (counter < 0) { 
					counter = 0; // we do not care how many we should skip
				}
				else {
					buffer.add(bracket);
				}
				return getState();
			}
			
			public String flush() {
				int size = buffer.size();
				int shift = 0;
				if (counter > 0) {
					// if it is a trailing flush we don't need the first bracket cause it's non valid open bracket
					size--;
					shift++;
				}
				StringBuilder builder = new StringBuilder(size);
				for (int i = 0; i < size; i++)
				{
					builder.append(buffer.get(i + shift));
				}
				buffer.clear();
				return builder.toString();
			}		
		};
		result.init();
		return result;
	}
	
	public static void printFormatted(String output) {
		System.out.println(String.valueOf(output.length()) + (output.length() > 0 ? " - " + output : ""));
	}

	public static void main(String[] args) {
	
               printFormatted(getValidBrackets("(()"));
               printFormatted(getValidBrackets(")()())"));
               printFormatted(getValidBrackets(")(()())"));
               printFormatted(getValidBrackets(")("));
               printFormatted(getValidBrackets("())(()())(()"));
                              
	}
}
