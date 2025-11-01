package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEPENDENTS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DOB_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MARITAL_STATUS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OCCUPATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SALARY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        assertTrue(DESC_AMY.equals(DESC_AMY));

        assertFalse(DESC_AMY.equals(null));

        assertFalse(DESC_AMY.equals(5));

        assertFalse(DESC_AMY.equals(DESC_BOB));

        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withSalary(VALID_SALARY_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withDateOfBirth(VALID_DOB_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withMaritalStatus(VALID_MARITAL_STATUS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withDependents(VALID_DEPENDENTS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withOccupation(VALID_OCCUPATION_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withInsurancePackage(VALID_INSURANCE_PACKAGE_NAME_BOB, VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB)
                .build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().orElse(null) + ", salary="
                + editPersonDescriptor.getSalary().orElse(null) + ", dateOfBirth="
                + editPersonDescriptor.getDateOfBirth().orElse(null) + ", maritalStatus="
                + editPersonDescriptor.getMaritalStatus().orElse(null) + ", occupation="
                + editPersonDescriptor.getOccupation().orElse(null) + ", dependents="
                + editPersonDescriptor.getInsurancePackage().orElse(null) + ", insurancePackage="
                + editPersonDescriptor.getDependents().orElse(null) + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
