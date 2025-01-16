/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sys;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
public class crudTable {
    
    private final String jdbcURL="jdbc:mysql://localhost:3306/2210010690_pertanahan";
    private final String username="root";
    private final String password="";
    
    private DefaultTableModel Modelnya;
    private TableColumn Kolomnya;
    
    public crudTable (){}
    
    public Connection getKoneksiDB() throws SQLException{ //menampung data koneksi
        try {
            //objek untuk menginstal jdbc driver
            Driver mysqldriver = new com.mysql.cj.jdbc.Driver(); 
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Koneksi DB Berhasil");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return DriverManager.getConnection(jdbcURL,username,password);
    }
    
    //untuk mengecek 
    public boolean DuplicateKey(String NamaTable, String PrimaryKey, String IsiData) {
        boolean hasil = false;
        int jumlah = 0;
        try {
            String SQL = "SELECT * FROM " +NamaTable +" WHILE " +PrimaryKey+" = "+IsiData+"'";
        Statement perintah = getKoneksiDB().createStatement();
        ResultSet hasilData = perintah.executeQuery(SQL);
        while(hasilData.next()){
            jumlah++;
        }
        if (jumlah == 1) { hasil = true; } else { hasil = false; }
        } catch (Exception e) {
             System.out.println(e.toString());
        }
        return hasil;
    }
    
    public String getFieldTable(String[] FieldTablenya){
        String hasilnya="";
        int deteksiIndexAkhir=FieldTablenya.length-1;
        try {
            for (int i = 0; 1< FieldTablenya.length; i++){
                
                if (i==deteksiIndexAkhir){
                    hasilnya=hasilnya+FieldTablenya[i];
                }else{
                    hasilnya=hasilnya+FieldTablenya[i]+",";
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "("+hasilnya+")";
    }
    
    public String getIsiTable(String[] IsiTablenya){
        String hasilnya="";
        int deteksiIndexAkhir=IsiTablenya.length-1;
        try {
            for (int i = 0; 1< IsiTablenya.length; i++){
                
                if (i==deteksiIndexAkhir){
                    hasilnya=hasilnya+"'"+IsiTablenya[i]+"'";
                }else{
                        hasilnya=hasilnya+"'"+IsiTablenya[i]+"',";
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "("+hasilnya+")";
    }
    
    public void SimpanDinamis(String NamaTable, String[] Fieldnya, String[] Isinya)
    {
        try {
            String SQLSave ="INSERT INTO " + NamaTable + " " + getFieldTable(Fieldnya)+
                    " VALUES " + getIsiTable(Isinya);
            Statement perintah = getKoneksiDB().createStatement();
            perintah.executeUpdate(SQLSave);
            perintah.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public String getFieldValueEdit(String[] Field, String[] value){
         String hasil = "";
        int deteksi = Field.length-1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i==deteksi){
                    hasil = hasil +Field[i]+" ='"+value[i]+"'";
                }else{
                   hasil = hasil +Field[i]+" ='"+value[i]+"',";  
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }
    
    public void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary,String[] Field, String[] Value){
        
        try {
           String SQLUbah = "UPDATE "+NamaTabel+" SET "+getFieldValueEdit(Field, Value)+" WHERE "+PrimaryKey+"='"+IsiPrimary+"'";
           Statement perintah = getKoneksiDB().createStatement();
           perintah.executeUpdate(SQLUbah);
           perintah.close();
           JOptionPane.showMessageDialog(null,"Data Berhasil Diubah");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
    }
    
     public void HapusDinamis(String NamaTabel, String PK, String isi){
        try {
            String SQL="DELETE FROM "+NamaTabel+" WHERE "+PK+"='"+isi+"'";
            Statement perintah = getKoneksiDB().createStatement();
            perintah.executeUpdate(SQL);
            perintah.close();
            JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
     
    public void settingJudulTabel(JTable Tabelnya, String[] JudulKolom){
        try {
            Modelnya = new DefaultTableModel();
            Tabelnya.setModel(Modelnya);
            for (int i = 0; i < JudulKolom.length; i++) {
                Modelnya.addColumn(JudulKolom[i]);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void settingLebarKolom(JTable Tabelnya,int[] Kolom){
        try {
          Tabelnya.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
          for (int i = 0; i < Kolom.length; i++) {
           Kolomnya =Tabelnya.getColumnModel().getColumn(i);
          Kolomnya.setPreferredWidth(Kolom[i]);   
          }
          
        
        } catch (Exception e) {
          System.out.println(e.toString());
        }
    }

    public Object[][] isiTabel(String SQL, int jumlah){
        Object[][] data =null;
        try {
         Statement perintah = getKoneksiDB().createStatement(
         ResultSet.TYPE_SCROLL_INSENSITIVE,
         ResultSet.CONCUR_READ_ONLY
         );
         ResultSet dataset = perintah.executeQuery(SQL);
         dataset.last();
         int baris = dataset.getRow();
         dataset.beforeFirst();
         int j =0;
         
         data = new Object[baris][jumlah];
         
         while (dataset.next()){
             for (int i = 0; i < jumlah; i++) {
                 data[j][i]=dataset.getString(i+1);
             }
             j++;
            }
         
        } catch (Exception e) {
        }
        return data;
    }

    public void tampilTabel(JTable Tabelnya, String SQL, String[] Judul){
      try {
        Tabelnya.setModel(new DefaultTableModel(isiTabel(SQL,Judul.length), Judul));
      } catch (Exception e) {
          System.out.println(e.toString());
      }
    }
    
}
