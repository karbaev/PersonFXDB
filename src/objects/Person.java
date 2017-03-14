package objects;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Danila on 15.12.2016.
 */
public class Person {
    private SimpleIntegerProperty id=new SimpleIntegerProperty(0);
    private SimpleStringProperty category=new SimpleStringProperty("");
    private SimpleStringProperty maker=new SimpleStringProperty("");
    private SimpleStringProperty model=new SimpleStringProperty("");
    private SimpleIntegerProperty quantity=new SimpleIntegerProperty(0);
    private SimpleIntegerProperty price=new SimpleIntegerProperty(0);

    public Person() {

    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public SimpleStringProperty categoryProperty() {
        return category;
    }
    public SimpleStringProperty makerProperty() {
        return maker;
    }
    public SimpleStringProperty modelProperty() {
        return model;
    }
    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
    public SimpleIntegerProperty priceProperty() {
        return price;
    }




    public void setId(String id) {
        this.id.set(Integer.parseInt(id));
    }
    public void setCategory(String category) {
        this.category.set(category);
    }
    public void setMaker(String maker) {
        this.maker.set(maker);
    }
    public void setModel(String model) {
        this.maker.set(model);
    }

    public void setQuantity(String quantity) {
        this.quantity.set(Integer.parseInt(quantity));
    }
    public void setPrice(String price) {
        this.price.set(Integer.parseInt(price));
    }

    public int getId() {
        return id.get();
    }
    public String getCategory() {
        return category.get();
    }
    public String getMaker() {
        return maker.get();
    }
    public String getModel() {
        return model.get();
    }public int getQuantity() {
        return quantity.get();
    }
    public int getPrice() {
        return price.get();
    }



    public Person(int id, String category, String maker, String model, int quantity, int price) {
        this.id.set(id);
        this.category.set(category);
        this.maker.set(maker);
        this.model.set(model);
        this.quantity.set(quantity);
        this.price.set(price);
    }



    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", maker=" + maker +
                ", model=" + model +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }



}
