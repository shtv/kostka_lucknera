package cuboid.base.visualisation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.FPSAnimator;

import cuboid.base.Block;
import cuboid.base.Cube;
import cuboid.base.Move;
import cuboid.base.Solution;

public class VisualisationWindow extends JFrame implements GLEventListener, KeyListener{
		
		private final static double[][] colors = {{1,0,0},{0,1,0},{0,0,1},{0.5,0.8,0.2},
												{0.8,0.5,0.2},{0.5,0.2,0.8},{0.8,0.2,0.5},
												{0.2,0.5,0.8},{0.2,0.8,0.5}};
		private Solution solution;
		private GLCanvas canvas;
		private GLU glu;
		private double angleA=0;
		private double angleB=0;
		private double distance=20;
		private int lastElem;
		
		public VisualisationWindow(Solution solution)
		{
		
			this.solution=solution;
			lastElem=solution.getSequence().size()-1;
			
			setBounds(100,100,800,800);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			canvas=new GLCanvas(new GLCapabilities());
			canvas.addGLEventListener(this);
			canvas.addKeyListener(this);
			add(canvas);
			
			addKeyListener(this);
		}

		private void drawCube(GL gl,int colorIdx, double centerX,double centerY,double centerZ)
		{
			gl.glBegin(GL.GL_QUADS);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY-0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY-0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX-0.5, centerY+0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY+0.5, centerZ-0.5);
				
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY-0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY-0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY+0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX-0.5, centerY+0.5, centerZ+0.5);
				
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY+0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY+0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY-0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX+0.5, centerY-0.5, centerZ-0.5);
				
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY+0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY+0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY+0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY+0.5, centerZ-0.5);
				
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY-0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY+0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY+0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX+0.5, centerY-0.5, centerZ-0.5);
				
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX+0.5, centerY-0.5, centerZ-0.5);
				gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
				gl.glVertex3d(centerX+0.5, centerY-0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY-0.5, centerZ+0.5);
				gl.glColor3d(colors[colorIdx][0]/2,colors[colorIdx][1]/2,colors[colorIdx][2]/2);
				gl.glVertex3d(centerX-0.5, centerY-0.5, centerZ-0.5);
			gl.glEnd();
		}
		
		private void drawMove(GL gl,Move m,int colorIdx)
		{
			gl.glColor3d(colors[colorIdx][0],colors[colorIdx][1],colors[colorIdx][2]);
			Block orientedBlock=new Block(m.getB());
			orientedBlock.orient(m.getOrientation());
			for(Cube c : orientedBlock.getCubes())
			{
				double centerX=m.getFit().getX()+c.getX();
				double centerY=m.getFit().getY()+c.getY();
				double centerZ=m.getFit().getZ()+c.getZ();
				drawCube(gl,colorIdx,centerX,centerY,centerZ);
			}
		}
		
		@Override
		public void display(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			int colorCounter=0;
			GL gl=arg0.getGL();

			gl.glLoadIdentity();
			float camX=(float)(distance*Math.cos(angleB)*Math.sin(angleA));
			float camY=(float)(distance*Math.sin(angleB));
			float camZ=(float)(distance*Math.cos(angleB)*Math.cos(angleA));
			glu.gluLookAt(camX,camY,camZ, 0, 0, 0, 0, 1,0);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			
			for(int i=0;i<=lastElem;i++)
				drawMove(gl, solution.getSequence().get(i),(i%colors.length));
			
			gl.glFlush();
		}

		@Override
		public void displayChanged(GLAutoDrawable arg0, boolean arg1,
				boolean arg2) {
		}

		@Override
		public void init(GLAutoDrawable arg0) {
			GL gl=arg0.getGL();
			glu=new GLU();
			gl.glClearColor(0, 0, 0, 0);
			gl.glEnable(GL.GL_DEPTH_TEST);
		}

		@Override
		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
				int arg4) {	
			GL gl=arg0.getGL();
			gl.glViewport(0, 0,getWidth(), getHeight());
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(60, (float)getWidth()/(float)getHeight(), 1, 1000);
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glLoadIdentity();
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			int keycode=arg0.getKeyCode();
			switch(keycode)
			{
			case KeyEvent.VK_0:				
				distance+=5;
				canvas.display();
				break;
			case KeyEvent.VK_9:
				distance-=5;
				canvas.display();
				break;
			case KeyEvent.VK_1:
				lastElem--;
				lastElem=(lastElem<0)?0:lastElem;
				canvas.display();
				break;
			case KeyEvent.VK_2:
				lastElem++;
				lastElem=(lastElem>=solution.getSequence().size())?solution.getSequence().size()-1:lastElem;
				canvas.display();
				break;
			case KeyEvent.VK_W:
				angleB+=0.25;
				canvas.display();
				break;
			case KeyEvent.VK_S:
				angleB-=0.25;
				canvas.display();
				break;
			case KeyEvent.VK_A:
				angleA-=0.25;
				canvas.display();
				break;
			case KeyEvent.VK_D:
				angleA+=0.25;
				canvas.display();
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}
}
