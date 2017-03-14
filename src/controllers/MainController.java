package controllers;

import interfaces.impls.CollectionAddressBook;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Person;
import utils.DialogManager;

import java.io.IOException;
import java.sql.SQLException;


public class MainController {
    CollectionAddressBook addressBook = new CollectionAddressBook();
    private ObservableList<Person> backupList;

    @FXML
    TableView tableAddressBook;
    @FXML
    TextField txtSearch;
    @FXML
    TableColumn<Person, Integer> columnID;
    @FXML
    TableColumn<Person, String> columnCat;
    @FXML
    TableColumn<Person, Integer> columnMak;
    @FXML
    TableColumn<Person, Float> columnMod;
    @FXML
    TableColumn<Person, Float> columnQuantity;
    @FXML
    TableColumn<Person, Float> columnPrice;
    @FXML
    Label labelStatus;

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditDialogController editDialogController;
    private Stage editDialogStage;
    private Stage mainStage;

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }

    @FXML
    private void initialize() throws Exception {
        initTableCells();
        initListeners();
        fillData();
        initLoader();
    }

    private void initTableCells() {
        columnID.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
        columnCat.setCellValueFactory(new PropertyValueFactory<Person, String>("category"));
        columnMak.setCellValueFactory(new PropertyValueFactory<Person, Integer>("maker"));
        columnMod.setCellValueFactory(new PropertyValueFactory<Person, Float>("model"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<Person, Float>("quantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Person, Float>("price"));
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../fxml/EditDialog.fxml"));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void fillData() throws Exception {
        addressBook.fillTestData();

        backupList = FXCollections.observableArrayList();
        backupList.addAll(addressBook.getPersonList());

        tableAddressBook.setItems(addressBook.getPersonList());

    }

    private void initListeners() {
        addressBook.getPersonList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                updateCountLabel();
            }
        });

        tableAddressBook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Person selectedPerson = (Person)tableAddressBook.getSelectionModel().getSelectedItem();
                    doEdit(selectedPerson);
                }
            }
        });
    }


    public void actionButtonPressed(ActionEvent actionEvent){
        Object source = actionEvent.getSource();
        // если нажата не кнопка - выходим из метода
        if (!(source instanceof Button)) {
            return;
        }
        Button clickedButton = (Button) source;
        Person selectedPerson = (Person)tableAddressBook.getSelectionModel().getSelectedItem(); //модель выбора позволяет работать с выбранными записями
//        editDialogController.setPerson(selectedPerson);

        switch (clickedButton.getId()){
            case "buttonAdd":
                editDialogController.setPerson(new Person());
                showDialog();
                if(!editDialogController.cancelled) addressBook.add(editDialogController.getPerson());
                System.out.println("add "+selectedPerson);
                break;
            case "buttonEdit":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                doEdit(selectedPerson);
                break;
            case "buttonDelete":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                addressBook.delete((Person)tableAddressBook.getSelectionModel().getSelectedItem());
                System.out.println("delete " + selectedPerson);
                break;
        }
    }

    private void doEdit(Person selectedPerson) {
        editDialogController.setPerson((Person)tableAddressBook.getSelectionModel().getSelectedItem());
        showDialog();
        addressBook.update(editDialogController.getPerson());
        System.out.println("edit " + selectedPerson);
    }

    private boolean personIsSelected(Person selectedPerson) {
        if(selectedPerson == null){
            DialogManager.showInfoDialog("Error", "Выберите контакт");
            return false;
        }
        return true;
    }

    public void showDialog() {
        if (editDialogStage==null) { //ленивая инициализация - создается только 1 раз
            editDialogStage = new Stage();
            editDialogStage.setTitle("Редактирование записи");
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }
//        editDialogStage.show(); //для обычного показа окна
         editDialogStage.showAndWait(); // для ожидания закрытия окна




    }

    public void onSearch(ActionEvent actionEvent) {
        addressBook.getPersonList().clear();
        for (Person person : backupList) { //может притормаживать на больших данных (если делать постраничные запросы в бд)
            if (person.getCategory().toLowerCase().contains(txtSearch.getText().toLowerCase())) {
                addressBook.getPersonList().add(person);
            }
        }
    }

    private void updateCountLabel() {
        labelStatus.setText("Количество записей: " + addressBook.getPersonList().size());
    }

    public void onAction(ActionEvent actionEvent) {
            actionButtonPressed(actionEvent);
    }
}
