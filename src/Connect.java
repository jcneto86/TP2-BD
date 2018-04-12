import java.sql.*;
import java.util.ArrayList;


public class Connect {

    // Nom du pilote JDBC et URL de la base de données
    private final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private final String DB_URL = "jdbc:mariadb://localhost:3306/Medicaments"; //changer l'hôte ou la base de données
    //  Identifiants de la base de données
    private final String USER = "root";
    private final String PASS = "";

    private Connection conn;
    private Statement stmt;

    private void ConnectBD() {
        try {

            // Enregister le pilote JDBC
            Class.forName(JDBC_DRIVER);

            // Ouvrir une connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Création de la déclaration;
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connect() {
        ConnectBD();
    }

    public void close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String traiterLigne(String sql) {

        // Retourne le résultat d'une requête SQL sous forme d'une chaîne de caractères

        String out = "";

        ArrayList<String> Lignes = new ArrayList<String>();
        Lignes = this.traiterColonne(sql);
        for (String ligne : Lignes) {
            out += ligne + "\n";
        }
        return out;
    }

    public ArrayList<String> traiterColonne(String sql) {

        ArrayList<String> outs = new ArrayList<String>();

        try {
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                String ligne = "";
                for (int col = 1; col <= colCount; col++) {
                    Object value = rs.getObject(col);
                    if (value != null) {
                        ligne += value.toString();
                        if (col < colCount) {
                            // Pour aligner les colonnes avec des tabulations
                            int blankcount = 24 - value.toString().length();
                            for (int blank = 0; blank < blankcount; blank++) {
                                ligne += " ";
                            }
                        }
                    }
                }
                outs.add(ligne);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outs;
    }

    public String traiterMiseajour(String sql) {
        String out;
        try {
            int result = stmt.executeUpdate(sql);
            out = result + " rangée(s) affectée(s)";
        } catch (SQLException e) {
            out = "Exception: 0 rangée(s) affectée(s)";
            e.printStackTrace();
        }
        return out;
    }


}