String sql = "CREATE TABLE customers (id INT PRIMARY KEY, name VARCHAR(50), email VARCHAR(50))";
boolean created = executeCreate(sql);
if (created) {
    System.out.println("Table created successfully!");
} else {
    System.out.println("Failed to create table.");
}



String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
boolean created = executeCreate(sql, "johndoe", "mypassword", "johndoe@example.com");
if (created) {
    System.out.println("User created successfully!");
} else {
    System.out.println("Failed to create user.");
}