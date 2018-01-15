package com.mmec.util;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class DrawFrame extends JFrame{

	 private String message = "";
	 
	public DrawFrame(String message)
	{
	   setTitle("DrawTest");
	   setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

	   // add panel to frame
	   DrawPanel panel = new DrawPanel(message);
	   panel.setBackground(Color.WHITE);
	   setLocationRelativeTo(null);
	   add(panel, BorderLayout.CENTER);
	}

	public static final int DEFAULT_WIDTH = 190;
    public static final int DEFAULT_HEIGHT = 210;  

}
