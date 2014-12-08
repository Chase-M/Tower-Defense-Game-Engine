package gameAuthoring.scenes;


import gameAuthoring.mainclasses.AuthorController;
import gameAuthoring.mainclasses.controllerInterfaces.GeneralSettingsConfiguring;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utilities.GSON.objectWrappers.GeneralSettingsWrapper;
import utilities.JavaFXutilities.DragAndDropFilePanes.audioPanes.DragAndDropCopyAudioPane;

public class GeneralSettingScene {

	private static final String GENERAL_SETTING_MSG = "Create Your Game Settings";
	private GeneralSettingsConfiguring myGeneralSettingsController;
	private Scene myScene;
	private VBox myVBox;
	private TextField myNameTextField;
	private TextField myHealthTextField;
	private TextField myCashTextField;
	private DragAndDropCopyAudioPane myAudioPane;
	private ComboBox<String> myGameTypeComboBox;
	private Group root;
	private HBox myHBox;


	public GeneralSettingScene(GeneralSettingsConfiguring controller){
		myGeneralSettingsController = controller;
		root = new Group();
		myScene = new Scene(root, AuthorController.SCREEN_WIDTH, AuthorController.SCREEN_HEIGHT);
		myVBox = new VBox(25);
		myVBox.setPrefWidth(AuthorController.SCREEN_WIDTH/2);
		myVBox.setPadding(new Insets(100));
		myVBox.setAlignment(Pos.CENTER);
		
		//root.getChildren().add(myVBox);
		createHeadingLabel();
		createOptionFields();
		createOptions();
		myAudioPane = new DragAndDropCopyAudioPane(200, 200, "/src");	
		//createAudioPane("Drag Your Desired Background Music Here:", myAudioPane);
		createNextButton();
		createRightImage();
	}


	private void createHeadingLabel(){
		Label headingLabel = new Label(GENERAL_SETTING_MSG);
		headingLabel.setStyle("-fx-font-size: 24px");
		myVBox.getChildren().add(headingLabel);
	}


	private void createOptionFields(){
		myNameTextField = new TextField();
		myNameTextField.setPrefWidth(AuthorController.SCREEN_WIDTH/3);
		myHealthTextField = new TextField();
		myHealthTextField.setPrefWidth(AuthorController.SCREEN_WIDTH/4);
		myCashTextField = new TextField();
		myCashTextField.setPrefWidth(AuthorController.SCREEN_WIDTH/4);
		myGameTypeComboBox = new ComboBox<String>();
		myGameTypeComboBox.getItems().addAll(
				"SinglePlayer",
				"Coop"
				);
	}
	

	private void createOptions(){
		createTextFieldWithLabel("Name of Your Game", myNameTextField);
		createTextFieldWithLabel("Starting Health", myHealthTextField);
		createTextFieldWithLabel("Starting Amount of Cash", myCashTextField);
		createComboBoxWithLabel("Select Game Type", myGameTypeComboBox);
	}


	private void createTextFieldWithLabel(String labelName, TextField textField){
		VBox vb = new VBox(15);
		//vb.setAlignment(Pos.BOTTOM_LEFT);
		Label optionLabel = new Label(labelName);
		vb.getChildren().addAll(optionLabel, textField);
		myVBox.getChildren().add(vb);

	}
	
	private void createComboBoxWithLabel(String labelName, ComboBox<String> comboBox){
		VBox vb = new VBox(15);
		Label optionLabel = new Label(labelName);
		vb.getChildren().addAll(optionLabel, comboBox);
		myVBox.getChildren().add(vb);
	}


	private void createAudioPane(String audioPaneLabel, DragAndDropCopyAudioPane audioPane){

		Label audioLabel = new Label(audioPaneLabel);
		VBox vb = new VBox(15);
		//vb.getChildren().add(audioPane);
	
	}


	private void createNextButton(){

		Button nextButton = new Button("Start Building!");
		nextButton.setOnAction(event -> handleButtonClick());	
		
		myVBox.getChildren().add(nextButton);	
	}

	private void handleButtonClick(){
		
        String gameNameText = myNameTextField.getText();
        int startingCash = Integer.parseInt(myCashTextField.getText());
        int startingHealth = Integer.parseInt(myHealthTextField.getText());
        String gameType = myGameTypeComboBox.getValue();
        if (!gameNameText.isEmpty() && !gameType.isEmpty() && startingCash!= 0 && startingHealth!=0) {
            myGeneralSettingsController.makeDirectory(gameNameText, gameType);
            GeneralSettingsWrapper wrapper = new GeneralSettingsWrapper(startingHealth, startingCash);
            myGeneralSettingsController.setGeneralSettings(wrapper);
        }
        
        
	}

	public void createRightImage(){
		HBox hb = new HBox();
		Image img = new Image("gameAuthoring/scenes/GeneralSettingsImage.png");
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(AuthorController.SCREEN_WIDTH/2);
		imgView.setFitHeight(AuthorController.SCREEN_HEIGHT);
		hb.getChildren().addAll(myVBox, imgView);
		root.getChildren().add(hb);
	}


	public Scene getScene(){
		return myScene;
	}






}
