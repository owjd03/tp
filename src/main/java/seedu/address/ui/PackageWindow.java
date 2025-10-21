package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Controller for a package page
 */
public class PackageWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(PackageWindow.class);
    private static final String FXML = "PackageWindow.fxml";

    private PackageListPanel packageListPanel;

    @FXML
    private StackPane packageListPanelPlaceholder;

    /**
     * Creates a new PackageWindow.
     *
     * @param root Stage to use as the root of the PackageWindow.
     */
    public PackageWindow(Stage root, ObservableList<InsurancePackage> packageObservableList) {
        super(FXML, root);
        packageListPanel = new PackageListPanel(packageObservableList);
        packageListPanelPlaceholder.getChildren().add(packageListPanel.getRoot());
    }

    /**
     * Creates a new PackageWindow.
     */
    public PackageWindow(ObservableList<InsurancePackage> packageObservableList) {
        this(new Stage(), packageObservableList);
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
