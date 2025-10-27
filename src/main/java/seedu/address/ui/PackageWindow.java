package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;

/**
 * Controller for a package page
 */
public class PackageWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(PackageWindow.class);
    private static final String FXML = "PackageWindow.fxml";
    private static final KeyCombination EXIT_KEY= new KeyCodeCombination(KeyCode.E);
    private static final String EXIT_CODE = "Exit Window - [E]";

    private PackageListPanel packageListPanel;

    @FXML
    private StackPane packageListPanelPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a new PackageWindow.
     *
     * @param root Stage to use as the root of the PackageWindow.
     */
    public PackageWindow(Stage root, ObservableList<InsurancePackage> packageObservableList) {
        super(FXML, root);
        packageListPanel = new PackageListPanel(packageObservableList);

        setAccelerator();
        fillInnerParts();
    }

    /**
     * Creates a new PackageWindow.
     */
    public PackageWindow(ObservableList<InsurancePackage> packageObservableList) {
        this(new Stage(), packageObservableList);
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
        packageListPanelPlaceholder.getChildren().add(packageListPanel.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(EXIT_CODE);
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
    }

    /**
     * Shows the package window.
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
        logger.fine("Showing all available package list.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the package window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the package window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the package window.
     */
    public void focus() {
        getRoot().requestFocus();
    }
}
