/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Bineesh
 */
public class Theme {
    
    private static final int FONT_COLOR = 0xffffff;
    private static final int MENU_FONT = 0xffffff;
    
    private static final int BG_COLOR1 = 0xeeeeee;
    private static final int BG_COLOR2 = 0xFFFFFF;
    
    private static final int HF_COLOR1 = 0x0099ff;
    private static final int HF_COLOR2 = 0x0066ff;
    
    private static final int VERTICAL = 0;
    
    private static final int MENU_COLOR1 = 0xeeeeee;
    private static final int MENU_COLOR2 = 0xefefef;
    
    private static final int MENU_BORDER = 0x50021B;
    private static final int ITEM_COLOR = 0xcccccc;
    
    private static final int TEXT_AREA_BG = 0xffffff;
    private static final int SYMBOLS_BG = 0x776666cc;
    
    private static final int ANCHOR = Graphics.TOP|Graphics.LEFT ;
    public static int HEIGHT, WIDTH;
    private int fheight;
    private Graphics g;
    private Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private static Image[] smilies; 
    
    public Theme() {
        this.fheight = f.getHeight()+6;
    }
    
    public void setGraphics(Graphics g) {
        this.g = g;
        g.setFont(f);
    }
    
    public static Theme getInstance() {
        
        Theme t = new Theme();
        return t;
    }
    
    public void paintBody() {
        gradientBox(BG_COLOR1, BG_COLOR2, 0, 0, WIDTH, HEIGHT, VERTICAL);
    }
    
    public void paintBody(String ImgUrl) {
        
        gradientBox(BG_COLOR1, BG_COLOR2, 0, 0, WIDTH, HEIGHT, VERTICAL);
        
        if (WIDTH != 128) {
            
        }
        
        try {
            Image im = Image.createImage(ImgUrl);  
            g.drawImage(im, 0, 0, ANCHOR);
        }
        catch (Exception e) {
            
        }
    }
    
    public void showProcess(String s) {
        
        if(s == null) {
            return;
        }
        gradientBox(HF_COLOR2, 0x003399, 0, 0, WIDTH, fheight, VERTICAL);
        g.setColor(MENU_FONT);
        g.drawString(s, WIDTH/2, 3, Graphics.TOP|Graphics.HCENTER);
        
    }
    
    public void drawMenu(Menu left_soft, Menu fire, Menu right_soft) {
        
        if(left_soft == null&& right_soft == null&& fire == null) {
            return;
        }
        
        gradientBox(HF_COLOR2, HF_COLOR1, 0, HEIGHT-fheight, WIDTH, fheight/2, VERTICAL);
        gradientBox(HF_COLOR1, HF_COLOR2, 0, HEIGHT-(fheight/2), WIDTH, fheight/2, VERTICAL);
        
        g.setColor(MENU_FONT);
        
        if(left_soft != null) {
            g.drawString(left_soft.name(), 5, HEIGHT-(fheight-3), ANCHOR);
        }
        if(right_soft != null) {
            g.drawString(right_soft.name(), WIDTH-(f.stringWidth(right_soft.name())+5), HEIGHT-(fheight-3), ANCHOR);
        }
        if(fire != null) {
            g.drawString(fire.name(), WIDTH/2, HEIGHT-(fheight-3), Graphics.TOP|Graphics.HCENTER);
        }
    }
    
    public void drawOptions(String [] options, int l, int Selected){
        
        int startx = 0, starty = HEIGHT-(l*fheight)-(fheight), width = WIDTH/2, height = l*fheight;
        gradientBox(MENU_COLOR1, MENU_COLOR2, startx, starty, width, height, VERTICAL);
        g.setColor(MENU_BORDER);
        g.drawRect(startx, starty, width, height);
        for(int i = 0; i < l; i++) {
            if( i == Selected) {
                g.setColor(ITEM_COLOR);
                g.fillRect(startx, starty+(i*fheight)+1, width, fheight);
            }
            g.setColor(MENU_BORDER);
            g.drawString(options[i], startx+2, starty+(i*fheight)+3, ANCHOR);
        }
        
    }
    
    public void drawTextArea(TextArea txt) {
        
        int y = txt.y;
        int startx = 4, width = WIDTH-8, starty = y, height = txt.height;
        g.setColor(0x000000);
        g.drawString(txt.Title, startx, starty+3, ANCHOR);
        starty += fheight;
        g.setColor(TEXT_AREA_BG);
        
        if(txt.lines != null) {
            int ht = getArrayLength(txt.lines) * fheight;
            if(ht > height) {
                height = txt.height = ht;
            }
        }
        
        g.fillRect(startx+1, starty+1, width-1, height-1);
        g.setColor(0xbbbbbb);
        g.drawRect(startx, starty, width, height);
        
        if(txt.Type == TextArea.TEXTAREA_ACTIVE) {
            g.setColor(0x000000);
        }
        else {
            g.setColor(0xbbbbbb);
        }
        
        drawLabel(txt.lines, startx+2, starty+3);
    }
    
    private int getArrayLength(String []ar) {
        
        int j = 0;
        for(int i = 0; i < ar.length; i++) {
            if(ar[i] == null) {
                j ++;
            }
        }
        
        return ar.length - j;
    }
    
    private String getStringOf(String[] ar, int y) {
        
        int j = 0;
        for (int i = 0; i < ar.length; i ++) {
            if(ar[i] != null) {
                if(y == j) {
                    return ar[i];
                }
                j ++;
            }
        }
        
        return null;
        
    }
    
    public void drawActiveTextArea(TextArea txt) {
        
        int y = txt.y;
        int startx = 4, width = WIDTH-8, starty = y, height = txt.height;
        g.setColor(0x000000);
        g.drawString(txt.Title, startx, starty+3, ANCHOR);
        starty += fheight;
        
        if(txt.lines != null) {
            int ht = getArrayLength(txt.lines) * fheight;
            if(ht > height) {
                height = txt.height = ht;
            }
        }
        
        g.setColor(TEXT_AREA_BG);
        g.fillRect(startx+1, starty+1, width-1, height-1);
        g.setColor(MENU_BORDER);
        g.drawRect(startx, starty, width, height);
        g.setColor(0x000000);
        
        int bwd, color = 0xbb000000, l = txt.Content.length();
        String st;
        if (txt.WritingMode == TextArea.CAPITAL_CHAR_MODE) {
            st = "ABC";
            bwd = f.stringWidth(st);
        }
        else if(txt.WritingMode == TextArea.SMALL_CHAR_MODE) {
            st = "abc";
            bwd = f.stringWidth(st);
        }
        else {
            st = "123";
            bwd = f.stringWidth(st);
        }
        
        int rb = f.stringWidth(l+"") + 1;
        bwd += rb + 3;
        int[] array = new int[bwd*fheight];
        for(int i = 0; i < array.length; i++) {
            array[i] = color;
        }
        
       Image im = Image.createRGBImage(array, bwd, fheight, true);
       g.drawImage(im, width - bwd + startx, y, ANCHOR);
       g.setColor(FONT_COLOR);
       g.drawString(st, width - bwd + startx + 1, y+3, ANCHOR);
       g.drawString(l+"", width - rb + startx, y + 3, ANCHOR);
        //g.drawString(txt.Content.toString(), startx+2, starty+2, ANCHOR);
       if(txt.Type == TextArea.TEXTAREA_ACTIVE) {
           g.setColor(0x000000);
       }
       else {
           g.setColor(0xbbbbbb);
       }
       drawLabel(txt.lines, startx+2, starty+3);
       
       drawShadow(startx, starty, width, 3, 0x000000);
       
       if(txt.showCurser) {
            
            starty += txt.getCaretY()*fheight;
            if(txt.lines != null) {
                String str = getStringOf(txt.lines, txt.getCaretY());
                if(str != null) {
                    try {
                    startx += f.substringWidth(str,0, txt.getCaretX());
                    }
                    catch(Exception e) {
                        System.out.println(txt.getCaretX());
                    }
                }
            }
            
            g.drawChar('|', startx, starty+2, ANCHOR);
        }
    }
    
    public void drawContactItem(ContactItem cit) {
        int startx = 0, starty = cit.y;

        
        if(cit.focused&&cit.isMarked()) {
            g.setColor(0xff490d);
            g.fillRect(startx, starty, WIDTH, fheight*2);
            g.setColor(0x009fff);
        }
        else if(cit.focused) {
            gradientBox(0x6600ff, 0x6600f7, startx, starty, WIDTH, fheight*2, VERTICAL);
            g.setColor(0xffffff);
        }
        else if(cit.isMarked()) {
            g.setColor(0x003fff);
            g.fillRect(startx, starty, WIDTH, fheight*2);
            g.setColor(0xffffff);
            g.drawLine(startx, starty+fheight*2-1, WIDTH, starty+fheight*2-1);
            g.setColor(0x000000);
            
        }
        else {
            g.setColor(0xeeeeee);
            g.fillRect(startx, starty, WIDTH, 2*fheight);
            g.setColor(0xcccccc);
            g.drawLine(startx, starty+fheight*2-1, WIDTH, starty+fheight*2-1);
            g.setColor(0x000000);
        }
        
        g.drawString(cit.name, startx+5, starty+fheight/2, ANCHOR);
    }
    
    public void drawScrollBar(int x, int y, int width, int height, int mx, int my, int mheight) {
        
        g.setColor(0xbbbbbb);
        g.fillRoundRect(x, y, width, height, 20, 5);
        
        g.setColor(0x999999);
        g.fillRoundRect(mx, my, width, mheight, 20, 5);
        
    }
    
    public int getFHeight() {
        return fheight;
    }
    
    private void gradientBox(int color1,int color2,int left,int top,int width,int height,int orientation) {
        
        int max=orientation==VERTICAL?height:width;
        int color1RGB[]=new int []{(color1>>16)&0xfff,(color1>>8)&0xff,color1&0xff};
        int color2RGB[]=new int []{(color2>>16)&0xfff,(color2>>8)&0xff,color2&0xff};
        int colorCalc[]=new int []{((color2RGB[0]-color1RGB[0])<<16)/max, ((color2RGB[1]-color1RGB[1])<<16)/max,((color2RGB[2]-color1RGB[2])<<16)/max};
        for(int i = max;i > -1;i--){
            g.setColor((color1RGB[0]+((i*colorCalc[0])>>16))<<16|(color1RGB[1]+((i*colorCalc[1])>>16))<<8|(color1RGB[2]+((i*colorCalc[2])>>16)));
            if(orientation==VERTICAL) {
                g.drawLine(left, top+i, left+width-1, top+i);
            }
            else {
                g.drawLine(left+i, top, left+i, top+height-1);
            }
                
        }
    }
    
    private int drawLabel(String[] str, int startx, int starty) {
        
        if(str == null) {
            return 0;
        }
        
        for(int i = 0; i < str.length; i++) {
            
            if(str[i] == null) {
                continue;
            }
            
            g.drawString(str[i], startx, starty, ANCHOR);
            starty += fheight;
        }
        return 0;
    }
    
    public int drawLabel( Label l) {
        
        if(l.lines == null) {
            return 0;
        }
        
        g.setFont(l.font);
        g.setColor(l.color);
        int fh = l.font.getHeight() + 2;
        int startx = l.x, starty = l.y + (int)(fheight*l.lineNo);
        
        for(int i = 0; i < l.lines.length; i++) {
            if(l.lines[i] == null) {
                continue;
            }
            
            if(l.getAlign() == Label.ALIGN_CENTRE) {
                int wid = f.stringWidth(l.lines[i]);
                int gap = WIDTH - wid;
                startx = gap/2;
            }
            
            g.drawString(l.lines[i], startx, starty, ANCHOR);
            starty += fh;
        }
        
        g.setFont(f);
        return (l.lines.length)*fh+(int)(fheight*l.lineNo);
    }
    
    public int drawSymbols (char[] Symbols, int selected) {
        
        int cw = f.stringWidth("O")+10, lc = 1;
        
        int d = (WIDTH%cw)/2, cl = WIDTH/cw, x=0;
        
        int startx = d, height = (Symbols.length/cl+1)*fheight + 2, starty = HEIGHT-fheight-height;
        g.setColor(FONT_COLOR);
        int[] array = new int[WIDTH*height];
        for(int i = array.length; i-- != 0; ) {
            array[i] = SYMBOLS_BG;
        }
        
        Image tr_image = Image.createRGBImage(array, WIDTH, height, true);
        //g.fillRect(startx, starty, WIDTH, height);
        g.drawImage(tr_image, startx - d, starty, ANCHOR);
        g.setColor(0x000000);
        
        for(int i =0; i < Symbols.length; i++) {
            if(i == lc*cl) {
                starty += fheight;
                lc ++;
            }
            if(i == selected) {
                g.setColor(0x6600ff);
                g.drawRoundRect(startx+(x*cw) + 2, starty, cw, fheight, 10, 5);
                g.setColor(FONT_COLOR);
                g.fillRoundRect(startx+(x*cw)+3, starty+1, cw-1, fheight-1, 10, 5);
                g.setColor(0x000000);
            }
            g.drawChar(Symbols[i], startx+(x*cw)+5, starty+3, ANCHOR);
            x = (x+1)%cl;
        }
        
        return cl;
    }
    
    public int drawSmilies(String[] Smilies, int selected) {
        
        initSmilies();
        int cw = 18, lc = 1;
        
        int d = (WIDTH%cw)/2, cl = WIDTH/cw, x=0;
        
        int startx = d, height = (16/cl + 1)*16, starty = HEIGHT-fheight-height;
        g.setColor(FONT_COLOR);
        
        int[] array = new int[WIDTH*height];
        for(int i = array.length; i-- != 0; ) {
            array[i] = SYMBOLS_BG;
        }
        //System.out.println("cl:"+cl+"height:"+height);
        Image tr_image = Image.createRGBImage(array, WIDTH, height, true);
        g.drawImage(tr_image, 0, starty, ANCHOR);
        
        for(int i =0; i < 16; i++) {
            if(i == lc*cl) {
                starty += 16;
                lc ++;
            }
            if(i == selected) {
                g.setColor(MENU_COLOR1);
                g.fillRect(startx+(x*cw) + 5, starty, cw, 16);
                g.setColor(0x000000);
            }
            if(smilies[i] != null) {
                g.drawImage(smilies[i], startx+(x*cw)+5, starty, ANCHOR);
            }
            x = (x+1)%cl;
        }
        
        return cl;
    }
    
    public Font getFont() {
        return f;
    }
    
    private void drawShadow(int x, int y, int width, int height, int color) {
        int[] array = new int[width*height];
        
        int ratio = 0x00/height, transparency = 0x00;
        int i,w = width;
        
        for(i = 0; i < array.length; i ++) {
            int c;
            if(i < w) {
                c = (transparency<<24)|color;
                array[i] = c;
            }
            else{
                w += width;
                transparency -= ratio;
                c = (transparency<<24)|color;
                array[i] = c;
            }
        }
        
        Image img = Image.createRGBImage(array, width, height, true);
        g.drawImage(img, x, y, ANCHOR);
        
    }
    
    private void initSmilies() {
        
        if(smilies != null) {
            return;
        }
        
        smilies = new Image[16];
        
         try {
            smilies[0] = Image.createImage("/1.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[1] = Image.createImage("/2.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[2] = Image.createImage("/3.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[3] = Image.createImage("/4.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[4] = Image.createImage("/5.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[5] = Image.createImage("/6.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[6] = Image.createImage("/7.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[7] = Image.createImage("/8.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[8] = Image.createImage("/9.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[9] = Image.createImage("/10.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[10] = Image.createImage("/11.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[11] = Image.createImage("/12.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[12] = Image.createImage("/13.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[13] = Image.createImage("/14.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[14] = Image.createImage("/15.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            smilies[15] = Image.createImage("/16.png");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void drawPrompt(String Dialog) {
        
        g.setColor(SYMBOLS_BG);
        int startx = 0, starty = HEIGHT - 3*fheight - 5, height = 2 * fheight + 5;
        
        g.fillRect(startx, starty, WIDTH, height);
        g.setColor(FONT_COLOR);
        
        String [] lines = Label.breakIntoLines(Dialog, WIDTH - 5);
        
        drawLabel(lines, startx+5, starty + 5);
        //g.drawString(Dialog, startx+5, starty, ANCHOR);
        
    }
}
