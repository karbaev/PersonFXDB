package model;

/**
 * Created by Danila on 15.12.2016.
 */

import objects.Person;

import java.sql.*;
import java.util.ArrayList;

/*
 * [Запуск JavaDB]
 * 1. В папке, где расположен JDK /db/bin запустить сервер БД через консоль (открыть окно комманд)
 * startNetworkServer -noSecurityManager
 * в случае успеха будет выведено сообщение:
 * >Сетевой сервер Apache Derby Network Server - 10.11.1.2 - (1629631) запущен и готов принимать соединения на порту 1527 на {3}
 *
 * [Подключение Data Source]
 * 2. В проекте необходимо создать новый ресурс JDBC.
 * В меню View выставить галочку Tool Windows, выбрать справа Database,
 * нажать кнопку "Data Source Properties"
 * Добавить Derby Remote.
 * В разделе Driver - выбрать Derby Remote.
 * В новой вкладке - если драйверов нет в блоке Driver Files, - нажать Download.
 * Перейти обратно в Project Data Sources - Derby Remote.
 * Переключить URL на default
 * и сконфигурировать следующим образом
 * (создать папку для размещения базы D:\Java, папку TestDB создавать не нужно)
 *  host:localhost
 *  database:D:\Java\TestDB;create=true
 *  url=jdbc:derby://localhost:1527/D:/Java/TestDB;create=true
 * Нажать кнопку "Test Connection" - должна появиться зеленая надпись "Successful".
 * Если на данном этапе выдает Failed, попробуйте перезапустить сервер.
 *  При нажатии OK, всплывет окно для задания мастер-пароля.
 * Если забыли пароль, можно здесь нажать кнопку Reset.
 *
 * [Создание таблиц]
 * 3. По умолчанию таблицы создаются в схеме APP базы данных
 * ПКМ на базе New - Table
 * Добавить таблицу person.
 * В таблицу добавить поля
 * 1) id - int, not null, primary key
 * в поле SQL Script продолжить:
 id INT NOT NULL PRIMARY KEY
 GENERATED ALWAYS AS IDENTITY
 (START WITH 1, INCREMENT BY 1)
 *  2) name - varchar(20)
 *  3) age - int
 *  4) weight - real
 *
 * 4. Заполнить таблицу тестовыми данными, два раза кликнув на нее.
 * Для заполнения поля id нехобходимо кликнуть по нему 1 раз, чтобы появилась надпись <generated>
 *
 * 5. Протестировать в консоли БД запрос SELECT * FROM APP.Person;
 *
 * 6. Добавить библиотеку работы с JavaDB:
 * File - Project Structure - Libraries
 * New Project Library - Java
 * добавить из папки JDK \db\lib\derbyclient.jar
 * Проверить, что derbyclient появилась в External Libraries,
 * в противном случае - повторить процедуру 6, указав путь вручную
 */


public class DBPersonModel {

    private ArrayList<Person> personList = new ArrayList<>();

    private String dbURL = "jdbc:derby://localhost:1527/C:\\java\\TestDB";
    private String user = "APP"; //можно указать имя схемы
    private String password = "a"; //любой непустой пароль

    private Connection connection; //поле подключения к БД
    private Statement statement = null; //поле запроса
    private ResultSet resultSet = null; //поле таблицы результата выдачи

    public static final String INSERT_PERSON = "INSERT INTO person (name, age, weight) VALUES (?, ?, ?)";
    public static final String UPDATE_PERSON = "UPDATE person SET name=?, age=?, weight=? WHERE id=?";


    protected Connection connectToDB() throws ClassNotFoundException {
        try {
            System.out.println("Connecting to a database...");
            connection = DriverManager.getConnection(dbURL, user, password);
            System.out.println("Connected database successfully...");

            return connection;
        } catch (SQLException se) {
            System.out.println("SQL exception - connectToDB(): " + se.getMessage());
            se.printStackTrace(); //Вывод стека ошибок JDBC
            return null;
        }
    }

    public DBPersonModel() throws Exception {
        connectToDB();
    }

     public int insertPerson(Person person) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PERSON);
            preparedStatement.setString(1, person.getCategory()); //name
            preparedStatement.setString(2, person.getMaker()); //age
            preparedStatement.setString(3, person.getModel()); //weight



            preparedStatement.executeUpdate();

            return 1;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch (Exception e) {
            throw e;
        } finally{
            close();
        }
        return -1;
    }

    public int updatePerson(Person person) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PERSON);
            preparedStatement.setString(1, person.getCategory()); //name
            preparedStatement.setString(2, person.getMaker()); //age
            preparedStatement.setString(3, person.getModel()); //weight
            preparedStatement.setInt(4,person.getId());
            preparedStatement.executeUpdate();

            return 1;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch (Exception e) {
            throw e;
        } finally{
            close();
        }
        return -1;
    }

    public int removePerson(Person person) throws SQLException {
        try {
            PreparedStatement preparedStatement =connection.prepareStatement("DELETE FROM person WHERE id="+person.getId());
            preparedStatement.executeUpdate();
            return 1;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch (Exception e) {
            throw e;
        } finally{
            close();
        }
        return -1;
    }


    public ArrayList<Person> selectPerson() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from PERSON");

            resultSet = statement.executeQuery(); //выполнить запрос
            while (resultSet.next()) { //вывести все строки таблицы результатов в цикле
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String maker = resultSet.getString("maker");
                String model = resultSet.getString("model");
                int quantity = resultSet.getInt("quantity");
                int price = resultSet.getInt("price");

                personList.add(new Person(id,category,maker, model,quantity,price));
/*                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Weight: " + weight);
                System.out.println("---");
                */
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch (Exception e) {
            throw e;
        } finally{
            close();
            return personList;

        }
    }


    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {
        DBPersonModel dd=new DBPersonModel();
        //dd.insertPerson();
        dd.selectPerson();

        dd.close();
    }

}
