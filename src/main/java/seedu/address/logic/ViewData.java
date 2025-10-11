package seedu.address.logic;

import seedu.address.model.person.Person;

/**
 * Container for data view.
 */
public class ViewData {
    private boolean isView;
    private final Person person;

    /**
     * Construct a {@code ViewData} with the given isView and a {@code Person}.
     */
    public ViewData(boolean isView, Person person) {
        this.isView = isView;
        this.person = person;
    }

    public boolean isShowView() {
        return this.isView;
    }

    public Person getPerson() {
        return this.person;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewData)) {
            return false;
        }

        ViewData otherViewData = (ViewData) other;
        if (person == null) {
            return isView == otherViewData.isShowView() && otherViewData.getPerson() == null;
        } else {
            return isView == otherViewData.isShowView()
                    && person.equals(otherViewData.getPerson());
        }
    }
}
