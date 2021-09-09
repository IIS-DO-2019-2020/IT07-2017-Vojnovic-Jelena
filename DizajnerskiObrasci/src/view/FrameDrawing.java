package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import controller.DrawingController;
import shapes.Point;

public class FrameDrawing extends JFrame {
	

	private DrawingView view = new DrawingView();
	private DrawingController controller;

	private final int OPERATION_DRAWING = 1;
	private final int OPERATION_EDIT_DELETE = 0;
	
	private int activeOperation = OPERATION_DRAWING;


	private ButtonGroup btnsOperation = new ButtonGroup();
	private ButtonGroup btnsShapes = new ButtonGroup();
	
	private JToggleButton btnOperationDrawing = new JToggleButton("Crtanje");
	private JToggleButton btnOperationEditOrDelete = new JToggleButton("Selektuj");
	private JButton btnActionEdit = new JButton("Izmeni");
	private JButton btnActionDelete = new JButton("Obrisi");
	private JToggleButton btnShapePoint = new JToggleButton("Tacka");
	private JToggleButton btnShapeLine = new JToggleButton("Linija");
	private JToggleButton btnShapeRectangle = new JToggleButton("Pravougaonik");
	private JToggleButton btnShapeCircle = new JToggleButton("Krug");
	private JToggleButton btnShapeDonut = new JToggleButton("Krofna");
	private JButton btnColorEdge = new JButton("Boja linije");
	private JButton btnColorInner = new JButton("Boja unutrasnjosti");
	
	private Color edgeColor = Color.BLACK, innerColor = Color.WHITE;
	
	private boolean lineWaitingForSecondPoint = false;
	private Point lineFirstPoint;
	
	private JPanel contentPane;
	
	public DrawingView getView() {
		return view;
	}

	public void setController(DrawingController controller) {
		this.controller = controller;
		controller.setCurrState(OPERATION_DRAWING);
	}
	
	
	public int getOPERATION_DRAWING() {
		return OPERATION_DRAWING;
	}

	public int getOPERATION_EDIT_DELETE() {
		return OPERATION_EDIT_DELETE;
	}

	public int getActiveOperation() {
		return activeOperation;
	}
	
	public void setActiveOperation(int operation) {
		this.activeOperation = operation;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameDrawing frame = new FrameDrawing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameDrawing() {
		setTitle("IT 38-2017 Maksimovic Milana");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 700);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(1100, 700));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.OperationDrawing(arg0);
			}
		});		
		contentPane.add(view, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(4, 0, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		btnOperationDrawing.setSelected(true);
		
		btnOperationDrawing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setCurrState(OPERATION_DRAWING);
			}
		});
		btnOperationDrawing.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsOperation.add(btnOperationDrawing);
		panel_2.add(btnOperationDrawing);
		
		btnOperationEditOrDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setCurrState(OPERATION_EDIT_DELETE);;
			}
		});
		btnOperationEditOrDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsOperation.add(btnOperationEditOrDelete);
		panel_2.add(btnOperationEditOrDelete);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		
		btnActionEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.OperationEdit(e);			}
		});
		btnActionEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_3.add(btnActionEdit);
		
		btnActionDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.OperationDelete(e);
			}
		});
		btnActionDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_3.add(btnActionDelete);
		
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		btnShapePoint.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapePoint);
		panel_4.add(btnShapePoint);
		
		btnShapeLine.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeLine);
		panel_4.add(btnShapeLine);
		
		btnShapeRectangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeRectangle);
		panel_4.add(btnShapeRectangle);
		
		btnShapeCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeCircle);
		panel_4.add(btnShapeCircle);
		
		btnShapeDonut.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnsShapes.add(btnShapeDonut);
		panel_4.add(btnShapeDonut);
		
		
		
		
		btnOperationDrawing.setSelected(true);
		//setOperationDrawing();
		btnShapePoint.setSelected(true);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
	
		btnColorEdge = new JButton("Boja ivice");
		btnColorEdge.addActionListener(btnColorEdgeClickListener());
		btnColorEdge.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_5.add(btnColorEdge);
		
		btnColorInner = new JButton("Boja unutrasnjosti");
		btnColorInner.addActionListener(btnColorInnerClickListener());
		btnColorInner.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_5.add(btnColorInner);
	}
	
	private ActionListener btnColorEdgeClickListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edgeColor = JColorChooser.showDialog(null, "Izaberite boju ivice", edgeColor);
				if (edgeColor == null) edgeColor = Color.BLACK;
			}
		};
	}
	
	private ActionListener btnColorInnerClickListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				innerColor = JColorChooser.showDialog(null, "Izaberite boju unutrasnjosti", innerColor);
				if (innerColor == null) innerColor = Color.WHITE;
			}
		};
	}
	
	
	public void update(int currState) {

		if(OPERATION_DRAWING == currState) {
			
			btnActionEdit.setEnabled(false);
			btnActionDelete.setEnabled(false);
			
			btnShapePoint.setEnabled(true);
			btnShapeLine.setEnabled(true);
			btnShapeRectangle.setEnabled(true);
			btnShapeCircle.setEnabled(true);
			btnShapeDonut.setEnabled(true);
			
			btnColorEdge.setEnabled(true);
			btnColorInner.setEnabled(true);
		}
		
		if(OPERATION_EDIT_DELETE == currState) {
			
			
			btnActionEdit.setEnabled(true);
			btnActionDelete.setEnabled(true);
			
			btnShapePoint.setEnabled(false);
			btnShapeLine.setEnabled(false);
			btnShapeRectangle.setEnabled(false);
			btnShapeCircle.setEnabled(false);
			btnShapeDonut.setEnabled(false);
			
			btnColorEdge.setEnabled(false);
			btnColorInner.setEnabled(false);
		}
		
	}
	
	public ButtonGroup getBtnsOperation() {
		return btnsOperation;
	}

	public void setBtnsOperation(ButtonGroup btnsOperation) {
		this.btnsOperation = btnsOperation;
	}

	public ButtonGroup getBtnsShapes() {
		return btnsShapes;
	}

	public void setBtnsShapes(ButtonGroup btnsShapes) {
		this.btnsShapes = btnsShapes;
	}

	public JToggleButton getBtnOperationDrawing() {
		return btnOperationDrawing;
	}

	public void setBtnOperationDrawing(JToggleButton btnOperationDrawing) {
		this.btnOperationDrawing = btnOperationDrawing;
	}

	public JToggleButton getBtnOperationEditOrDelete() {
		return btnOperationEditOrDelete;
	}

	public void setBtnOperationEditOrDelete(JToggleButton btnOperationEditOrDelete) {
		this.btnOperationEditOrDelete = btnOperationEditOrDelete;
	}

	public JButton getBtnActionEdit() {
		return btnActionEdit;
	}

	public void setBtnActionEdit(JButton btnActionEdit) {
		this.btnActionEdit = btnActionEdit;
	}

	public JButton getBtnActionDelete() {
		return btnActionDelete;
	}

	public void setBtnActionDelete(JButton btnActionDelete) {
		this.btnActionDelete = btnActionDelete;
	}

	public JToggleButton getBtnShapePoint() {
		return btnShapePoint;
	}

	public void setBtnShapePoint(JToggleButton btnShapePoint) {
		this.btnShapePoint = btnShapePoint;
	}

	public JToggleButton getBtnShapeLine() {
		return btnShapeLine;
	}

	public void setBtnShapeLine(JToggleButton btnShapeLine) {
		this.btnShapeLine = btnShapeLine;
	}

	public JToggleButton getBtnShapeRectangle() {
		return btnShapeRectangle;
	}

	public void setBtnShapeRectangle(JToggleButton btnShapeRectangle) {
		this.btnShapeRectangle = btnShapeRectangle;
	}

	public JToggleButton getBtnShapeCircle() {
		return btnShapeCircle;
	}

	public void setBtnShapeCircle(JToggleButton btnShapeCircle) {
		this.btnShapeCircle = btnShapeCircle;
	}

	public JToggleButton getBtnShapeDonut() {
		return btnShapeDonut;
	}

	public void setBtnShapeDonut(JToggleButton btnShapeDonut) {
		this.btnShapeDonut = btnShapeDonut;
	}

	public JButton getBtnColorEdge() {
		return btnColorEdge;
	}

	public void setBtnColorEdge(JButton btnColorEdge) {
		this.btnColorEdge = btnColorEdge;
	}

	public JButton getBtnColorInner() {
		return btnColorInner;
	}

	public void setBtnColorInner(JButton btnColorInner) {
		this.btnColorInner = btnColorInner;
	}

	public Color getEdgeColor() {
		return edgeColor;
	}

	public void setEdgeColor(Color edgeColor) {
		this.edgeColor = edgeColor;
	}

	public Color getInnerColor() {
		return innerColor;
	}

	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
	}

	public boolean isLineWaitingForSecondPoint() {
		return lineWaitingForSecondPoint;
	}

	public void setLineWaitingForSecondPoint(boolean lineWaitingForSecondPoint) {
		this.lineWaitingForSecondPoint = lineWaitingForSecondPoint;
	}

	public Point getLineFirstPoint() {
		return lineFirstPoint;
	}

	public void setLineFirstPoint(Point lineFirstPoint) {
		this.lineFirstPoint = lineFirstPoint;
	}

	public void setView(DrawingView view) {
		this.view = view;
	}

}
