package ujianOOP;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public final class BikeFrame extends JFrame implements ActionListener, MouseListener {

    // button
    private JButton btnUpdate = new JButton("Update");
    private JButton btnInsert = new JButton("Insert");
    private JButton btnDelete = new JButton("Delete");
    
    // table
    private JScrollPane scrollPane = new JScrollPane();
    private JTable table = new JTable();
    private DefaultTableModel dtm = new DefaultTableModel(new Object[]{"Kode", "Nama", "Jenis", "Jumlah Gigi", "Atur Tinggi", "Lampu", "Stok"}, 0);

    // label
    private JLabel titleLabel = new JLabel(" ");
    private JLabel lblKode = new JLabel("Kode",JLabel.CENTER);
    private JLabel lblNama = new JLabel("Nama",JLabel.CENTER);
    private JLabel lblJenis = new JLabel("Jenis",JLabel.CENTER);
    private JLabel lblJmlGigi = new JLabel("Jumlah Gigi (3 - 7)",JLabel.CENTER);
    private JLabel lblAturTinggi = new JLabel("Atur Tinggi",JLabel.CENTER);
    private JLabel lblLampu = new JLabel("Lampu",JLabel.CENTER);
    private JLabel lblStok = new JLabel("Stok",JLabel.CENTER);

    private JTextField txtKode = new JTextField("Auto Generate!");
    private JTextField txtNama = new JTextField();
    private JTextField txtJmlGigi = new JTextField();
    private JTextField txtAturTinggi = new JTextField();
    private JTextField txtLampu = new JTextField();
    private JTextField txtStok = new JTextField();

    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel isi = new JPanel(new GridLayout(10,2));
    private JPanel buttons = new JPanel(new FlowLayout());
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton spdlipat = new JRadioButton("Sepeda Lipat");
    private JRadioButton spdgunung = new JRadioButton("Sepeda Gunung");
    private JPanel jPanelNor = new JPanel();
    

    // connection
    private Connect conn = new Connect();
    private ResultSet rs = null;
    private ResultSetMetaData rsm = null;
    private Vector<String> headerTable = new Vector<String>();
    private Vector<Vector<String>> dataTable = new Vector<Vector<String>>();

    public BikeFrame() {
    	    
        createForm();
        txtKode.setEditable(false);
        txtLampu.setEditable(false);
        
        buttonGroup.add(spdlipat);        
        buttonGroup.add(spdgunung);
        jPanelNor.add(spdlipat);
        jPanelNor.add(spdgunung);
        
//        isi.add(label);
        isi.add(jPanelNor);

        
        isi.add(lblKode);isi.add(txtKode);
        isi.add(lblNama);isi.add(txtNama);
        isi.add(lblJenis);isi.add(jPanelNor);
        isi.add(lblJmlGigi);isi.add(txtJmlGigi);
        isi.add(lblAturTinggi);isi.add(txtAturTinggi);
        isi.add(lblLampu);isi.add(txtLampu);
        isi.add(lblStok);isi.add(txtStok);
        
        buttons.add(btnUpdate);
        buttons.add(btnInsert);
        buttons.add(btnDelete);
        
        panel.add(isi, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        
        btnUpdate.addActionListener(this);
        btnInsert.addActionListener(this);
        btnDelete.addActionListener(this);
        table.addMouseListener(this);
        spdlipat.addActionListener(sliceActionListener);
        spdgunung.addActionListener(sliceActionListener);

        scrollPane.setViewportView(table);
        table.setModel(dtm);

        refreshData();

        this.setVisible(true);
    }
    
    ActionListener sliceActionListener = new ActionListener() {
	    public void actionPerformed(ActionEvent actionEvent) {
	      AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	      int kode;
	      String nama_kode;
	      if(aButton.getText() == "Sepeda Lipat")
	      {
	    	  nama_kode = "LT";
	    	  txtLampu.setText("False"); 
	    	  rs = conn.executeQuery("SELECT REGEXP_SUBSTR(KodeSepeda,\"[0-9]+\") as kode FROM `bike` WHERE JenisSepeda='"+aButton.getText()+"' ORDER BY KodeSepeda desc LIMIT 1");
	      }else {
	    	  nama_kode = "GN";
	    	  rs = conn.executeQuery("SELECT REGEXP_SUBSTR(KodeSepeda,\"[0-9]+\") as kode FROM `bike` WHERE JenisSepeda='"+aButton.getText()+"' ORDER BY KodeSepeda desc LIMIT 1");
	    	  txtLampu.setText("True");
	      }
	      
	      try {
    		  while (rs.next()) {
    			  kode = rs.getInt(1)+1;
    			  System.out.println(rs.getInt(1));
    			  txtKode.setText(nama_kode+String.format("%03d", kode));
    		  }
	      } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	      }
		      
	    }
	  };
    
    public void refreshData() {
        int x = dtm.getRowCount();
        for(int i=0;i<x;i++){
            dtm.removeRow(0);
        }
        rs = conn.executeQuery("SELECT * FROM bike");
        Vector<String> vec = new Vector<String>();
        try {
            while (rs.next()) {
                vec = new Vector<String>();
                vec.add(rs.getString(1));
                vec.add(rs.getString(2));
                vec.add(rs.getString(3));
                vec.add(rs.getString(4));
                vec.add(rs.getString(5));
                vec.add(rs.getString(6));
                vec.add(rs.getString(7));
                dtm.addRow(vec);
            }
        } catch (Exception ex) {}
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnUpdate || e.getSource() == btnInsert || e.getSource() == btnDelete){
        	String kode = txtKode.getText();
            String nama = txtNama.getText();
            String jenis = buttonGroup.getElements().nextElement().getText();
            String jmlgigi = txtJmlGigi.getText();
            String aturtinggi = txtAturTinggi.getText();
            String lampu = txtLampu.getText();
            String stok = txtStok.getText();
            
            if(kode.equals("") || 
            		nama.equals("") || 
            		jenis.equals("") || 
            		jmlgigi.equals("") || 
            		aturtinggi.equals("")  || 
            		lampu.equals("") || 
            		stok.equals(""))
            {
            	JOptionPane.showMessageDialog(null, "Input tidak boleh kosong!", "", JOptionPane.ERROR_MESSAGE);return;
            }else if(!isNumeric(stok)) 
            {
            	JOptionPane.showMessageDialog(null, "Input Stok wajib angka!", "", JOptionPane.ERROR_MESSAGE);return;
            }else if(!isNumeric(aturtinggi)) 
            {
            	JOptionPane.showMessageDialog(null, "Input Atur Tinggi wajiib angka!", "", JOptionPane.ERROR_MESSAGE);return;
            }
            else if(Integer.parseInt(jmlgigi) < 3 || Integer.parseInt(jmlgigi) > 7) 
            {
            	JOptionPane.showMessageDialog(null, "Input Stok wajib antara 3 - 7!", "", JOptionPane.ERROR_MESSAGE);return;
            }
            
            int input_lampu = 0;
            if(lampu == "True") 
            {
            	input_lampu = 1;
            }
            
            String input_jenis = "Sepeda Lipat";
            if(jenis == "Sepeda Lipat") {
            	input_jenis = "Sepeda Gunung";
            }
            
            if(e.getSource() == btnInsert) 
            {
            	String check_kode = null;
            	rs = conn.executeQuery("SELECT * FROM `bike` WHERE KodeSepeda='"+kode+"'");
            	System.out.println(rs);
	            try {
		      		  while (rs.next()) {
		      			  check_kode = rs.getString(1);
		      			  System.out.println(check_kode);
		      		  }
		  	      } catch (SQLException e1) {
		  				// TODO Auto-generated catch block
		  				e1.printStackTrace();
		  	      }
	            
	            if(!check_kode.equals("")) 
	            {
	            	JOptionPane.showMessageDialog(null, "Tidak dapat insert karena data sudah ada!", "", JOptionPane.ERROR_MESSAGE);return;
	            }
		            
	            conn.executeUpdate("INSERT INTO bike(KodeSepeda, NamaSepeda, JenisSepeda, JumlahGigi, AturTinggi, Lampu, Stok) VALUES ('"+kode+"','"+nama+"','"+jenis+"','"+jmlgigi+"','"+aturtinggi+"','"+input_lampu+"','"+stok+"')");
	            refreshData();
	            JOptionPane.showMessageDialog(null, "Sukses tambah : "+ nama + " ("+kode+")");
            }else if(e.getSource() == btnDelete){
            	conn.executeUpdate("DELETE FROM `bike` WHERE KodeSepeda='"+kode+"'");
	            refreshData();
	            JOptionPane.showMessageDialog(null, "Sukses delete data");
            }else {
                conn.executeUpdate("UPDATE bike SET NamaSepeda='"+nama+"',JenisSepeda='"+input_jenis+"',JumlahGigi='"+jmlgigi+"',AturTinggi='"+aturtinggi+"',Lampu='"+input_lampu+"',Stok='"+stok+"' WHERE KodeSepeda='"+kode+"'");
                refreshData();
                JOptionPane.showMessageDialog(null, "Sukses update : "+ nama + " ("+kode+")");
            }
            	
        }
    }
    
    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
	}

	public void mouseClicked(MouseEvent e) {
        if(e.getSource()==table){
            try{
                txtKode.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                txtNama.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                txtJmlGigi.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
                txtAturTinggi.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
                txtLampu.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
                txtStok.setText(table.getValueAt(table.getSelectedRow(), 6).toString());
                
                if (table.getValueAt(table.getSelectedRow(), 2).toString().equals("Sepeda Gunung")) {
                    spdgunung.setSelected(true);
               } else { 
                    spdlipat.setSelected(true);
               }
                
            } catch (Exception f){}
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}

    private void createForm() {
        this.setTitle("Aplikasi GUI Bike");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        new BikeFrame();
    }
}
