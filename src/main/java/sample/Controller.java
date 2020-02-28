package sample;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import leijnse.info.AcdpAccessor;
import leijnse.info.ExtractPictureMetaData;
import leijnse.info.ImageRow;
import leijnse.info.PictureMetaData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.List;

public class Controller {

    @FXML
    private ListView listItems = new ListView();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File choosedDirectory =new File("dummy");

    @FXML
    private TextField txtSearchKeywords = new TextField();

    @FXML
    private Label lblAcdpDirectory = new Label();

    @FXML
    private Label lblClickedImage = new Label();

    @FXML
    public void setAcdpDirectory(Event e){

        System.out.println("Button setAcdpDiretory clicked");
        configuringDirectoryChooser(directoryChooser);
        choosedDirectory = directoryChooser.showDialog(new Stage());
        String myChoosenDirectory = choosedDirectory.toPath().toString();
        System.out.println("choosedDirectory: " + myChoosenDirectory);
        lblAcdpDirectory.setText(myChoosenDirectory);
        lblClickedImage.setText("");
        Node node = null;
        lblClickedImage.setGraphic(node);


    }

    @FXML
    public void startSearch(Event e){
        System.out.println("Button startSearch clicked");
        AcdpAccessor acdpAccessor = new AcdpAccessor();
        listItems.getItems().clear();
        listItems.refresh();

        List<ImageRow> imageWithSomeKeywords = acdpAccessor.selectFromImageTable(true,lblAcdpDirectory.getText() + "/layout", "-","-", BigInteger.valueOf(0),txtSearchKeywords.getText());
        imageWithSomeKeywords.forEach(imageRow -> {
            listItems.getItems().add(imageRow.getDirectory()+"/"+imageRow.getFile()+", keywords: " + imageRow.getIptcKeywords());
        });
        listItems.refresh();
        lblClickedImage.setText("");
        Node node = null;
        lblClickedImage.setGraphic(node);


    }

    @FXML
    public void listItemsClicked(Event e) throws FileNotFoundException {
        System.out.println("listItems clicked");
        lblClickedImage.setText("listItems clicked");
        ObservableList selectedIndices = listItems.getSelectionModel().getSelectedIndices();

        int myIndex = 0;
        for(Object o : selectedIndices){
            System.out.println("o = " + o + " (" + o.getClass() + ")");
            myIndex = (int)o;
            String myItemText = (String)listItems.getItems().get(myIndex);
            System.out.println(myItemText);
            int endItemText = myItemText.indexOf(", keywords");
            if (endItemText>0){
                String myImage = myItemText.substring(0,endItemText);
                System.out.println(myImage);
                lblClickedImage.setText(myItemText);
                FileInputStream input = new FileInputStream(myImage);
                Image image = new Image(input);
                ImageView imageView = new ImageView(image);

                System.out.println("pictureMetaData Image Height: " + image.getHeight());
                System.out.println("pictureMetaDate Image Widht: " + image.getWidth());
                imageView.setFitHeight(300);
                double sizeCorrector = (image.getWidth() / image.getHeight());
                double correctedSize = sizeCorrector * 300;
                imageView.setFitWidth(correctedSize);
                lblClickedImage.setGraphic(imageView);
            }
            else {
                lblClickedImage.setText(myItemText);
            }
        }
    }
    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for FileChooser
        directoryChooser.setTitle("Select Directory");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}
