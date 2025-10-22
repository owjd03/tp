package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Panel containing the list of packages.
 */
public class PackageListPanel extends UiPart<Region> {
    private static final String FXML = "PackageListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PackageListPanel.class);

    @FXML
    private ListView<InsurancePackage> packageListView;

    /**
     * Creates a {@code PackageListPanel} with the given {@code ObservableList}.
     */
    public PackageListPanel(ObservableList<InsurancePackage> packageList) {
        super(FXML);
        packageListView.setItems(packageList);
        packageListView.setCellFactory(listView -> new PackageListPanel.PackageListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of an {@code InsurancePackage} using a {@code PackageCard}.
     */
    class PackageListViewCell extends ListCell<InsurancePackage> {
        @Override
        protected void updateItem(InsurancePackage insurancePackage, boolean empty) {
            super.updateItem(insurancePackage, empty);

            if (empty || insurancePackage == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PackageCard(insurancePackage).getRoot());
            }
        }
    }
}
