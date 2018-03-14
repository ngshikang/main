package seedu.address.logic;

import seedu.address.logic.commands.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements methods to autocomplete commands and fields in a user query
 */
public class Autocompleter {

    private Logic logic;

    private  final String[] commandsList =  {AddCommand.COMMAND_WORD,
            ClearCommand.COMMAND_WORD,
            DeleteCommand.COMMAND_WORD,
            EditCommand.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            FindCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD,
            HistoryCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD,
            RedoCommand.COMMAND_WORD,
            SelectCommand.COMMAND_WORD,
            UndoCommand.COMMAND_WORD};
    private List<String> names;
    private List<String> phones;
    private List<String> emails;
    private List<String> adresses;

    public Autocompleter(Logic logic){
        this.logic = logic;
        updateFields();
    }

    public String autocomplete(String input) {

        String[] words = input.trim().split(" ");
        Set<String> possibilities = new HashSet<>();

        //trying to complete a command
        if (words.length == 1 && words[0].length() > 0) {

            for (String command : commandsList) {
                if (command.startsWith(words[0])) {
                    //we only append the end of the command
                    possibilities.add(command.substring(words[0].length()));
                }
            }
        }
        //trying to complete a field
        else if (words.length > 1) {
            int lastFieldIndex = input.lastIndexOf('/');

            if (lastFieldIndex > 0 && input.substring(lastFieldIndex).length() > 0) {
                String field = input.substring(lastFieldIndex + 1);
                List<String> fieldsList;

                switch (input.charAt(lastFieldIndex - 1)) {
                    case 'n':
                        fieldsList = names;
                        break;

                    case 'p':
                        fieldsList = phones;
                        break;

                    case 'e':
                        fieldsList = emails;
                        break;

                    case 'a':
                        fieldsList = adresses;
                        break;

                    default:
                        fieldsList = new ArrayList<>();
                        break;
                }

                for (String fieldPossibility : fieldsList) {

                    if (fieldPossibility.startsWith(field)) {
                        //we only append the end of the field
                        possibilities.add(fieldPossibility.substring(field.length()));
                    }
                }
            }
        }
        String longestCommonPrefix = "";

        if (possibilities.size() > 0) {
            String p = (String) possibilities.toArray()[0];

            for (int i = 0; i < p.length(); i++) {
                boolean isPrefixOkay = true;

                for (String possibility : possibilities) {
                    if (!possibility.startsWith(longestCommonPrefix + p.charAt(i))) {
                        isPrefixOkay = false;
                    }
                }
                if (!isPrefixOkay) {
                    break;
                }

                longestCommonPrefix += p.charAt(i);
            }
        }
        return longestCommonPrefix;
    }

    private void updateFields(){
        names = logic.getFilteredPersonList().stream().map(person -> person.getName().toString()).collect(Collectors.toList());
        phones = logic.getFilteredPersonList().stream().map(person -> person.getPhone().toString()).collect(Collectors.toList());
        emails = logic.getFilteredPersonList().stream().map(person -> person.getEmail().toString()).collect(Collectors.toList());
        adresses = logic.getFilteredPersonList().stream().map(person -> person.getAddress().toString()).collect(Collectors.toList());
    }
}
