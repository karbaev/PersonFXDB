package interfaces;

import objects.Person;

/**
 * Created by Danila on 15.12.2016.
 */
public interface Addressbook {
    public void add(Person person);
    public void update(Person person);
    public void delete(Person person);
}
