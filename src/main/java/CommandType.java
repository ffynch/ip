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
    private final boolean acceptsArguments;

    CommandType(String keyword, boolean acceptsArguments) {
        this.keyword = keyword;
        this.acceptsArguments = acceptsArguments;
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
                    || type.acceptsArguments && input.startsWith(type.keyword + " ")) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
