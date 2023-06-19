String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";
String name = "John";
int age = 30;
int id = 1;
boolean success = executeUpdate(sql, name, age, id);



String sql = "INSERT INTO users(name, age) VALUES(?, ?)";
String name = "John";
int age = 30;
boolean success = executeUpdate(sql, name, age);



String sql = "DELETE FROM users WHERE id = ?";
int id = 1;
boolean success = executeUpdate(sql, id);



String sql = "ALTER TABLE users ADD COLUMN email VARCHAR(100)";
boolean success = executeUpdate(sql);



String sql = "DROP TABLE users";
boolean success = executeUpdate(sql);