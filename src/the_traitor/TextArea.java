
package the_traitor;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
/**
 * class which acts as a text field and text area.
 * @author Bineesh
 */
public class TextArea extends Component  implements FocusableObject {
    
    private boolean CaretBlinking = true;
    private boolean CaretUpdated = false;
    
    private int LastPressedKey = Integer.MIN_VALUE;
    private int CharSelector = 0;
    private int DisplayMode = MULTILINE_MODE;
    
    public int limit = -1;
    public int WritingMode = SMALL_CHAR_MODE;
    public int Type = TEXTAREA_ACTIVE;
    
    private long LastCaretUpdated = 0;
    private long keyDelay = 500L;
    
    private Menu clear = new Menu("Clear", Menu.CLEAR);
    private Menu ok = new Menu("Ok", Menu.OK);
    private Menu t9 = new Menu("T9");
    
    private int caretX = 0, caretY = 0;

    /**
     * Symbols
     */
    public Symbols sym;
    
    public String Title;
    public String[] lines;
    public StringBuffer Content;
    public int height, caretIndex = -1;
    public boolean fixedHeight, showCurser = true;
    
    public Window parent;
    
    public static final int TEXTAREA_INACTIVE = 0;
    public static final int TEXTAREA_ACTIVE = 1;
    
    public static final int SINGLELINE_MODE = 0;
    public static final int MULTILINE_MODE = 1;
    
    public static final int SMALL_CHAR_MODE = 0;
    public static final int CAPITAL_CHAR_MODE = 1;
    public static final int NUM_MODE = 2;
    
    public static final char[] NUM0_CHARS = {' ','0','\n'};
    public static final char[] NUM1_CHARS = {'.',',','?','!','@',':',';','/','1'};
    public static final char[] NUM2_CHARS = {'a','b','c','2'};
    public static final char[] NUM3_CHARS = {'d','e','f','3'};
    public static final char[] NUM4_CHARS = {'g','h','i','4'};
    public static final char[] NUM5_CHARS = {'j','k','l','5'};
    public static final char[] NUM6_CHARS = {'m','n','o','6'};
    public static final char[] NUM7_CHARS = {'p','q','r','s','7'};
    public static final char[] NUM8_CHARS = {'t','u','v','8'};
    public static final char[] NUM9_CHARS = {'w','x','y','z','9'};
    
    public TextArea(String Title, int height, boolean fixedHeight) {
        
        this.Title = Title;
        this.height = height;
        this.Content = new StringBuffer();
        this.fixedHeight = fixedHeight;
        
        sym = new Symbols();
        sym.parent = this;
        
        lines = new String[1];
        lines[0] = "";
    }
    
    public TextArea(String Title) {
        
        this.Title = Title;
        this.Content = new StringBuffer();
        
        sym = new Symbols();
        sym.parent = this;
       
        height = Theme.getInstance().getFHeight();
        DisplayMode = SINGLELINE_MODE;
        fixedHeight = true;
        
        lines = new String[1];
        lines[0] = "";
    }
    
    public int getHeight() {
        Theme t = new Theme();
        return this.height + t.getFHeight() +5;
    }
    /**
     * Sets the TextArea text
     * @param str string which is to be set
     */
    public void setText(String str) {
        if(str == null) {
            str = "";
        }
        
        Content.delete(0, Content.length());
        Content.append(str);
        caretIndex = str.length()-1;
        breakIntoLines(Content.toString(), Theme.WIDTH-10);
        updateCaretXY();
    }
    /**
     * Sets the limit of text area
     * @param limit limit to the text area
     * 
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public void paint (Graphics g) {
       
       Theme t = new Theme();
       
       t.setGraphics(g);
       
       if(focused) {
           parent.paint(g);
           g.translate(0, parent.TranselateY);
           t.drawActiveTextArea(this);
           g.translate(0, -parent.TranselateY);
           
           if(Content.length() != 0 ) {
               t.drawMenu(t9, ok, clear);
           }
           else {
               t.drawMenu(t9, ok, null);
           }
       } else {
           g.translate(0, parent.TranselateY);
           t.drawTextArea(this);
           g.translate(0, -parent.TranselateY);
       }
       
    }
    
    public Menu getLSMenu () {
        return t9;
    }

    public Menu getRSMenu () {
        if(Content.length() != 0 && caretIndex >= 0) {
            return clear;
        }
        return null;
    }

    public Menu getFireMenu () {
        return ok;
    }

    public void next () {
        if(DisplayMode == SINGLELINE_MODE) {
            parent.next();
            return;
        }
        
        caretDown();
        caretIndex = calculateCaretPosition();
    }
    
    private void caretDown () {
        
        int cy;
        
        if(getArrayLength(lines) > caretY + 1) {
            caretY ++;
            cy = getStringIndexOf(lines, caretY);
        }
        else {
            caretY = 0;
            cy = getStringIndexOf(lines, caretY);
        }
        
        if(lines[cy].length() <= caretX) {
            int v = lines[cy].length();
            if(v != 0) {
                caretX = v - 1;
            }
            else {
                caretX = 0;
            }
        }
        
    }

    public void previous () {
        if(DisplayMode == SINGLELINE_MODE) {
            parent.previous();
            return;
        }
        
        caretUp();
        caretIndex = calculateCaretPosition();
    }
    
    private void caretUp () {
        
        if(0 <= caretY - 1) {
            caretY --;
        }
        else {
            caretY = getArrayLength(lines) - 1;
        }
        
        int cy = getStringIndexOf(lines, caretY);
        
        if(lines[cy].length() <= caretX) {
            int v = lines[cy].length();
            if(v != 0) {
                caretX = v - 1;
            }
            else {
                caretX = 0;
            }
        }
        
    }
    /**
     * To get the array length excluding null strings
     * @param ar
     * @return 
     */
    private int getArrayLength(String []ar) {
        
        int j = 0;
        for(int i = 0; i < ar.length; i++) {
            if(ar[i] == null) {
                j ++;
            }
        }
        
        return ar.length - j;
    }
    /**
     * To get the caret Index from x and y
     * @return 
     */
    private int calculateCaretPosition () {
        
        int sum = 0, n = getStringIndexOf(lines, caretY);
        
        for(int i = 0; i < n; i ++) {
            if(lines[i] == null) {
                sum ++;
            }
            else {
                sum += lines[i].length();
            }
        }
        
        
        return sum + caretX - 1;
    }
    
    public boolean isFocusable () {
        return true;
    }
    
    public void left () {
        
        if(caretIndex > -1) {
            caretIndex --;
            updateCaretXY();
        }
        
    }
    
    public void right () {
        
        if(caretIndex < Content.length()-1) {
            caretIndex ++;
            updateCaretXY();
        }
        
    }

    public long run () {

        if(CaretBlinking) {
            if(showCurser) {
                showCurser = false;
            }
            else {
                showCurser = true;
            }
        }
            
        return 500L;
    }

    public void setActive () {
        Traitor.focused = this;
        focused = true;
    }
    
    public void goInactive () {
        Traitor.focused = parent;
        focused = false;
    }
    
    private char[] getChars (int key) {
        
        switch(key) {
            case Traitor.KEY_NUM0 : 
                if(DisplayMode == SINGLELINE_MODE) {
                    return new char[]{' ','0'};
                }
                return NUM0_CHARS;
            case Traitor.KEY_NUM1 : return NUM1_CHARS;
            case Traitor.KEY_NUM2 : return NUM2_CHARS;
            case Traitor.KEY_NUM3 : return NUM3_CHARS;
            case Traitor.KEY_NUM4 : return NUM4_CHARS; 
            case Traitor.KEY_NUM5 : return NUM5_CHARS; 
            case Traitor.KEY_NUM6 : return NUM6_CHARS; 
            case Traitor.KEY_NUM7 : return NUM7_CHARS; 
            case Traitor.KEY_NUM8 : return NUM8_CHARS; 
            case Traitor.KEY_NUM9 : return NUM9_CHARS; 
                
        }
        
        return null;
    }
    
    private char[] getNums (int key) {
        
        switch(key) {
            case Traitor.KEY_NUM0 : return new char[]{'0'};
            case Traitor.KEY_NUM1 : return new char[]{'1'};
            case Traitor.KEY_NUM2 : return new char[]{'2'};
            case Traitor.KEY_NUM3 : return new char[]{'3'};
            case Traitor.KEY_NUM4 : return new char[]{'4'}; 
            case Traitor.KEY_NUM5 : return new char[]{'5'}; 
            case Traitor.KEY_NUM6 : return new char[]{'6'}; 
            case Traitor.KEY_NUM7 : return new char[]{'7'}; 
            case Traitor.KEY_NUM8 : return new char[]{'8'}; 
            case Traitor.KEY_NUM9 : return new char[]{'9'}; 
                
        }
        
        return null;
    }
    
    private char toUpper (char c) {
        
        int ascii = (int)c;
        if(ascii >= 97 && ascii <= 123) {
            ascii -= 32;
        }
        
        return (char)ascii;
    }
    /**
     * Listens and manages keys when a text area is focused
     * @param key key from keypressed() event
     */
    public void listenKey (int key) {
        
        if(Type == TEXTAREA_INACTIVE) {
            return;
        }
        
        makeInsertReady();
        
        updateCaretPosition(key);
        LastPressedKey = key;
        char c = (char)01;
        
        char [] KeySet;
        if(WritingMode == SMALL_CHAR_MODE || WritingMode == CAPITAL_CHAR_MODE) {
            KeySet = getChars(key);
        }
        else {
            KeySet = getNums(key);
        }
        
        if(KeySet != null) {
            if(WritingMode == CAPITAL_CHAR_MODE) {
                c = toUpper(KeySet[CharSelector]);
            }
            else {
                c = KeySet[CharSelector];
            }
        }
        
        if(c != (char)01) {
            
            if(CaretUpdated) {
                Content.insert(caretIndex, c);
                breakIntoLines(Content.toString(), Theme.WIDTH-10);
                updateCaretXY();
                CaretUpdated = false;
            }
            else {
                
                char ci = Content.charAt(caretIndex);
                
                Content.setCharAt(caretIndex, c);
                
                if(c != '\n' && ci != '\n') {
                    alphabetChange(c);
                }
                else {
                    breakIntoLines(Content.toString(), Theme.WIDTH-10);
                    updateCaretXY();
                }
            }
        }
        
        if(KeySet != null) {
            if(CharSelector < KeySet.length-1) {
                CharSelector ++;
            }
            else {
                CharSelector = 0;
            }
        }
        
        LastCaretUpdated = System.currentTimeMillis();
        
        if(key == Traitor.KEY_STAR) {
            showSymbols();
        }
        else if(key == Traitor.KEY_POUND) {
            WritingMode = (WritingMode+1)%3;
        }
        
        setInserted();
    }
    
    private void makeInsertReady () {
        if(CaretBlinking) {
            CaretBlinking = false;
        }
    }
    
    private void setInserted () {
        if(!CaretBlinking) {
            CaretBlinking = true;
        }
    }
    
    private void updateCaretPosition (int key) {
        
        char[] ks = getChars(key);
        if(ks == null) {
            return;
        }
        
        if(WritingMode == NUM_MODE) {
            caretIndex ++;
            CaretUpdated = true;
            CharSelector = 0;
            return;
        }
        
        if(LastPressedKey == key) {
            if(LastCaretUpdated + keyDelay <= System.currentTimeMillis()) {
                caretIndex ++;
                CaretUpdated = true;
                CharSelector = 0;
            }
        }
        else {
            caretIndex ++;
            CharSelector = 0;
            CaretUpdated = true;
        }
        
    }
    
    private void updateCaretXY () {
        
        int i = 0, len = 0, plen = 0, line = 0;
        
        if (lines != null) {
            for( ;i < lines.length ; i++) {
                
                if(lines[i] == null) {
                    len += 1;
                    plen ++;
                }
                else {
                    len += lines[i].length();
                }
                if(caretIndex < len) {
                    caretY = line;
                    break;
                }
                
                plen = len;
                
                if(lines[i] != null) {
                    line++;
                }
            }
            
            caretX = (caretIndex+1) - plen;
        }
                
        
    }
    
    public int getCaretX () {
        return caretX;
    }
    
    public int getCaretY () {
        return caretY;
    }
    private void showSymbols () {
        Traitor.focused = sym;
    }
    /**
     * Shows the system text box.
     * @param mid midlet that gives canvas
     */
    public void showT9Box (Midlet mid) {
        mid.setTextBox(this);
    }
    /**
     * Self Focus
     */
    public void getFocused () {
        
        String s = null;
        char code = '?';
        if(sym.selectedGroup == Symbols.ORD_SYMBOLS) {
            code = sym.CharSymbols[sym.selectedItem];
        }
        else if(sym.selectedGroup == Symbols.SMILIES) {
            s = sym.smilies[sym.selectedItem];
        }
        else {
            code = sym.Unicode[sym.selectedItem];
        }
        
        if(CaretBlinking) {
            
            CaretBlinking = false;
        }
        
        if(s == null) {
            Content.insert(caretIndex+1, code);
            breakIntoLines(Content.toString(), Theme.WIDTH - 10);
            caretIndex ++;
            updateCaretXY();
        }
        else {
            Content.insert(caretIndex+1, s);
            breakIntoLines(Content.toString(), Theme.WIDTH-10);
            caretIndex += s.length();
            updateCaretXY();
        }
        
        if(!CaretBlinking) {
            
            CaretBlinking = true;
        }
        
        Traitor.focused = this;
    }
    /**
     * <font color="red"> Clear One Character </font>
     */
    public void clear () {
        
        if(Type == TEXTAREA_INACTIVE) {
            return;
        }
        
        Content.deleteCharAt(caretIndex);
        breakIntoLines(Content.toString(), Theme.WIDTH-10);
        caretIndex --;
        
        updateCaretXY();
        
    }
    /**
     * This is to decrease the delay while typing.
     * @param a new changed alphabet
     */
    private void alphabetChange (char a) {
        
        int ind = getStringIndexOf(lines, caretY);
        char[] ca = lines[ind].toCharArray();
        ca[caretX-1] = a;
        lines[ind] = new String(ca);
        
    }
    /**
     * To get the perfect String of index y
     * @param ar String array contains null strings
     * @param y No of perfect string required
     * @return index of y th perfect string
     */
    private int getStringIndexOf (String[] ar, int y) {
        
        int j = 0;
        for (int i = 0; i < ar.length; i ++) {
            if(ar[i] != null) {
                if(y == j) {
                    return i;
                }
                j ++;
            }
        }
        
        return 0;
        
    }
    /**
     * Optimized function for Text Wrapping
     * @see function in label class.
     */
    private void breakIntoLines (String string, int width) {
		
	if(DisplayMode == SINGLELINE_MODE) {
            lines = new String[1];
            lines[0] = string;
            return;
        }
        
        if ( string != null ) {
               
            Vector parsedLines = new Vector();
            String newLine;
			
			// The index of the character that starts this line.
            int lineStart = 0;
			
			// The index of a character that may appear on the
			// next line.  For example a space can appear on this
			// line or the next, but a letter in the middle of the
			// word must stay on the same line as the entire word.
            int lastBreakableSpot = 0;
			
			// The index of the last character that wasn't a
			// whitespace character such as ' '.
            int lastNonWhiteSpace = 0;
			
			// The width of a wide character used as a safety
			// margin in calculating the line width.  Not all font
			// width calculations are reliable (e.g. the default
			// font on the Motorola SLVR).  Adjusting the line
			// width by this amount assures no text will get clipped.
            Font f = Theme.getInstance().getFont();
            int charWidth = f.charWidth( 'O' );
            width -= charWidth;
			
			// Scan lengths character-by-character.
            char[] chars = string.toCharArray();
			
            for ( int i = 0; i < chars.length; i++ ) {
                boolean isSeparator =
                        (chars[i] == '-') ||(chars[i] == '/');
                boolean isWhiteSpace = (chars[i] == ' ');
				
                boolean isNewLine = (chars[i] == '\n');
                boolean isCarrageReturn = (chars[i] == '\r');
				
                boolean isLineBreak = isNewLine || isCarrageReturn;
                int lineWidth = f.charsWidth( chars, lineStart, i - lineStart );

                if ( (isLineBreak) || (lineWidth > width) ) {
                    int lineEnd;
					
                    if ( isLineBreak ) {
                        lineEnd = i;
                    }
                    else if ( lastBreakableSpot > lineStart ) {
                        lineEnd = lastBreakableSpot;
                    }
                    else {
						// This word is longer than the line so do a mid-word break
						// with the current letter on the next line.
                        lineEnd = i - 1;
                    }
					
					// Record the line.
                    newLine = string.substring( lineStart, lineEnd )/*.trim()*/;
                    parsedLines.addElement( newLine );
					
					// Setup for the next line.
                    if ( isLineBreak ) {
                        lineStart = lineEnd + 1;  // +1 to advance over the '\n'
						
						// Add an empty line between the paragraphs.
                        if ( isNewLine ) {
                            parsedLines.addElement( null );
                        }
                    }
                    else  {
                            lineStart = lineEnd;
                    }
                }

				// Is this a good place for a line-break?
                if ( isSeparator ) {
					// Put the separator char on this line (e.g. "full-").
                    lastBreakableSpot = i + 1;
                }

                if ( isWhiteSpace ) {
					// Break at the whitespace.  It will be trimmed later.
                    lastBreakableSpot = /*lastNonWhiteSpace + 1*/i + 1;
                }
                else {
					// Record this character as the last non-whitespace processed.
                    lastNonWhiteSpace = i;
                }
            }
			
			// Add the last line.
            newLine = string.substring( lineStart )/*.trim()*/;
            parsedLines.addElement( newLine );
			
			// Convert the vector into a string array.
            lines = new String[parsedLines.size()];
            Enumeration e = parsedLines.elements();
            int i = 0;
			
            while ( e.hasMoreElements() ) {
                lines[i++] = (String)e.nextElement();
            }
        }
    }
    
    
}

/**
 * Symbols Class
 * Providing facility to add unicode symbols, that not provided by any other app.
 * @author Bineesh
 */
class Symbols implements FocusableObject {
    
    public static final int ORD_SYMBOLS = 0;
    public static final int SMILIES = 1;
    
    private Menu ok = new Menu("Use", Menu.OK);
    private Menu cancel = new Menu("Cancel", Menu.CANCEL);
    public TextArea parent;
    private int CCount = 0;
    public int selectedItem = 0, selectedGroup = ORD_SYMBOLS;
    
    public char[] CharSymbols = { '.', ',', '\'', '?','!','"','-','(',')','@','/',':','_',';','+','&','%','*','=','<','>','[',']','{','}','\\','~','`','^','|','#'};
    public String[] smilies = {":-)", ":-D",";-)", ":-(", ":-P", ";-(", ":-|", ":-/", ":O", ":-*", ":-x", ":->", "8-)", "%-)", ":-@", ";>"};
    public char[] Unicode = {'','','','','','','','','','','','','','','','','','','','','♥','♦','♠','☺'};

    public Menu getLSMenu() {
        return null;
    }

    public Menu getRSMenu() {
        return cancel;
    }

    public Menu getFireMenu() {
        return ok;
    }

    public void next() {
        if(selectedGroup == ORD_SYMBOLS) {
            if(selectedItem + CCount < CharSymbols.length) {
                selectedItem += CCount;
            }
        }
        else if(selectedGroup == SMILIES) {
            if(selectedItem + CCount < 16) {
                selectedItem += CCount;
            }
        }
        else {
            if(selectedItem+CCount < Unicode.length) {
                selectedItem += CCount;
            }
        }
    }

    public void previous() {
        if(selectedItem - CCount >= 0) {
            selectedItem -= CCount;
        }
    }
    
    public void left() {
        if(selectedItem > 0) {
            selectedItem--;
        }
    }
    
    public void right() {
        if(selectedGroup == ORD_SYMBOLS) {
            if(selectedItem < CharSymbols.length -1) {
                selectedItem++;
            }
        }
        else if(selectedGroup == SMILIES) {
            if(selectedItem < smilies.length -1) {
                selectedItem++;
            }
        }
        else {
            if(selectedItem < Unicode.length -1) {
                selectedItem++;
            }
        }
    }

    public void paint(Graphics g) {
        parent.paint(g);
        
        Theme t = new Theme();
        t.setGraphics(g);
        if(selectedGroup == ORD_SYMBOLS) {
            CCount = t.drawSymbols(CharSymbols, selectedItem);
        }
        else if(selectedGroup == SMILIES) {
            CCount = t.drawSmilies(smilies, selectedItem);
        }
        else {
            CCount = t.drawSymbols(Unicode, selectedItem);
        }
        t.drawMenu(null, ok, cancel);
    } 
    /**
     * Listens to keys 
     * @param key 
     */
    public void listenKey(int key) {
        if(key == Traitor.KEY_STAR) {
            selectedGroup = (selectedGroup+1)%3;
            selectedItem = 0;
        }
    }

    public long run() {
        
        return 500L;
    }
    
}