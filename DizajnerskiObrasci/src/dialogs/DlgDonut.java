package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

import shapes.Donut;
import shapes.Point;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgDonut extends JDialog {
	private JTextField txtX;
	private JTextField txtY;
	private JTextField txtRadius;
	private JTextField txtInnerRadius;
	
	private Donut donut = null;
	private Color edgeColor = null, innerColor = null;

	private boolean isSelected = false;
	
	private JButton btnEdgeColor = new JButton(" ");
	private JButton btnInnerColor = new JButton(" ");
	public DlgDonut() {
		setResizable(false);
		setTitle("IT07-2017 Vojnovic Jelena");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 258, 213);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(6, 2, 0, 0));
			{
				JLabel lblNewLabel_1 = new JLabel("X koordinata", SwingConstants.CENTER);
				panel.add(lblNewLabel_1);
			}
			{
				txtX = new JTextField();
				panel.add(txtX);
				txtX.setColumns(10);
			}
			{
				JLabel lblNewLabel = new JLabel("Y koordinata");
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel);
			}
			{
				txtY = new JTextField();
				panel.add(txtY);
				txtY.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Radijus");
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel_2);
			}
			{
				txtRadius = new JTextField();
				panel.add(txtRadius);
				txtRadius.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Unutrasnji radijus");
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel_2);
			}
			{
				txtInnerRadius = new JTextField();
				panel.add(txtInnerRadius);
				txtInnerRadius.setColumns(10);
			}
			{
				JLabel lblEdgeColor = new JLabel("Boja ivice");
				lblEdgeColor.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblEdgeColor);
			}
			{
				btnEdgeColor.setHorizontalAlignment(SwingConstants.CENTER);
				btnEdgeColor.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						edgeColor = JColorChooser.showDialog(null, "Izaberite boju ivice", edgeColor);
						if (edgeColor == null) edgeColor = Color.BLACK;
						btnEdgeColor.setBackground(edgeColor);
					}
				});
				panel.add(btnEdgeColor);
			}
			{
				JLabel lblInnerColor = new JLabel("Boja unutrasnjosti");
				lblInnerColor.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblInnerColor);
			}
			{
				btnInnerColor.setHorizontalAlignment(SwingConstants.CENTER);
				btnInnerColor.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						innerColor = JColorChooser.showDialog(null, "Izaberite boju unutrasnjosti", innerColor);
						if (innerColor == null) innerColor = Color.WHITE;
						btnInnerColor.setBackground(innerColor);
					}
				});
				panel.add(btnInnerColor);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btnOk = new JButton("Potvrdi");
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							int newX = Integer.parseInt(txtX.getText());
							int newY = Integer.parseInt(txtY.getText());
							int newRadius = Integer.parseInt(txtRadius.getText());
							int newInnerRadius = Integer.parseInt(txtInnerRadius.getText());

							if(newX < 0 || newY < 0 || newRadius < 1 || newInnerRadius < 1 || newInnerRadius >= newRadius) {
								JOptionPane.showMessageDialog(null, "Uneli ste pogresne podatke!", "Greska!", JOptionPane.ERROR_MESSAGE);
								return;
							}
							donut = new Donut(new Point(newX, newY), newRadius, newInnerRadius, edgeColor, innerColor);
							donut.setSelected(isSelected);
							dispose();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Uneli ste pogresne podatke!", "Greska!", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				panel.add(btnOk);
			}
			{
				JButton btnNotOk = new JButton("Odustani");
				btnNotOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				panel.add(btnNotOk);
			}
		}
	}

	public Donut getDonut() {
		return donut;
	}
	
	public void setPoint(Point point) {
		txtX.setText("" + point.getX());
		txtY.setText("" + point.getY());
	}
	
	public void setColors(Color edgeColor, Color innerColor) {
		this.edgeColor = edgeColor;
		this.innerColor = innerColor;
		
		btnEdgeColor.setBackground(edgeColor);
		btnInnerColor.setBackground(innerColor);
	}

	public Color getEdgeColor() {
		return edgeColor;
	}
	
	public Color getInnerColor()
	{
		return innerColor;
	}
	
	public void setDonut(Donut donut) {
		txtX.setText("" + donut.getCenter().getX());
		txtY.setText("" + donut.getCenter().getY());
		txtRadius.setText("" + donut.getRadius());
		txtInnerRadius.setText("" + donut.getInnerRadius());
		edgeColor = donut.getEdgeColor();
		innerColor = donut.getInnerColor();
		
		isSelected = donut.isSelected();
		
		setColors(donut.getEdgeColor(), donut.getInnerColor());
	}
}
