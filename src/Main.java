import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printActions();

        while (true) {
            printActions();

        }
    }


    private static Connection connect() {
        String url = "jdbc:sqlite:C:\\Users\\linds\\Videos\\datagrip";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printActions() {
        System.out.println("""
                MENY
                ========
                0. Avsluta
                1. Visa
                2. Ändra
                3. Ta bort
                5. Visa huvudmenyn""");
        while (true) {
            System.out.println("\nVälj (5 för att visa val):");
            String input = scanner.nextLine();
            switch (input) {
                case "0" -> System.exit(0);
                case "1" -> showMenu();
                case "2" -> changeMenu();
                case "3" -> deleteMenu();
                case "5" -> printActions();

            }
        }
    }

    private static void showMenu() {
        System.out.println("""
                MENY
                ========
                0. Avsluta
                1. Visa Artister
                2. Visa genre
                3. Visa antal artister
                4. Sök Artist
                5. Visa val
                6. Visa Huvudmeny""");
        while (true) {
            System.out.println("\nVälj (6 för att visa val):");
            String input = scanner.nextLine();
            switch (input) {
                case "0" -> System.exit(0);
                case "1" -> selectAll();
                case "2" -> selectAllTypes();
                case "3" -> countArtists();
                case "4" -> searchForArtist();
                case "5" -> showMenu();
                case "6" -> printActions();


            }

        }
    }

    private static void changeMenu() {
        System.out.println("""
                MENY
                ========
                0. Avsluta
                1. Lägg till artist
                2. Lägg till genre
                3. Ändra pris
                4. Visa val
                5. Visa Huvudmeny""");
        while (true) {
            System.out.println("\nVälj (4 för att visa val):");
            String input = scanner.nextLine();
            switch (input) {
                case "0" -> System.exit(0);
                case "1" -> insertNewMusic();
                case "2" -> insertNewGenre();
                case "3" -> update();
                case "4" -> changeMenu();
                case "5" -> printActions();


            }

        }
    }

    private static void deleteMenu() {
        System.out.println("""
                MENY
                ========
                0. Avsluta
                1. Ta bort artist
                2. Ta bort genre
                4. Visa val
                5. Visa Huvudmeny""");
        while (true) {
            System.out.println("\nVälj (4 för att visa val):");
            String input = scanner.nextLine();
            switch (input) {
                case "0" -> System.exit(0);
                case "1" -> deleteMusic();
                case "2" -> deleteGenre();
                case "4" -> deleteMenu();
                case "5" -> printActions();


            }

        }
    }

    private static void insertNewGenre() {
        System.out.println("Skriv in ny genre: ");
        String inputGenre = scanner.nextLine();
        insertGenre(inputGenre);

    }

    private static void insertGenre(String genre) {
        String sql = "INSERT INTO type(typeGenre) VALUES(?)";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, genre);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till en ny genre");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // CREATE
    private static void insertNewMusic() {
        System.out.println("Skriv in en Artist: ");
        String inputArtist = scanner.nextLine();
        System.out.printf("Skriv in skivnamnet: ");
        String inputRecord = scanner.nextLine();
        System.out.println("Skriv in vilket år skivan släpptes: ");
        int inputYear = scanner.nextInt();
        System.out.println("Skriv in priset på skivan: ");
        int inputPrice = scanner.nextInt();
        System.out.println("I vilken genre vill du ha den?");
        selectAllTypes();
        int inputType = scanner.nextInt();
        insert(inputArtist, inputRecord, inputYear, inputPrice, inputType);
        scanner.nextLine();


    }

    private static void insert(String artist, String record, int year, int price, int type) {
        String sql = "INSERT INTO music(musicArtist, musicRecord, musicYear, musicPrice, musicType) VALUES(?,?,?,?,?)";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, artist);
            pstmt.setString(2, record);
            pstmt.setInt(3, year);
            pstmt.setInt(4, price);
            pstmt.setInt(5, type);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till en ny artist");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // READ
    private static void selectAll() {
        String sql = "SELECT * FROM music INNER JOIN type on musicType = typeId \n";

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("musicID") + "|" + "\t" +
                        rs.getString("musicArtist") + "|" + "\t" +
                        rs.getString("musicRecord") + "|" + "\t" +
                        rs.getInt("musicYear") + "|" + "\t" +
                        rs.getInt("musicPrice") + ":-" + "|" + "\t" +
                        rs.getString("typeGenre"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void selectAllTypes() {
        String sql = "SELECT * FROM type";

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("typeId") + "\t" +
                        rs.getString("typeGenre"));


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // UPDATE
    private static void update() {
        String sql = "UPDATE music SET musicPrice = ? WHERE musicId = ?";


        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            selectAll();
            System.out.println("Skriv in vilket ID du vill ändra på: ");
            String inputId = scanner.nextLine();
            pstmt.setInt(2, Integer.parseInt(inputId));
            System.out.println("Skriv in ditt nya pris: ");
            String inputPrice = scanner.nextLine();
            pstmt.setString(1, inputPrice);
            pstmt.executeUpdate();
            System.out.println("ID " + inputId + " har uppdateras");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // DELETE
    private static void deleteMusic() {
        selectAll();
        System.out.println("Skriv in id:t på artisten som du vill ska ta bort: ");
        int inputId = scanner.nextInt();
        delete(inputId);
        scanner.nextLine();
    }

    private static void delete(int id) {
        String sql = "DELETE FROM music WHERE musicId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            selectAll();
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Du har tagit bort en artist.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteGenre() {
        selectAllTypes();
        System.out.println("Skriv in id:t på genren som du vill ska ta bort: ");
        int inputTypeId = scanner.nextInt();
        deleteSQLgenre(inputTypeId);
        scanner.nextLine();
    }

    private static void deleteSQLgenre(int id) {
        String sql = "DELETE FROM type WHERE typeId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Du har tagit bort en genre.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void countArtists() {
        String sql = "SELECT COUNT(*) FROM music";

        try (Connection conn = connect();
             Statement query = conn.createStatement()) {
            ResultSet rs = query.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Antal artister: " + rs.getInt("COUNT(*)"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchForArtist() {
        String sql = "SELECT * FROM music WHERE musicArtist = ?";

        try (Connection conn = connect();
             PreparedStatement query = conn.prepareStatement(sql)) {
            System.out.println("Sök artist: ");
            String artistSearch = scanner.nextLine();
            query.setString(1, artistSearch);
            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                System.out.println("musicID: " + rs.getInt("musicID") + "\t" +
                        "Artist: " + rs.getString("musicArtist") + "\t" +
                        "Pris: " + rs.getInt("musicPrice")); //+ "\t" +
                        //"Genre: " + rs.getString("typeGenre") + "\n");


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}