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
import leijnse.info.CopyDirectory;
import leijnse.info.ImageRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Controller {

    @FXML
    private ListView listItems = new ListView();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File choosedAcdpDirectory =new File("dummy");
    File choosedImageDirectory =new File("dummy");
    File choosedEmptyImageDirectory =new File("dummy");



    @FXML
    private TextField txtSearchKeywords = new TextField();

    @FXML
    private Label lblAcdpDirectory = new Label();

    @FXML
    private Label lblImageDirectory = new Label();


    @FXML
    private Label lblClickedImage = new Label();

    @FXML
    public void setAcdpDirectory(Event e){

        System.out.println("Button setAcdpDiretory clicked");
        configuringDirectoryChooser(directoryChooser);
        choosedAcdpDirectory = directoryChooser.showDialog(new Stage());
        String myChoosenDirectory = choosedAcdpDirectory.toPath().toString();
        System.out.println("choosedDirectory: " + myChoosenDirectory);
        lblAcdpDirectory.setText(myChoosenDirectory);
        lblClickedImage.setText("");
        Node node = null;
        lblClickedImage.setGraphic(node);


    }

    @FXML
    public void setImageDirectory(Event e){

        System.out.println("Button setImageDiretory clicked");
        configuringDirectoryChooser(directoryChooser);
        choosedImageDirectory = directoryChooser.showDialog(new Stage());
        String myChoosenDirectory = choosedImageDirectory.toPath().toString();
        System.out.println("choosedDirectory: " + myChoosenDirectory);
        lblImageDirectory.setText(myChoosenDirectory);

    }



    @FXML
    public void cmdDirectoryNamesClicked(Event e){
        System.out.println("Button cmdDirectoryNamesClicked clicked");
        CopyDirectory copyDirectory = new CopyDirectory();
        String myChoosenImageDirectory = choosedImageDirectory.toPath().toString();
        String myChoosenDirectory = choosedAcdpDirectory.toPath().toString() + "/layout";
        copyDirectory.copyFilesDirectoryNameToACDP(myChoosenImageDirectory,myChoosenDirectory);
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
    public void createKeywordsFromMicrrosoftVision(Event e) throws IOException {
        System.out.println("Button createKeywordsFromMicrrosoftVision clicked");
        String mySubscriptionKey = new String(Files.readAllBytes(Paths.get("/home/pi/Dokumente/subscriptionKey1")));
        CopyDirectory copyDirectory = new CopyDirectory();
        copyDirectory.setSubscriptionKey(mySubscriptionKey);
        String myChoosenImageDirectory = choosedImageDirectory.toPath().toString();
        String tempDirectory = "/media/pi/datapartition/temp";

        copyDirectory.addVisionTagsToFiles(myChoosenImageDirectory,tempDirectory );
        System.out.println("createKeywordsFromMicrrosoftVision done");

    }



    @FXML
    public void cmdExifDataClicked(Event e){
        System.out.println("Button cmdExifDataClicked clicked");
        CopyDirectory copyDirectory = new CopyDirectory();
        String myChoosenImageDirectory = choosedImageDirectory.toPath().toString();
        String myChoosenDirectory = choosedAcdpDirectory.toPath().toString() + "/layout";
        copyDirectory.copyFilesToACDP(myChoosenImageDirectory,myChoosenDirectory);
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
    public void cmdInitializeACDPClicked(Event e) throws IOException {
        System.out.println("Button cmdInitializeACDPClicked clicked");
        AcdpAccessor acdpAccessor = new AcdpAccessor();


        String myChoosenDirectory = choosedAcdpDirectory.toPath().toString();
        choosedEmptyImageDirectory = directoryChooser.showDialog(new Stage());
        String myChoosenEmptyImageDirectory = choosedEmptyImageDirectory.toPath().toString();
        acdpAccessor.copyLayout(myChoosenEmptyImageDirectory,myChoosenDirectory );

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
                // lblClickedImage.setText(myItemText);
                lblClickedImage.setText("");
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
