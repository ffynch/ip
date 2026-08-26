package beemo.parser;

/**
 * Identifies the command represented by a line of user input.
 */
public enum CommandType {
    BYE("bye", false),
    LIST("list", false),
    MARK("mark", true),
    UNMARK("unmark", true),
    DELETE("delete", true),
    TODO("todo", true),
    DEADLINE("deadline", true),
    EVENT("event", true),
    UNKNOWN("", false);

    private final String keyword;
    private final boolean canAcceptArguments;

    CommandType(String keyword, boolean canAcceptArguments) {
        this.keyword = keyword;
        this.canAcceptArguments = canAcceptArguments;
    }

    public String getKeyword() {
        return keyword;
    }

    /**
     * Returns the command type matching the input's first keyword.
     */
    public static CommandType from(String input) {
        for (CommandType type : values()) {
            if (input.equals(type.keyword)
                    || (type.canAcceptArguments && input.startsWith(type.keyword + " "))) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
