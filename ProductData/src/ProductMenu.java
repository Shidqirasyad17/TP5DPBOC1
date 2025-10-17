import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        ProductMenu menu = new ProductMenu();

        menu.setSize(700 , 600);

        menu.setLocationRelativeTo(null);

        menu.setContentPane(menu.mainPanel);

        menu.getContentPane().setBackground(Color.WHITE);

        menu.setVisible(true);

        menu.setDefaultCloseOperation(JFrame. EXIT_ON_CLOSE);

    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua produk
    private Database database;
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JSlider RatingSlider;
    private JLabel RatingLabel;

    // constructor
    public ProductMenu() {

        // buat objek database
        database = new Database();

        // isi tabel produk
        productTable.setModel(setTable());
        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        // atur isi combo box
        String[] kategoriData = {"Pilih kategori", "Elektronik", "Makanan", "Minuman", "Pakaian", "Alat Tulis"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));
        // sembunyikan button delete

        //untuk mengecek perubahan pada rating
        RatingSlider.addChangeListener(e ->{
            //ambil value rating saat digeser
            int value = RatingSlider.getValue();
            //untuk menampilkan rating di label menggunakan bintang
            RatingLabel.setText("Rating: " + "★".repeat(value));
        });
        deleteButton.setVisible(false);
        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(selectedIndex == -1){
                insertData();
            }else {
                updateData();
            }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: tambahkan konfirmasi sebelum menghapus data
                int confirm =  JOptionPane.showConfirmDialog(
                        null, "Apakah anda yakin menghapus data ini?",
                            "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION){
                deleteData();
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = productTable.getSelectedRow();
                // simpan value textfield dan combo box
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curRating = productTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box dan slider
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(curHarga);
                kategoriComboBox.setSelectedItem(curKategori);
                RatingSlider.setValue(Integer.parseInt(curRating));

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("update");
                // tampilkan button delete
                deleteButton.setVisible(true);

            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = {"No", "ID Produk", "Nama", "Harga", "Kategori", "Rating"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try{
            ResultSet resultSet = database.selectQuery("SELECT * FROM produk");

            //isi tabel dengan hasil query
            int i = 1;
            while (resultSet.next()){
                Object[] row = new Object[6];
                row[0] = i;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getDouble("harga");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getInt("rating");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tmp; // return juga harus diganti
    }

    public void insertData() {
        try {
        // ambil value dari textfield dan combobox
        String id = idField.getText();
        String nama = namaField.getText();
        double harga = Double.parseDouble(hargaField.getText());
        String kategori = kategoriComboBox.getSelectedItem().toString();
        int rating = RatingSlider.getValue();

        //untuk input kosong
            if (id.isEmpty() || nama.isEmpty() || harga == 0 || kategori.equals("Pilih kategori") || rating == 0) {
                JOptionPane.showMessageDialog(
                        null, "semua kolom harus diisi!", "Error" ,JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            //pengecekan apakah id sudah ada di data
            ResultSet rs = database.selectQuery("SELECT * FROM produk WHERE id = '" + id + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "ID sudah dipakai!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        // menambahkan data
            String sqlQuery = "INSERT INTO produk VALUES ('" + id + "', '" + nama + "', " + harga + ", '" + kategori + "'," + rating +")";
            database.insertUpdateDeleteQuery(sqlQuery);

        // update tabel
        productTable.setModel(setTable());
        // bersihkan form
        clearForm();
        // feedback
         System.out.println("Insert berhasil");
         JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

        }catch (NumberFormatException ex){
            //jika input harga bukan angka
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "error", JOptionPane.ERROR_MESSAGE);
        }catch (SQLException e){
            //jika ada error saat akses database
            JOptionPane.showMessageDialog(null,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = RatingSlider.getValue();

            // untuk input kosong
            if (id.isEmpty() || nama.isEmpty() || harga == 0 || kategori.equals("Pilih kategori") || rating == 0) {
                JOptionPane.showMessageDialog(
                        null, "semua kolom harus diisi!", "Error" ,JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // update produk
            String sqlQuery = "UPDATE produk SET nama='" + nama + "', harga=" + harga + ", kategori='" + kategori + "', rating=" + rating + " WHERE id='" + id + "'";
            database.insertUpdateDeleteQuery(sqlQuery);


            // update tabel
            productTable.setModel(setTable());
            // bersihkan form
            clearForm();
            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "data berhasil dibah");
        }catch (NumberFormatException ex){
            //jika harga bukan angka
            JOptionPane.showMessageDialog(null, "harga harus berupa angka", " error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        String id = idField.getText();
        if (id.isEmpty()){
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //menghapus data
        String sqlQuery = "DELETE FROM produk WHERE id='" + id + "'";
        database.insertUpdateDeleteQuery(sqlQuery);

        // update tabel
        productTable.setModel(setTable());
        // bersihkan form
        clearForm();
        // feedback
        System.out.println("delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        RatingSlider.setValue(0);
        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }


}