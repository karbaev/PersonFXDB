package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import objects.Person;
import utils.DialogManager;

/**
 * Created by Danila on 15.12.2016.
 */
public class EditDialogController {
    /**
     * Текущий объект изменения
     */
    private Person person;
    public boolean cancelled=true;

    public Person getPerson() {
        return person;
    }

    @FXML
    TextField txtID;
    @FXML
    TextField txtCat;
    @FXML
    TextField txtMak;
    @FXML
    TextField txtMod;
    @FXML
    TextField txtQuantity;
    @FXML
    TextField txtPrice;


    /**
     * Принимает объект person в текущую форму и заполняет текстовые поля формы соотвествующими значениями
     * @param person текущий объект
     */
    public void setPerson(Person person) {
        this.person = person;
        this.cancelled = true;
        txtID.setText(Integer.toString(person.getId()));
        txtCat.setText(person.getCategory());
        txtMak.setText(person.getMaker());
        txtMod.setText(person.getModel());
        txtQuantity.setText(Integer.toString(person.getQuantity()));
        txtPrice.setText(Integer.toString(person.getPrice()));
    }

    /**
     * Отмена (без сохранения изменений), закрытие окна и переход на главную форму
     * @param actionEvent событие
     */
    public void actionClose(ActionEvent actionEvent) { //on Cancel button
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.hide(); //закрытие окна без знания того, кто его открыл
    }

    /**
     * Сохранение, закрытие окна и переход на главную форму
     * @param actionEvent событие
     */
    public void actionSave(ActionEvent actionEvent) { //on OK button
        if (!checkValues()){ //отмена если имя не заполнено
            return;
        }
        person.setId(txtID.getText());
        person.setCategory(txtCat.getText());
        person.setMaker(txtMak.getText());
        person.setModel(txtMod.getText());
        person.setQuantity(txtQuantity.getText());
        person.setPrice(txtPrice.getText());
        //System.out.println(person);
        this.cancelled = false;
        actionClose(actionEvent);
    }

    /**
     * Проверка, чтобы были заполнены обязятельные поля (name)
     * @return true - если заполнено; false - если не заполнено.
     */
    private boolean checkValues() {
        if (txtCat.getText().trim().length()==0){
            DialogManager.showInfoDialog("error", "Заполните поле Name");
            return false;
        }
        return true;
    }

    /**
     * Пустой метод-заглушка - используется, пока не прописаны другие действия
     * @param actionEvent событие
     */
    public void onAction(ActionEvent actionEvent) {
    }
}
