package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.insurance.InsurancePackage;

/**
 * An UI component that displays information of a {@code Package}.
 */
public class PackageCard extends UiPart<Region> {

    private static final String FXML = "PackageListCard.fxml";

    public final InsurancePackage insurancePackage;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label description;

    /**
     * Creates a {@code PackageCode} with the given {@code Package} and index to display.
     */
    public PackageCard(InsurancePackage insurancePackage) {
        super(FXML);
        this.insurancePackage = insurancePackage;
        name.setText(insurancePackage.getPackageName());
        description.setText(insurancePackage.getPackageDescription());
    }
}
