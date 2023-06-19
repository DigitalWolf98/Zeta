String sql = "SELECT name FROM products WHERE price > ? AND category = ?";
ResultSet result = database.executeRead(sql, 50.0, "Electronics");
List<String> products = new ArrayList<>();
while (result.next()) {
    String name = result.getString("name");
    products.add(name);
}
System.out.println("Products in the Electronics category that cost more than $50: " + products);



String sql = "SELECT * FROM users";
ResultSet resultSet = executeRead(sql);
while (resultSet.next()) {
    int id = resultSet.getInt("id");
    String name = resultSet.getString("name");
    String email = resultSet.getString("email");
    System.out.println("User id: " + id + ", name: " + name + ", email: " + email);
}