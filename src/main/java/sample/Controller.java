package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import leijnse.info.AcdpAccessor;
import leijnse.info.CopyDirectory;
import leijnse.info.ImageRow;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Controller {

    @FXML
    private ListView listItems = new ListView();

    private ListView listImages = new ListView();
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

        List<ImageRow> imageWithSomeKeywords = acdpAccessor.selectFromImageTable(true,lblAcdpDirectory.getText() + "/layout", "-","-", txtSearchKeywords.getText());
        imageWithSomeKeywords.forEach(imageRow -> {
            String allKeywords = putKeyWordsInString(imageRow.getIptcKeywords());
            listItems.getItems().add(imageRow.getDirectory()+"/"+imageRow.getFile()+", keywords: " + allKeywords);
        });
        listItems.refresh();
        lblClickedImage.setText("");
        Node node = null;
        lblClickedImage.setGraphic(node);


    }

    @FXML
    public void createKeywordsFromMicrrosoftVision(Event e) throws IOException {
        System.out.println("Button createKeywordsFromMicrrosoftVision clicked");
        String mySubscriptionKey = new String(Files.readAllBytes(Paths.get("/Users/edleijnse/OneDrive/Finanzen/Lizensen/Microsoft/keys/subscriptionKey1")));

        CopyDirectory copyDirectory = new CopyDirectory();
        copyDirectory.setSubscriptionKey(mySubscriptionKey);
        String myChoosenImageDirectory = choosedImageDirectory.toPath().toString();
        String tempDirectory = "/Volumes/MyDrive01/temp";

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

        List<ImageRow> imageWithSomeKeywords = acdpAccessor.selectFromImageTable(true,lblAcdpDirectory.getText() + "/layout", "-","-",txtSearchKeywords.getText());
        imageWithSomeKeywords.forEach(imageRow -> {
            String allKeywords = putKeyWordsInString(imageRow.getIptcKeywords());
            listItems.getItems().add(imageRow.getDirectory()+"/"+imageRow.getFile()+", keywords: " + allKeywords);
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
        listImages.getItems().clear();
        listImages.refresh();

        List<ImageRow> imageWithSomeKeywords = acdpAccessor.selectFromImageTable(true,lblAcdpDirectory.getText() + "/layout", "-","-", txtSearchKeywords.getText());
        imageWithSomeKeywords.forEach(imageRow -> {
            String allKeywords = putKeyWordsInString(imageRow.getIptcKeywords());
            listItems.getItems().add(imageRow.getDirectory()+"/"+imageRow.getFile()+", keywords: " + allKeywords);
            listImages.getItems().add(imageRow.getImage());
        });

        listItems.refresh();
        lblClickedImage.setText("");
        Node node = null;
        lblClickedImage.setGraphic(node);


    }
    public String putKeyWordsInString(String[] iptcKeyWords){
        String allKeywords = "";
        String delimitor = "";

        for (int ii=0;ii<iptcKeyWords.length;ii++){
            System.out.println(iptcKeyWords[ii]);
            allKeywords += delimitor + iptcKeyWords[ii];
            delimitor = ", ";
        }
        return allKeywords;
    }
    public Image getImage(String iFileName) throws IOException {
        System.out.println("fileName: " + iFileName);
        int ii = iFileName.lastIndexOf("/");
        System.out.println("ii: " + ii);
        if (ii<0) {
            ii = iFileName.lastIndexOf("\\");
        }
        final byte[][] myImage = {null};
        if (ii>0){
            String myFileName = iFileName.substring(ii+1);
            System.out.println("fileName: " + myFileName);
            AcdpAccessor acdpAccessor = new AcdpAccessor();

            List<ImageRow> imageWithSomeKeywords = acdpAccessor.selectFromImageTable(true,lblAcdpDirectory.getText() + "/layout", "-",myFileName, "-");
            imageWithSomeKeywords.forEach(imageRow -> {
                myImage[0] = imageRow.getImage();
                System.out.println("image found, length: " + myImage[0].length);
            });
        }

        ByteArrayInputStream bis = new ByteArrayInputStream((myImage[0]));
        Image image = new Image(bis);
        return image;
    }
    public Image getImageFromByteArray(byte[] iImageByteArray) throws IOException {

        final byte[][] myImage = {null};

        if (iImageByteArray != null){
            myImage[0] = iImageByteArray;
            ByteArrayInputStream bis = new ByteArrayInputStream((myImage[0]));
            Image image = new Image(bis);
            return image;
        }

      return null;
    }


    @FXML
    public void listItemsClicked(Event e) throws IOException {
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
                Image image1 = getImageFromByteArray((byte[])listImages.getItems().get(myIndex));
                ImageView imageView = new ImageView(image1);

                System.out.println("pictureMetaData Image Height: " + image1.getHeight());
                System.out.println("pictureMetaDate Image Widht: " + image1.getWidth());
                imageView.setFitHeight(300);
                double sizeCorrector = (image1.getWidth() / image1.getHeight());
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
