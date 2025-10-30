package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_RELATIONSHIP_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.RELATIONSHIP_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.RELATIONSHIP_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RELATIONSHIP_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RELATIONSHIP_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCaretakerCommand;
import seedu.address.logic.commands.EditCaretakerCommand.EditCaretakerDescriptor;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.testutil.EditCaretakerDescriptorBuilder;

public class EditCaretakerCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCaretakerCommand.MESSAGE_USAGE);

    private final EditCaretakerCommandParser parser = new EditCaretakerCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCaretakerCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid individual fields
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.INVALID_CHARS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.INVALID_DIGITS);
        assertParseFailure(parser, "1" + INVALID_RELATIONSHIP_DESC, Relationship.BLANK_RELATIONSHIP);

        // invalid combination
        assertParseFailure(parser,
                "1" + INVALID_NAME_DESC + INVALID_ADDRESS_DESC + VALID_PHONE_AMY,
                Name.INVALID_CHARS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB
                + ADDRESS_DESC_AMY + NAME_DESC_AMY + RELATIONSHIP_DESC_AMY;

        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_AMY)
                .withRelationship(VALID_RELATIONSHIP_AMY)
                .build();

        EditCaretakerCommand expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + RELATIONSHIP_DESC_BOB;

        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withRelationship(VALID_RELATIONSHIP_BOB)
                .build();

        EditCaretakerCommand expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_THIRD_PERSON;

        // name
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .build();
        EditCaretakerCommand expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditCaretakerDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditCaretakerDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // relationship
        userInput = targetIndex.getOneBased() + RELATIONSHIP_DESC_AMY;
        descriptor = new EditCaretakerDescriptorBuilder().withRelationship(VALID_RELATIONSHIP_AMY).build();
        expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // valid followed by invalid
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + RELATIONSHIP_DESC_BOB + PHONE_DESC_AMY
                + ADDRESS_DESC_BOB + RELATIONSHIP_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(
                PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_RELATIONSHIP));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC;
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_ADDRESS));
    }

    @Test
    public void parse_addressBlankCopyFromPatient_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // " a/" with no value after the prefix
        String userInput = targetIndex.getOneBased() + " " + PREFIX_ADDRESS;

        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptor();
        descriptor.markCopyAddressFromPatient();

        EditCaretakerCommand expectedCommand = new EditCaretakerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
