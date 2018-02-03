
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class AppConsulta {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame marco=new MarcoAplicacion();
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marco.setVisible(true);
	}
}

class MarcoAplicacion extends JFrame{
	
	public MarcoAplicacion(){
		setTitle("Consulta BBDD");
		setBounds(500, 300, 400, 400);
		setLayout(new BorderLayout());
		JPanel menus=new JPanel();
		menus.setLayout(new FlowLayout());
		secciones=new JComboBox();
		secciones.setEditable(false);
		secciones.addItem("Todos");
		paises=new JComboBox();
		paises.setEditable(false);
		paises.addItem("Todos");
		resultado=new JTextArea(4, 50);
		resultado.setEditable(false);
		add(resultado);
		menus.add(secciones);
		menus.add(paises);
		add(menus, BorderLayout.NORTH);
		add(resultado, BorderLayout.CENTER);
		JButton botonConsulta=new JButton("Consulta");
		botonConsulta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ejecutaConsulta();
			}
			
		});
		add(botonConsulta, BorderLayout.SOUTH);
		
		//----------------CONEXIÓN BBDD------------------------------
		try {

			conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/pruebas", "root", "root");
			Statement sentencia=conexion.createStatement();
			//------------------CARGA JCOMBOBOX SECCIONES-----------------
			String consulta="SELECT DISTINCTROW SECCIÓN FROM PRODUCTOS";
			ResultSet rs=sentencia.executeQuery(consulta);
			
			while(rs.next()){
				secciones.addItem(rs.getString(1));
			}
			rs.close();
			//------------------CARGA JCOMBOBOX PAISES-----------------
			consulta="SELECT DISTINCTROW PAÍSDEORIGEN FROM PRODUCTOS";
			rs=sentencia.executeQuery(consulta);
			
			while(rs.next()){
				paises.addItem(rs.getString(1));
			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//----------------------FIN DE CONEXIÓN CON LA BBDD
	}
	
	private void ejecutaConsulta(){
		ResultSet rs=null;
		try{
			String seccion=(String)secciones.getSelectedItem();
			enviaConsultaSeccion=conexion.prepareStatement(consultaSeccion);
			enviaConsultaSeccion.setString(1, seccion);
			rs=enviaConsultaSeccion.executeQuery();
			
			while (rs.next()){
				resultado.append(rs.getString(1)+", ");
				resultado.append(rs.getString(2)+", ");
				resultado.append(rs.getString(3)+", ");
				resultado.append(rs.getString(4)+".\n");
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private JComboBox secciones;
	private JComboBox paises;
	private JTextArea resultado;
	private Connection conexion;
	private PreparedStatement enviaConsultaSeccion;
	private final String consultaSeccion="SELECT NOMBREARTÍCULO, SECCIÓN, PRECIO, PAÍSDEORIGEN "
										 + "FROM PRODUCTOS WHERE SECCIÓN=?";
}

