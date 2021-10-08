package seedu.address.logic.parser;


import static seedu.address.logic.parser.CliSyntax.FLAG_FRIEND;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.friend.FriendIdContainsKeywordPredicate;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, FLAG_FRIEND);

        if (argMultimap.getValue(FLAG_FRIEND).isPresent()) {
            return new ListCommand(
                    new FriendIdContainsKeywordPredicate(argMultimap.getValue(FLAG_FRIEND).get()));
        }
        // no tags present, default to friend search
        return new ListCommand(new FriendIdContainsKeywordPredicate(args));
    }

}
