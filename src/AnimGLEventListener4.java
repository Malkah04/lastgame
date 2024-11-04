import Texture.TextureReader;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener4 extends AnimListener {

    int animationIndex = 0;
    int monsterIndex = 4;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth/2 , y = 0;
    int x1 = maxWidth/3, y1 = maxHeight-10;
    boolean isFlag=false,toColl=false ,isarrow=true;


    String textureNames[] = {"Man1.png","Man2.png","Man3.png","Man4.png","c.png","a.png","b.png" ,"d.png" ,"e.png","f.png","g.png","h.png","i.png","j.png","k.png","l.png","m.png","n.png","o.png","p.png","q.png","r.png","s.png","t.png","u.png","v.png","w.png","x.png","y.png","z.png" ,"HealthB.png","Health.png","ninja star.png","Back.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

     int indexOfWhite=textureNames.length-4;
     int indexOfRed=textureNames.length-3;
     int posOfwhitex=0,posOfred=0;
     int posOfwhitey=maxHeight-10,posOfredy=maxHeight-10;
    int indexOfNinja=textureNames.length-2;
    int posXN=x;
    int posYN=y;
    /*
     5 means gun in array pos
     x and y coordinate for gun
     */
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch( IOException e ) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
    double a=0;
    boolean isOver=false;

    public void display(GLAutoDrawable gld) {
        if(isOver){
            y1=-4;
            return;
        }
        boolean missed=true;
        y1--;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        DrawBackground(gl);
        handleKeyPress();
        animationIndex = animationIndex % 4;
        //drow the man
        DrawSprite(gl, x, y, animationIndex, 1);
        //draw the litters
            DrawSprite(gl, x1, y1, monsterIndex, 1);

        if(toColl&&isarrow) {
            DrawSprite(gl, posXN, posYN, indexOfNinja, 1);
            reachTheTarget();
            gl.glPushMatrix();
            gl.glTranslated(posXN, posYN, 0);
            gl.glRotated(a++,0,0,1); //why not rotated
            DrawSprite(gl, posXN, posYN, indexOfNinja, 1);
            gl.glPopMatrix();
        }
        if (Math.abs(posXN - x1) <= 1 && Math.abs(posYN - y1) <= 1) {
            reset();
            y1=-4;
            missed=false;
        }
        if(missed&&y1<=-1){
            posOfred-=5;
        }
        if(posOfred<-25){
            isOver=true;
        }
        Drawrec(gl,posOfwhitex,posOfwhitey,  indexOfWhite ,1 );
        Drawrec(gl,posOfred,posOfredy,indexOfRed,1);

        double dist = sqrdDistance(x,y,x1,y1);
        double radii = Math.pow(0.5*0.1*maxHeight+0.5*0.1*maxHeight,2);
        boolean isCollided = dist<=50;
//        System.out.println(isCollided + ", "+ dist + ", "+ radii);
        if(y1<0){
            x1=(int)(Math.random()*maxWidth);
            y1=maxHeight;
            monsterIndex = (int)(Math.random()*26)+4;
        }

    }

   public void reset(){
        posXN=x;
        posYN=y;
        toColl=false;
        isarrow=false;
        isFlag=false;
   }

    public double sqrdDistance(int x, int y, int x1, int y1){
        return Math.pow(x-x1,2)+Math.pow(y-y1,2);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    public void Drawrec(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(4.0f, 0.0f);
        gl.glVertex3f(4.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(4.0f, 1.0f);
        gl.glVertex3f(4.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 0.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length-1]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
     * KeyListener
     */
    boolean flag=false;
    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 0) {
                x--;
            }
            flag=true;
//            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth-10) {
                x++;
            }
            flag=true;

//            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (y > maxHeight/2) {
                y--;
            }
            flag=true;

//            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (y < maxHeight-10) {
                y++;
            }
            flag=true;
//            animationIndex++;
        }
        if(flag){
            animationIndex++;
            flag=false;
        }
    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care
        char keyChar = event.getKeyChar();
        for (int i = 4; i <textureNames.length-2 ; i++) {
            if(keyChar==textureNames[i].charAt(0)&&textureNames[i].equals(textureNames[monsterIndex])){
                isFlag = true;
                toColl = true;
                isarrow = true;
                break;
            }

        }
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
    public void reachTheTarget() {
        if (posXN < x1) {
            posXN++;
        }
        else if (posXN > x1) {
            posXN--;
        }
        if (posYN < y1) {
            posYN++;
        }
        else if (posYN > y1) {
            posYN--;
        }
    }

}
