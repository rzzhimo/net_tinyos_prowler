/*
 * Copyright (c) 2003, Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 * Author: Gyorgy Balogh, Gabor Pap, Miklos Maroti
 * Date last modified: 02/09/04
 */
 
package net.tinyos.prowler;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This is a sample application, it shows a way of utilizing the Prowler 
 * simulator. This is a broadcast application, where a mote in the middle of the 
 * field broadcasts a message which is further broadcasted by all the recepients.
 * Please note that this is not the only way of writing applications, this is 
 * just an example.
 * 
 * @author Gabor Pap, Gyorgy Balogh, Miklos Maroti
 */
public class TestBroadcastNode extends Mica2Node{

	/** This field is true if this mote rebroadcasted the message already. */
	boolean sent = false;

	/** This field stores the mote from which the message was first received. */
	private Node parent = null; 

	/**
	 * This extension of the {@link Application} baseclass does everything we 
	 * expect from the broadcast application, simply forwards the message once,
	 * and that is it. 
	 */
	public class BroadcastApplication extends Application{
		
		/**
		 * @param node the Node on which this application runs.
		 */
		public BroadcastApplication(Node node) {
			super(node);
		}
    
		/**
		 * Stores the sender from which it first receives the message, and passes 
		 * the message.
		 */
		public void receiveMessage(Object message ){
			if (parent == null){
				parent = parentNode;
				sendMessage( message );
			}            
		}    

		/**
		 * Sets the sent flag to true. 
		 */
		public void sendMessageDone(){
			sent = true;                        
		}
	}

	public Node getParent()
	{
		return this.parent;
	}

	public TestBroadcastNode(Simulator sim, RadioModel radioModel) {
		super(sim, radioModel);
	}
    
	/**
	 * Draws a filled circle, which is: <br>
	 *  - blue if the node is sending a message <br>
	 *  - red if the node received a corrupted message <br>
	 *  - green if the node received a message without problems <br>
	 *  - pink if the node sent a message <br>
	 *  - and black as a default<br>
	 * It also draws a line between mote and its parent, which is another mote
	 * who sent the message first to this.
	 */ 
//	public void display(Display disp){
//		Graphics g = disp.getGraphics();
//		int      x = disp.x2ScreenX(this.x);
//		int      y = disp.y2ScreenY(this.y);
//               
//		if( sending ){                    
//			g.setColor( Color.blue );
//		}else if( receiving ){
//			if( corrupted )
//				g.setColor( Color.red );
//			else
//				g.setColor( Color.green );                            
//		}else{
//			if( sent )
//				g.setColor( Color.pink );
//			else
//				g.setColor( Color.black );
//		}
//		g.fillOval( x-3, y-3, 5, 5 );
//		if( parent != null ){
//			g.setColor( Color.black );
//			int x1 = disp.x2ScreenX(parent.getX());
//			int y1 = disp.y2ScreenY(parent.getY());
//			g.drawLine(x,y,x1,y1);
//		}
//	}   
	public void display(DisplayQueue displayQueue){
		
	}
}

