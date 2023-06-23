package ru.itmo.kurs004.handlers;

public class DatabaseCommand {
    public enum CommandType {
        BEGIN_TRANSACTION,
        PERSIST_ENTITY,
        MERGE_ENTITY,
        COMMIT_TRANSACTION,
        PRINT_LINE
    }

    private CommandType type;
    private Object argument;

    public DatabaseCommand(CommandType type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandType getType() {
        return type;
    }

    public Object getArgument() {
        return argument;
    }
}