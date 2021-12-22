package org.dontpanic.zookidash.zk;

class RuokResponse {
    private String command;
    private String error;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
