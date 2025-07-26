package com.example.bffauth.model;

public class AuthResult {
    private boolean success;
    private String source;

    public static AuthResult success(String source) {
        AuthResult result = new AuthResult();
        result.success = true;
        result.source = source;
        return result;
    }

    public static AuthResult failure() {
        AuthResult result = new AuthResult();
        result.success = false;
        return result;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
