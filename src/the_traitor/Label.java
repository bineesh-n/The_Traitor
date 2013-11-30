/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
/**
 *
 * @author Bineesh
 */
public class Label extends Component {

    private int height = 0, align = ALIGN_LEFT;
    /** allows to change font **/
    public Font font;
    private String LabelString;
    /** lines after wrapping **/
    public String[] lines;
    /** cursor is in this line number **/
    public float lineNo;
    /** x coordinate, default color **/
    public int x, color = 0x04819e;
    private boolean breakedLines = false;
    /** two alignments **/
    public static final int ALIGN_CENTRE = 0;
    public static final int ALIGN_LEFT = 1;
    
    /**
     * 
     * @param str string label
     * @param LineNo space difference between two components
     */
    public Label(String str, float LineNo) {
        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        this.LabelString = str;
        this.lineNo = LineNo;
    }
    /**
     * 
     * @param font font of label
     * @param str
     * @param LineNo 
     */
    public Label(Font font, String str, float LineNo) {
        this.font = font;
        this.LabelString = str;
        this.lineNo = LineNo;
    }
    
    public int getHeight() {
         return height;
    }

    public void paint(Graphics g) {
        
        if(!breakedLines) {
            lines = breakIntoLines(LabelString, Theme.WIDTH - 5);
            breakedLines = true;
        }
        
        Theme t = Theme.getInstance();
        t.setGraphics(g);
        height = t.drawLabel(this);
        
    }
    /**
     * Sets the x axis.
     * ignores x axis on center alignment
     * @param x x axis
     */
    public void setXAxis(int x) {
        this.x = x;
    }
    /**
     * Sets Alignment.
     * @param align alignment as in constants
     */
    public void setAlign(int align) {
        this.align = align;
    }
    /**
     * 
     * @return alignment of label
     */
    public int getAlign() {
        return this.align;
    }
    /**
     * 
     * @param color new color
     */
    public void setColor(int color) {
        this.color = color;
    }
    /**
     * 
     * @param newLabel new String is to be set
     */
    public void editLabel(String newLabel) {
        this.LabelString = newLabel;
        breakedLines = false;
    }
    
    public boolean isFocusable() {
        return false;
    }
    /**
     * Text-wrapper
     * @param string string input
     * @param width width allowed
     * @return String array divided into lines
     */
    public static String[] breakIntoLines (String string, int width) {
		
                String[] lines = null;
                        
		if ( string != null )
		{
			
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
			
                        Font f = Theme.getInstance().getFont();
			int charWidth = f.charWidth( 'O' );
			width -= charWidth;
			
			// Scan lengths character-by-character.
			char[] chars = string.toCharArray();
			
			for ( int i = 0; i < chars.length; i++ )
			{
				boolean isSeparator =
					(chars[i] == '-') ||
					(chars[i] == '/');
				boolean isWhiteSpace =
					(chars[i] == ' ');
				
				boolean isNewLine =
					(chars[i] == '\n');
				boolean isCarrageReturn =
					(chars[i] == '\r');
				
				boolean isLineBreak = isNewLine || isCarrageReturn;
				int lineWidth = f.charsWidth( chars, lineStart, i - lineStart );

				
				if ( (isLineBreak) || (lineWidth > width) )
				{
					int lineEnd;
					
					if ( isLineBreak )
					{
						lineEnd = i;
					}
					else if ( lastBreakableSpot > lineStart )
					{
						lineEnd = lastBreakableSpot;
					}
					else
					{
						// This word is longer than the line so do a mid-word break
						// with the current letter on the next line.
						lineEnd = i - 1;
					}
					
					// Record the line.
					newLine = string.substring( lineStart, lineEnd ).trim();
					parsedLines.addElement( newLine );
					
					// Setup for the next line.
					if ( isLineBreak )
					{
						lineStart = lineEnd + 1;  // +1 to advance over the '\n'
						
						// Add an empty line between the paragraphs.
						//if ( isNewLine )
						//{
						//	parsedLines.addElement( " " );
						//}
					}
					else  // line break in the middle of a paragraph
					{
						lineStart = lineEnd;
					}
				}

				// Is this a good place for a line-break?
				if ( isSeparator )
				{
					// Put the separator char on this line (e.g. "full-").
					lastBreakableSpot = i + 1;
				}

				if ( isWhiteSpace )
				{
					// Break at the whitespace.  It will be trimmed later.
					lastBreakableSpot = /*lastNonWhiteSpace + 1*/i + 1;
				}
				else
				{
					// Record this character as the last non-whitespace processed.
					lastNonWhiteSpace = i;
				}
			}
			
			// Add the last line.
			newLine = string.substring( lineStart ).trim();
			parsedLines.addElement( newLine );
			
			// Convert the vector into a string array.
			lines = new String[parsedLines.size()];
			Enumeration e = parsedLines.elements();
			int i = 0;
			
			while ( e.hasMoreElements() )
			{
				lines[i++] = (String)e.nextElement();
			}
		}
                
                return lines;

	}
    
}
