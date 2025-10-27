package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Controller for a view page
 */
public class ViewWindow extends UiPart<Stage> {

    private static final String FXML = "ViewWindow.fxml";

    private static final Logger logger = LogsCenter.getLogger(ViewWindow.class);

    private static final KeyCombination EXIT_KEY= new KeyCodeCombination(KeyCode.E);

    private static final String EXIT_CODE = "Exit Window - [E]";

    private Person personToView;
    private PersonListPanel personListPanel; //to contain PersonCard

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private ListView<Person> personListView;

    @FXML
    private StackPane statusbarPlaceholder;


    /**
     * Creates a new ViewWindow.
     *
     * @param root Stage to use as the root of the ViewWindow.
     */
    public ViewWindow(Stage root) {
        super(FXML, root);
        this.personToView = null;

        setAccelerator();
    }

    /**
     * Creates a new ViewWindow.
     */
    public ViewWindow() {
        this(new Stage());
    }

    /**
     * Set the personToView variable
     */
    public void setPerson(Person personToView) {
        this.personToView = personToView;
        fillInnerParts();
    }

    /**
     * Sets the accelerator of to close the window
     */
    private void setAccelerator() {
        getRoot().getScene().getAccelerators().put(
                EXIT_KEY,
                () -> hide()
        );

    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        ObservableList<Person> viewList = FXCollections.observableArrayList(personToView);
        personListPanel = new PersonListPanel(viewList, true);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(EXIT_CODE);
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
    }

    /**
     * Shows the view window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing view page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the view window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the view window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the view window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

}
