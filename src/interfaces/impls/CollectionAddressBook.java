package interfaces.impls;

import interfaces.Addressbook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DBPersonModel;
import objects.Person;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Danila on 15.12.2016.
 */
public class CollectionAddressBook implements Addressbook {

    ObservableList<Person> personList = FXCollections.observableArrayList();

     public ObservableList<Person> getPersonList() {
        return personList;
    }

    public void fillTestData() throws Exception {
        personList = FXCollections.observableArrayList((new DBPersonModel()).selectPerson());
        /*        personList.add(new Person(1, "aaa", 22, 55));
        personList.add(new Person(2, "bbb", 22, 55));
        personList.add(new Person(3, "ccc", 22, 55));
        personList.add(new Person(4, "ddd", 22, 55));
        */
    }

    public void print(){
        for (Person p: personList) {
            System.out.println(p);
        }
    }

    @Override
    public void add(Person person) {
        try {
            new DBPersonModel().insertPerson(person);
        } catch (Exception e) {
            e.printStackTrace();
        }

        personList.add(person);
    }

    @Override
    public void update(Person person) {
        try {
            new DBPersonModel().updatePerson(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Person person) {
        try {
            new DBPersonModel().removePerson(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
        personList.remove(person);
    }
}
