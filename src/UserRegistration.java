import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserRegistration {
    public static String DB_URL = "jdbc:mysql://localhost:3306/db_demo";
    public static String USER_NAME = "root";
    public static String PASSWORD = "";

    public static void main(String[] args) {
        String str = "Nguyen Van A, 2005-05-05, Male, 1111, hehe@gmail.com, hehe, 123456"; 
        String str2 = "Thach, 2005/07/03, Male, 20235556, thach@gmail,com, thach, 123456";
        System.out.println(registerData(str));

        // print out all the data in the database 
        // Connection conn = null;
        // try {
        //     // Tải driver MySQL
        //     Class.forName("com.mysql.cj.jdbc.Driver");
            
        //     // Kết nối đến cơ sở dữ liệu
        //     conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        //     System.out.println("Connect successfully!");

        //     // Truy vấn dữ liệu
        //     String sql = "SELECT * FROM registration";
        //     PreparedStatement pr = conn.prepareStatement(sql);
        //     ResultSet r1 = pr.executeQuery();
            
        //     // Hiển thị dữ liệu
        //     while (r1.next()) {
        //         System.out.print(r1.getString("FullName") + " - ");
        //         System.out.print(r1.getDate("DateOfBirth") + " - ");
        //         System.out.print(r1.getString("Gender") + " - ");
        //         System.out.print(r1.getString("CitizenID") + " - ");
        //         System.out.print(r1.getString("Email") + " - ");
        //         System.out.print(r1.getString("UserName") + " - ");
        //         System.out.print(r1.getString("Password") + " - ");
        //     }

        // } catch (ClassNotFoundException e) {
        //     System.out.println("MySQL Driver not found!");
        //     System.err.println("MySQL Driver not found!");
        // } catch (SQLException e) {
        //     System.err.println("SQL Error!");
        // } finally {
        //     // Đóng kết nối
        //     if (conn != null) {
        //         try {
        //             conn.close();
        //             System.out.println("Connection closed.");
        //         } catch (SQLException e) {
        //             System.err.println("SQL Error!");
        //         }
        //     }
        // }
    }

    public static String registerData (String str) {
        List<String> s = Arrays.asList(str.split(", "));
        String fullName = s.get(0);
        String dob = s.get(1);
        String gender = s.get(2);
        String citizenID = s.get(3);
        String email = s.get(4);
        String userName = s.get(5);
        String password = s.get(6);

        Connection conn = null;
        PreparedStatement pr = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

            // Insert data
            String sql = "INSERT INTO registration (FullName, DateOfBirth, Gender, CitizenID, Email, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pr = conn.prepareStatement(sql);

            pr.setString(1, fullName);
            pr.setDate(2, java.sql.Date.valueOf(dob));
            pr.setString(3, gender);
            pr.setString(4, citizenID);
            pr.setString(5, email);
            pr.setString(6, userName);
            pr.setString(7, password);

            int rowsInserted = pr.executeUpdate();
            if (rowsInserted > 0) {
                return "Data inserted successfully!";
            } else {
                return "Failed to insert data.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        } finally {
            // Đóng kết nối
            try {
                if (pr != null) pr.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String validPerson(String str) {
        List<String> s = Arrays.asList(str.split(", "));
        String dob = s.get(1);
        String gender = s.get(2);
        String citizenID = s.get(3);
        String email = s.get(4);

        // Check DoB format
        if (!dob.matches("\\d{4}/\\d{2}/\\d{2}")) {
            return "false: Invalid DoB format. Use yyyy/mm/dd";
        }

        // Check Gender Format
        if (!(gender.equals("Male") || gender.equals("Female"))) {
            return "false: Invalid Gender. Use 'Male' or 'Female'";
        }

        // Check for duplicate CitizenID or Email
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT CitizenID, Email FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                 ResultSet r1 = pr.executeQuery()) {

                while (r1.next()) {
                    if (r1.getString("CitizenID").equals(citizenID)) {
                        return "false. Duplicate CitizenID found.";
                    }
                    if (r1.getString("Email").equals(email)) {
                        return "false. Duplicate Email found.";
                    }
                }
                // insert data
                System.out.println(registerData(str));
            }
            } catch (SQLException e) {
                return "true";
        }
        return "true";
    }
    
    public static Boolean validAccount(String str) {
        List<String> s = Arrays.asList(str.split(", "));
        String Username = s.get(0);
        String Password = s.get(1);

        // Check for username is in the database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT username, password FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                 ResultSet r1 = pr.executeQuery()) {

                while (r1.next()) {
                    if (r1.getString("UserName").equals(Username)) {
                        return false;
                    }
                    if (r1.getString("Password").equals(Password)) {
                        return false;
                    }
                }
                // insert data
                System.out.println(registerData(str));
            }
        } catch (SQLException e) {
            return true;
        }
        return true;
    }

    public static Boolean validQA(String QA) {
        List<String> s = Arrays.asList(QA.split(", "));
        List<String> questions = s.subList(0, 3);
        List<String> answers = s.subList(3,6);

        // Check for username is in the database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT username FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                ResultSet r1 = pr.executeQuery()) {
                    while (r1.next()) {
                        for (int i=0; i<3; i++) {
                            if (r1.getString("QA_Questions").equals(questions.get(i))) {
                                return false;
                            }
                            if (r1.getString("QA_Answers").equals(answers.get(i))) {
                                return false;
                            }
                        }   
                    }
                }
        } catch (SQLException e) {
            return true;
        }
        return true;
    }

    public static Boolean loginSuccess(String str) {
        List<String> s = Arrays.asList(str.split(", "));
        String Username = s.get(0);
        String Password = s.get(1);

        // Check for username is in the database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT username, password FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                 ResultSet r1 = pr.executeQuery()) {

                while (r1.next()) {
                    if (r1.getString("UserName").equals(Username)) {
                        return false;
                    }
                    if (r1.getString("Password").equals(Password)) {
                        return false;
                    }
                }
        }
        } catch (SQLException e) {
            return true;
        }
        return true;
    }

    public String checkPerson(String str) {
        List<String> s = Arrays.asList(str.split(", "));
        String ID = s.get(3);

        // Check ID if it is UserName or CitizenID or Email
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT username, citizenID, email FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                 ResultSet r1 = pr.executeQuery()) {

                while (r1.next()) {
                    if (r1.getString("CitizenID").equals(ID)) {
                        return r1.getString("username");
                    } 
                    if (r1.getString("Email").equals(ID)) {
                        return r1.getString("username");
                    }
                    if (r1.getString("username").equals(ID)){
                        return r1.getString("username");
                    }
                }
        }
        } catch (SQLException e) {
            return "false";
        }
        return "false";
    }

    public String getQuestion(String Username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT username FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                 ResultSet r1 = pr.executeQuery()) {

                while (r1.next()) {
                    if (r1.getString("UserName").equals(Username)) {
                        return r1.getString("QA_Questions");
                    }
                }
        }
        } catch (SQLException e) {
            return "false";
        }
        return "false";
    }

    public String getAnswer(String Username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "SELECT username FROM registration";
            try (PreparedStatement pr = conn.prepareStatement(sql);
                 ResultSet r1 = pr.executeQuery()) {

                while (r1.next()) {
                    if (r1.getString("UserName").equals(Username)) {
                        return r1.getString("QA_Answers");
                    }
                }
        }
        } catch (SQLException e) {
            return "false";
        }
        return "false";
    }

    public static void resetPassword(String Username, String Password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
            String sql = "UPDATE registration SET Password = ? WHERE UserName = ?";

            try (PreparedStatement pr = conn.prepareStatement(sql)) {
                pr.setString(1, Password);
                pr.setString(2, Username);
                int rowsUpdated = pr.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Record updated successfully!");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error!");
        }
    } 
}
