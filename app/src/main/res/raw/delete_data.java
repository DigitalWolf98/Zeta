String sql = "DROP TABLE customers";
boolean success = executeDelete(sql);
if (success) {
    System.out.println("Таблица customers успешно удалена");
} else {
    System.out.println("Ошибка при удалении таблицы customers");
}



String sql = "DELETE FROM orders WHERE order_date < ?";
Date date = new Date();
boolean success = executeDelete(sql, date);
if (success) {
    System.out.println("Удаляны все записи, у которых дата заказа меньше текущей даты");
} else {
    System.out.println("Ошибка при удалении заказов");
}