package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import controller.DrawingController;

public class DrawingFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DrawingView view = new DrawingView();
	private DrawingController controller;
	
	private JToggleButton btnPoint;
	private JToggleButton btnLine;
	private JToggleButton btnRectangle;
	private JToggleButton btnCircle;
	private JToggleButton btnDonut;
	private JToggleButton btnHexagon;
	
	private JButton btnFront;
	private JButton btnBack;
	private JButton btnToFront;
	private JButton btnToBack;
	private JToggleButton btnSelect;
	private JButton btnDelete;
	private JButton btnEdit;
	private JButton btnUndo;
	private JButton btnRedo;
	private JPanel northPanel; 
	private JScrollPane scrollPane; 
	private JTextArea textArea; 

	
	private JButton btnEdgeColor;
	private JButton btnInnerColor;
	
	private JSeparator separator;
	private JSeparator separator_1;
	
	
	public DrawingView getView() {
		return view;
	}
	
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawingFrame frame = new DrawingFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	*/
	public DrawingFrame() {
		
		setTitle("IT7-2017 Jelena Vojnovic");
		
		view.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				controller.mouseClicked(e);

			}
		});
		
		getContentPane().add(view, BorderLayout.CENTER);
		view.setLayout(new BorderLayout(0, 0));
		
		ButtonGroup buttonGroup = new ButtonGroup();

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.PINK);
		toolBar.setMaximumSize(new Dimension(67, 2));
		toolBar.setOrientation(SwingConstants.VERTICAL);
		view.add(toolBar, BorderLayout.WEST);

		btnPoint = new JToggleButton("Point");
		btnPoint.setPreferredSize(new Dimension(75, 25));
		btnPoint.setMinimumSize(new Dimension(75, 25));
		btnPoint.setMaximumSize(new Dimension(75, 25));
		btnPoint.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		toolBar.add(btnPoint);
		buttonGroup.add(btnPoint);

		btnLine = new JToggleButton("Line");
		btnLine.setPreferredSize(new Dimension(75, 25));
		btnLine.setMinimumSize(new Dimension(75, 25));
		btnLine.setMaximumSize(new Dimension(75, 25));
		toolBar.add(btnLine);
		buttonGroup.add(btnLine);

		btnRectangle = new JToggleButton("Rectangle");
		btnRectangle.setPreferredSize(new Dimension(75, 25));
		btnRectangle.setMinimumSize(new Dimension(75, 25));
		btnRectangle.setMaximumSize(new Dimension(75, 25));
		toolBar.add(btnRectangle);
		buttonGroup.add(btnRectangle);

		btnHexagon = new JToggleButton("Hexagon");
		btnHexagon.setPreferredSize(new Dimension(75, 25));
		btnHexagon.setMinimumSize(new Dimension(75, 25));
		btnHexagon.setMaximumSize(new Dimension(75, 25));
		toolBar.add(btnHexagon);
		buttonGroup.add(btnHexagon);

		btnCircle = new JToggleButton("Circle");
		btnCircle.setPreferredSize(new Dimension(75, 25));
		btnCircle.setMinimumSize(new Dimension(75, 25));
		btnCircle.setMaximumSize(new Dimension(75, 25));
		toolBar.add(btnCircle);
		buttonGroup.add(btnCircle);

		btnDonut = new JToggleButton("Donut");
		btnDonut.setPreferredSize(new Dimension(75, 25));
		btnDonut.setMinimumSize(new Dimension(75, 25));
		btnDonut.setMaximumSize(new Dimension(75, 25));
		toolBar.add(btnDonut);
		buttonGroup.add(btnDonut);
		
		separator_1 = new JSeparator();
		toolBar.add(separator_1);
		//*****************************//
		
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setBackground(Color.PINK);
		toolBar_1.setMinimumSize(new Dimension(67, 25));
		toolBar_1.setMaximumSize(new Dimension(67, 25));
		toolBar_1.setOrientation(SwingConstants.VERTICAL);
		view.add(toolBar_1, BorderLayout.EAST);

		btnFront = new JButton("Front");
		btnFront.setPreferredSize(new Dimension(75, 25));
		btnFront.setMinimumSize(new Dimension(75, 25));
		btnFront.setMaximumSize(new Dimension(75, 25));
		btnFront.setEnabled(false);
		
		
		toolBar_1.add(btnFront);
		
		btnBack = new JButton("Back");
		btnBack.setPreferredSize(new Dimension(75, 25));
		btnBack.setMinimumSize(new Dimension(75, 25));
		btnBack.setMaximumSize(new Dimension(75, 25));
		btnBack.setEnabled(false);
		
	
		toolBar_1.add(btnBack);
		
		btnToFront = new JButton("To Front");
		btnToFront.setPreferredSize(new Dimension(75, 25));
		btnToFront.setMinimumSize(new Dimension(75, 25));
		btnToFront.setMaximumSize(new Dimension(75, 25));
		btnToFront.setEnabled(false);
		
		
	
		toolBar_1.add(btnToFront);
		
		btnToBack = new JButton("To Back");
		btnToBack.setPreferredSize(new Dimension(75, 25));
		btnToBack.setMinimumSize(new Dimension(75, 25));
		btnToBack.setMaximumSize(new Dimension(75, 25));
		btnToBack.setEnabled(false);
		
	
		toolBar_1.add(btnToBack);
		
		separator = new JSeparator();
		toolBar_1.add(separator);
		
	//
		
		//TEXT AREA
		
		JPanel panel = new JPanel();
		view.add(panel, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setForeground(Color.WHITE);
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setPreferredSize(new Dimension(400, 1000));
		textArea.setEditable(false);

		northPanel = new JPanel();
		northPanel.setBackground(Color.PINK);
		northPanel.setMinimumSize(new Dimension(100, 100));
		view.add(northPanel, BorderLayout.NORTH);
		
		
		btnSelect = new JToggleButton("Select");
		btnSelect.setPreferredSize(new Dimension(70, 25));
		btnSelect.setMinimumSize(new Dimension(70, 25));
		btnSelect.setMaximumSize(new Dimension(70, 25));
		northPanel.add(btnSelect);
		buttonGroup.add(btnSelect);
		
		btnEdit = new JButton("Edit");
		btnEdit.setPreferredSize(new Dimension(60, 25));
		btnEdit.setMinimumSize(new Dimension(60, 25));
		btnEdit.setMaximumSize(new Dimension(60, 25));
		btnEdit.setEnabled(true);
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.edit(e);
			}
		});
		northPanel.add(btnEdit);
		buttonGroup.add(btnEdit);
		
		btnDelete = new JButton("Delete");
		btnDelete.setPreferredSize(new Dimension(75, 25));
		btnDelete.setMinimumSize(new Dimension(75, 25));
		btnDelete.setMaximumSize(new Dimension(75, 25));
		btnDelete.setEnabled(true);
		
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.delete(e);
			}
		});
		
		
		northPanel.add(btnDelete);
		buttonGroup.add(btnDelete);
		
		btnUndo = new JButton("Undo");
		btnUndo.setEnabled(false);

		northPanel.add(btnUndo);
		
		
		btnRedo = new JButton("Redo");
		btnRedo.setEnabled(false);
		
		
		northPanel.add(btnRedo);
		
		btnInnerColor = new JButton("Inner color");
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnInnerColor.setBackground(JColorChooser.showDialog(null, "Choose inside color", Color.white));
				if (btnInnerColor.getBackground().equals(Color.BLACK)) {
					btnInnerColor.setForeground(Color.WHITE);
				}
			}
		});
		btnInnerColor.setBackground(SystemColor.control);
		northPanel.add(btnInnerColor);

		btnEdgeColor = new JButton("Edge color");
		btnEdgeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEdgeColor.setBackground(JColorChooser.showDialog(null, "Choose outside color", Color.black));
				if (btnEdgeColor.getBackground().equals(Color.white)) {
					btnEdgeColor.setForeground(Color.black);
				}
			}
		});
		btnEdgeColor.setForeground(Color.WHITE);
		btnEdgeColor.setBackground(Color.BLACK);
		northPanel.add(btnEdgeColor);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setAutoscrolls(true);
		scrollPane.setPreferredSize(new Dimension(20, 64));
		scrollPane.setViewportView(textArea);
		view.add(scrollPane, BorderLayout.SOUTH);
		
	}
	
	public JToggleButton getBtnPoint() {
		return btnPoint;
	}

	public JToggleButton getBtnLine() {
		return btnLine;
	}

	public JToggleButton getBtnRectangle() {
		return btnRectangle;
	}

	public JToggleButton getBtnHexagon() {
		return btnHexagon;
	}

	public JToggleButton getBtnCircle() {
		return btnCircle;
	}

	public JToggleButton getBtnDonut() {
		return btnDonut;
	}

	public JButton getBtnFront() {
		return btnFront;
	}

	public JButton getBtnBack() {
		return btnBack;
	}

	public JButton getBtnToFront() {
		return btnToFront;
	}

	public JButton getBtnToBack() {
		return btnToBack;
	}

	public JToggleButton getBtnSelect() {
		return btnSelect;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnEdit() {
		return btnEdit;
	}

	public JButton getBtnUndo() {
		return btnUndo;
	}

	public JButton getBtnRedo() {
		return btnRedo;
	}

	public JTextArea getTextArea() {
		return textArea;

	}
	public JButton getBtnInnerColor() {
		return btnInnerColor;
	}
	public JButton getBtnEdgeColor() {
		return btnEdgeColor;
	}
	
	public void setController(DrawingController controller) {
		this.controller = controller;
	}


}
