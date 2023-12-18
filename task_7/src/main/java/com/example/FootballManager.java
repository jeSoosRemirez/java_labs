package task_7.src.main.java.com.example;

import java.sql.*;
import java.util.Scanner;

public class FootballManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/football_db";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            initializeDatabase(connection);
            
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("1. Додати команду");
                System.out.println("2. Додати гравця");
                System.out.println("3. Вивести всі команди та гравців");
                System.out.println("4. Вийти");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addTeam(connection);
                        break;
                    case 2:
                        addPlayer(connection);
                        break;
                    case 3:
                        displayTeamsAndPlayers(connection);
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Неправильний вибір. Спробуйте знову.");
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTeamsTable = "CREATE TABLE IF NOT EXISTS teams (id SERIAL PRIMARY KEY, name VARCHAR(255))";
            statement.execute(createTeamsTable);

            String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (id SERIAL PRIMARY KEY, name VARCHAR(255), team_id INT)";
            statement.execute(createPlayersTable);
        }
    }

    private static void addTeam(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть ім'я команди:");
        String teamName = scanner.nextLine();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO teams (name) VALUES (?)")) {
            preparedStatement.setString(1, teamName);
            preparedStatement.executeUpdate();
        }
        System.out.println("Команда додана успішно.");
    }

    private static void addPlayer(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть ім'я гравця:");
        String playerName = scanner.nextLine();

        displayTeams(connection);

        System.out.println("Виберіть ID команди для гравця:");
        int teamId = scanner.nextInt();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (name, team_id) VALUES (?, ?)")) {
            preparedStatement.setString(1, playerName);
            preparedStatement.setInt(2, teamId);
            preparedStatement.executeUpdate();
        }
        System.out.println("Гравець доданий успішно.");
    }

    private static void displayTeams(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM teams");
            System.out.println("Команди:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ". " + resultSet.getString("name"));
            }
        }
    }

    private static void displayTeamsAndPlayers(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM teams");
            while (resultSet.next()) {
                int teamId = resultSet.getInt("id");
                String teamName = resultSet.getString("name");
                System.out.println("Команда: " + teamName);

                ResultSet playersResultSet = statement.executeQuery("SELECT * FROM players WHERE team_id = " + teamId);
                while (playersResultSet.next()) {
                    System.out.println("  Гравець: " + playersResultSet.getString("name"));
                }
                System.out.println();
            }
        }
    }
}
