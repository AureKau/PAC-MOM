package DataBase.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.System;
import java.text.Normalizer;


public class GestionDB {

    private final String pathRes_db= "src/main/java/DataBase/res_db";
    private final String dbName = pathRes_db + "/db_file/DB_PacMom.db";
    private final String pathFileScripSqlite = pathRes_db +"/db_file/dbPacMom.sql";
    static String pathFileLog =  "src/main/java/DataBase/res_db/libs/logback.xml";


    static {
        System.setProperty("logback.configurationFile", pathFileLog);
    }

    private static final Logger logger = LoggerFactory.getLogger(GestionDB.class);
    private Connection connection = null;

    static GestionDB instance;

    private GestionDB() {
        try {
            createDataBase();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            logger.error("probleme lors de la création de la db");
            throw new RuntimeException(e);
        }
    }

    public static GestionDB getInstance(){
        if (instance == null){
            instance = new GestionDB();
        }

        return instance;
    }

    private ResultSet executeSqlCommand(String sqlCommand) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        logger.info("commande effectuer(executeSqlCommand) : " + sqlCommand);
        ResultSet set =  conn.createStatement().executeQuery(sqlCommand);

        return set;
    }


    public void executeSqlInsertOrUpdate(String sqlCommand) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        logger.info("commande effectuer(executeSqlInsertOrUpdate) : " + sqlCommand);
        conn.createStatement().executeUpdate(sqlCommand);
        conn.close();
    }

    public ResultSet executeSqlSelect(String champs, String table) throws SQLException, ClassNotFoundException {
        return executeSqlCommand("SELECT" + champs + " FROM " + table + ";");
    }

    public ResultSet executeSqlSelect(String champs, String table, String whereCondition) throws SQLException, ClassNotFoundException {
        return executeSqlCommand("SELECT" + champs + " FROM " + table + "WHERE " + whereCondition + ";");
    }

    public ResultSet executeSqlSelect(String sqlCommand) throws SQLException, ClassNotFoundException {
        return executeSqlCommand(sqlCommand);
    }

    public ResultSet executeSelectAllTable(int idNote) throws SQLException, ClassNotFoundException {
        return executeSqlCommand("SELECT * FROM note INNER JOIN Livre USING(id_livre) INNER JOIN auteur USING (id_auteur) WHERE id_note = " + idNote +" ;");
    }

    public ResultSet executeSelectAllTable() throws SQLException, ClassNotFoundException {
        return executeSqlCommand("SELECT * FROM note LEFT JOIN Livre USING(id_livre) LEFT JOIN auteur USING (id_auteur);");
    }


    public void saveNoteWithAll(String nomAuteur, String titreLivre, String titreNote, String texteNote) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Convertir les entrées en minuscules et normaliser les accents
            nomAuteur = normalizeAndLowerCase(nomAuteur);
            titreLivre = normalizeAndLowerCase(titreLivre);
            titreNote = normalizeAndLowerCase(titreNote);

            // Vérifier si l'auteur existe déjà
            String selectAuteurQuery = "SELECT id_Auteur FROM Auteur WHERE LOWER(nom_auteur) = ?";
            stmt = conn.prepareStatement(selectAuteurQuery);
            stmt.setString(1, nomAuteur);
            rs = stmt.executeQuery();

            int idAuteur;
            if (rs.next()) {
                // L'auteur existe déjà, récupérer son ID
                idAuteur = rs.getInt("id_Auteur");
            } else {
                // L'auteur n'existe pas, l'insérer dans la table Auteur
                String insertAuteurQuery = "INSERT INTO Auteur (nom_auteur) VALUES (?)";
                stmt = conn.prepareStatement(insertAuteurQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, nomAuteur);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idAuteur = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID de l'auteur nouvellement inséré.");
                }
            }

            // Vérifier si le livre existe déjà
            String selectBookQuery = "SELECT id_livre FROM Livre WHERE LOWER(titre) = ?";
            stmt = conn.prepareStatement(selectBookQuery);
            stmt.setString(1, titreLivre);
            rs = stmt.executeQuery();

            int idLivre;
            if (rs.next()) {
                // Le livre existe déjà, récupérer son ID
                idLivre = rs.getInt("id_livre");
            } else {
                // Le livre n'existe pas, l'insérer dans la table Livre
                String insertBookQuery = "INSERT INTO Livre (titre, id_Auteur) VALUES (?, ?)";
                stmt = conn.prepareStatement(insertBookQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, titreLivre);
                stmt.setInt(2, idAuteur);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idLivre = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID du livre nouvellement inséré.");
                }
            }

            // Vérifier si la note existe déjà pour ce livre
            String selectNoteQuery = "SELECT id_note FROM Note WHERE LOWER(titre) = ? AND id_livre = ?";
            stmt = conn.prepareStatement(selectNoteQuery);
            stmt.setString(1, titreNote);
            stmt.setInt(2, idLivre);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                // La note n'existe pas pour ce livre, l'insérer dans la table Note
                String insertNoteQuery = "INSERT INTO Note (titre, texte, id_livre) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(insertNoteQuery);
                stmt.setString(1, titreNote);
                stmt.setString(2, texteNote);
                stmt.setInt(3, idLivre);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }



    public String normalizeAndLowerCase(String input) {
        // Supprimer les accents et convertir en minuscules
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }


    public void saveNoteWithBook (String titreLivre, String titreNote, String texteNote) throws SQLException, ClassNotFoundException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Convertir les entrées en minuscules et normaliser les accents
            titreLivre = normalizeAndLowerCase(titreLivre);
            titreNote = normalizeAndLowerCase(titreNote);

            // Vérifier si le livre existe déjà
            String selectBookQuery = "SELECT id_livre FROM Livre WHERE LOWER(titre) = ?";
            stmt = conn.prepareStatement(selectBookQuery);
            stmt.setString(1, titreLivre);
            rs = stmt.executeQuery();

            int idLivre;
            if (rs.next()) {
                // Le livre existe déjà, récupérer son ID
                idLivre = rs.getInt("id_livre");
            } else {
                // Le livre n'existe pas, l'insérer dans la table Livre
                String insertBookQuery = "INSERT INTO Livre (titre, id_Auteur) VALUES (?, ?)";
                stmt = conn.prepareStatement(insertBookQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, titreLivre);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idLivre = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID du livre nouvellement inséré.");
                }
            }

            // Vérifier si la note existe déjà pour ce livre
            String selectNoteQuery = "SELECT id_note FROM Note WHERE LOWER(titre) = ? AND id_livre = ?";
            stmt = conn.prepareStatement(selectNoteQuery);
            stmt.setString(1, titreNote);
            stmt.setInt(2, idLivre);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                // La note n'existe pas pour ce livre, l'insérer dans la table Note
                String insertNoteQuery = "INSERT INTO Note (titre, texte, id_livre) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(insertNoteQuery);
                stmt.setString(1, titreNote);
                stmt.setString(2, texteNote);
                stmt.setInt(3, idLivre);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }

    }

    public void saveNoteOnly(String titreNote, String texteNote) throws SQLException, ClassNotFoundException {
        String insertNoteQuery = "INSERT INTO Note (titre, texte) " +
                "VALUES(\"" + titreNote + "\",\"" + texteNote + "\");";

        String transactionQuery = "BEGIN TRANSACTION; " +
                insertNoteQuery + " " +
                "COMMIT;";

        executeSqlInsertOrUpdate(transactionQuery);
    }


    private void closeExistingConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    logger.info("Connexion fermée avec succès.");
                }
            } catch (SQLException e) {
                logger.error("Erreur lors de la fermeture de la connexion.", e);
            } finally {
                connection = null;
            }
        }
    }


    private Connection getConnection() throws SQLException, ClassNotFoundException {
        closeExistingConnection();
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        return connection;
    }

    private Statement getStatement() throws SQLException, ClassNotFoundException {
        return getConnection().createStatement();
    }


    private void dropDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        conn.createStatement().execute("DROP DATABASE DB_PacMom;");
        logger.info("supréssion de la base de donnée");
        conn.close();
    }

    public boolean databaseExists() {
        File dbFile = new File(dbName);
        return dbFile.exists();
    }



    public void createDataBase() throws ClassNotFoundException, SQLException, IOException {
        if (!databaseExists()){
            Class.forName("org.sqlite.JDBC");
            Connection connection = getConnection();
            String script = readSQLScript(pathFileScripSqlite);
            executeScript(connection, script);
            logger.info("création de la base de données");
            connection.close();
        }

    }


    // Read the SQL Script File
    private String readSQLScript(String filePath) throws IOException {
        StringBuilder script = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
        }
        return script.toString();
    }

    //Execute the SQL Script File
    private void executeScript(Connection connection, String script) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String[] sqlCommands = script.split(";");

            for (String sqlCommand : sqlCommands) {
                if (!sqlCommand.trim().isEmpty()) {
                    statement.addBatch(sqlCommand);
                }
            }
            statement.executeBatch();

        }
    }


    private String recupNameTableAndColumn() throws ClassNotFoundException, SQLException {
        logger.info("récuperation de tous les nom des tables et des colones");
        DatabaseMetaData metaData = getConnection().getMetaData();
        String result = "";

        ResultSet tables = metaData.getTables(null, null, null, new String[] {"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            result = result + "Table : " + tableName + "\n";

            // Récupérer les métadonnées des colonnes de la table actuelle
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                boolean isNullable = (columns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls);
                boolean isAutoIncrement = columns.getBoolean("IS_AUTOINCREMENT");
                boolean isUnique = false; // Par défaut, nous supposons que la colonne n'est pas unique

                // Vérifiez si la colonne a une contrainte unique en vérifiant les index uniques
                ResultSet uniqueIndexes = metaData.getIndexInfo(null, null, tableName, true, false);
                while (uniqueIndexes.next()) {
                    String uniqueColumnName = uniqueIndexes.getString("COLUMN_NAME");
                    if (uniqueColumnName.equals(columnName)) {
                        isUnique = true;
                        break;
                    }
                }
                uniqueIndexes.close();

                // Afficher les informations de la colonne
                result = result + "   Colonne : " + columnName + ", Type : " + columnType +
                        ", Not Null : " + isNullable + ", AutoIncrement : " + isAutoIncrement +
                        ", Unique : " + isUnique + "\n";
            }
            columns.close();
        }
        tables.close();

        return result;

    }

    @Override
    public String toString() {
        try {
            return recupNameTableAndColumn();
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("erreur lors de la récupération du nom des tables et des champs");
            throw new RuntimeException(e);
        }
    }
}

