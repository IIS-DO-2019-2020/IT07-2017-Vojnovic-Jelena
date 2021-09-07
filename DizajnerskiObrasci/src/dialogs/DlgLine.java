package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

import shapes.Line;
import shapes.Point;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgLine extends JDialog {
	private JTextField txtFirstX;
	private JTextField txtFirstY;
	private JTextField txtSecondX;
	private JTextField txtSecondY;
	
	private Line line = null;
	private Color edgeColor = null;

	private boolean isSelected = false;
	
	private JButton btnEdgeColor = new JButton(" ");
	public DlgLine() {
		setResizable(false);
		setTitle("IT07-2017 Vojnovic Jelena");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 300, 210);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(5, 2, 0, 0));
			{
				JLabel lblNewLabel_1 = new JLabel("Prva X koordinata", SwingConstants.CENTER);
				panel.add(lblNewLabel_1);
			}
			{
				txtFirstX = new JTextField();
				panel.add(txtFirstX);
				txtFirstX.setColumns(10);
			}
			{
				JLabel lblNewLabel = new JLabel("Prva Y koordinata");
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel);
			}
			{
				txtFirstY = new JTextField();
				panel.add(txtFirstY);
				txtFirstY.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Druga X koordinata");
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel_2);
			}
			{
				txtSecondX = new JTextField();
				panel.add(txtSecondX);
				txtSecondX.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Druga Y koordinata");
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel_2);
			}
			{
				txtSecondY = new JTextField();
				panel.add(txtSecondY);
				txtSecondY.setColumns(10);
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
							int newFirstX = Integer.parseInt(txtFirstX.getText());
							int newFirstY = Integer.parseInt(txtFirstY.getText());
							int newSecondX = Integer.parseInt(txtSecondX.getText());
							int newSecondY = Integer.parseInt(txtSecondY.getText());

							if(newFirstX < 0 || newFirstY < 0 || newSecondX < 0 || newSecondY < 0) {
								JOptionPane.showMessageDialog(null, "Uneli ste pogresne podatke!", "Greska!", JOptionPane.ERROR_MESSAGE);
								return;
							}
							line = new Line(new Point(newFirstX, newFirstY), new Point(newSecondX, newSecondY), edgeColor);
							line.setSelected(isSelected);
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

	public Line getLine() {
		return line;
	}

	public Color getEdgeColor() {
		return edgeColor;
	}
	
	public void setLine(Line line) {
		txtFirstX.setText("" + line.getStartPoint().getX());
		txtFirstY.setText("" + line.getStartPoint().getY());
		txtSecondX.setText("" + line.getEndPoint().getX());
		txtSecondY.setText("" + line.getEndPoint().getY());
		edgeColor = line.getEdgeColor();
		isSelected = line.isSelected();
		
		btnEdgeColor.setBackground(line.getEdgeColor());
	}
}
