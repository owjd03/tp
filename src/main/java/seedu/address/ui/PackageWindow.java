package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

public class PackageWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(PackageWindow.class);
    private static final String FXML = "PackageWindow.fxml";

    /**
     * Creates a new PackageWindow.
     *
     * @param root Stage to use as the root of the PackageWindow.
     */
    public PackageWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new PackageWindow.
     */
    public PackageWindow() {
        this(new Stage());
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
