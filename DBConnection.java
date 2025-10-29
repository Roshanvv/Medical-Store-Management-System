Class.forName("com.mysql.cj.jdbc.Driver");
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/medicalstore?useSSL=false&serverTimezone=UTC",
    "root",
    "Mysql@9090"
);
