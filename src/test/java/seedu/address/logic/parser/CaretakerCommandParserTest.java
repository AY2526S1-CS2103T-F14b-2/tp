package seedu.address.logic.parser;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CaretakerCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;


import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

public class CaretakerCommandParserTest {

    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PHONE = "98765432";
    private static final String VALID_ADDRESS = "12 Clementi Rd";
    private static final String VALID_RELATIONSHIP = "Brother";

    private static final String NAME_DESC = " " + PREFIX_NAME + VALID_NAME;
    private static final String PHONE_DESC = " " + PREFIX_PHONE + VALID_PHONE;
    private static final String ADDRESS_DESC = " " + PREFIX_ADDRESS + VALID_ADDRESS;
    private static final String RELATIONSHIP_DESC = " " + PREFIX_RELATIONSHIP + VALID_RELATIONSHIP;

    private static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "!!!";
    private static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "abc";
    private static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS + "";
    private static final String INVALID_RELATIONSHIP_DESC = " " + PREFIX_RELATIONSHIP + "";

    private final CaretakerCommandParser parser = new CaretakerCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index targetIndex = Index.fromOneBased(1);
        Caretaker expectedCaretaker = new Caretaker(
                new Name(VALID_NAME),
                new Phone(VALID_PHONE),
                new Address(VALID_ADDRESS),
                new Relationship(VALID_RELATIONSHIP)
        );

        String userInput = "1" + NAME_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC;

        assertParseSuccess(parser, userInput, new CaretakerCommand(targetIndex, expectedCaretaker));
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CaretakerCommand.MESSAGE_USAGE);

        // missing name
        assertParseFailure(parser, "1" + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC, expectedMessage);

        // missing phone
        assertParseFailure(parser, "1" + NAME_DESC + ADDRESS_DESC + RELATIONSHIP_DESC, expectedMessage);

        // missing address
        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + RELATIONSHIP_DESC, expectedMessage);

        // missing relationship
        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + ADDRESS_DESC, expectedMessage);

        // missing index
        assertParseFailure(parser, NAME_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, "1" + NAME_DESC + INVALID_PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC,
                Phone.INVALID_DIGITS);

        // invalid address
        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + INVALID_ADDRESS_DESC + RELATIONSHIP_DESC,
                Address.BLANK_ADDRESS);

        // invalid relationship (if you have constraints)
        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + ADDRESS_DESC + INVALID_RELATIONSHIP_DESC,
                Relationship.BLANK_RELATIONSHIP);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = "zero" + NAME_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC;
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CaretakerCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertParseFailure(parser, "1" + NAME_DESC + NAME_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + ADDRESS_DESC + ADDRESS_DESC + RELATIONSHIP_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        assertParseFailure(parser, "1" + NAME_DESC + PHONE_DESC + ADDRESS_DESC + RELATIONSHIP_DESC + RELATIONSHIP_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_RELATIONSHIP));
    }

}
