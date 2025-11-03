package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCaretakerCommand;
import seedu.address.logic.commands.EditCaretakerCommand.EditCaretakerDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCaretakerCommandParser implements Parser<EditCaretakerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCaretakerCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_RELATIONSHIP);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCaretakerCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_RELATIONSHIP);

        EditCaretakerDescriptor editCaretakerDescriptor = new EditCaretakerDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editCaretakerDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editCaretakerDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String raw = argMultimap.getValue(PREFIX_ADDRESS).get();
            if (raw.isBlank()) {
                // a/ with no value => copy from patient
                editCaretakerDescriptor.markCopyAddressFromPatient();
            } else {
                editCaretakerDescriptor.setAddress(ParserUtil.parseAddress(raw));
            }
        }
        if (argMultimap.getValue(PREFIX_RELATIONSHIP).isPresent()) {
            editCaretakerDescriptor.setRelationship(ParserUtil.parseRelationship(argMultimap
                    .getValue(PREFIX_RELATIONSHIP).get()));
        }

        if (!editCaretakerDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCaretakerCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCaretakerCommand(index, editCaretakerDescriptor);
    }
}
