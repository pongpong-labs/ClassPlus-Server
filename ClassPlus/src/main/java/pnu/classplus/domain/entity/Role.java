package pnu.classplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_ASSISTANT("ROLE_ASSISTANT"),
    ROLE_PROFESSOR("ROLE_PROFESSOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String text;

    Role(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @JsonCreator
    public static Role fromText(String text) {
        for (Role r : Role.values()) {
            if (r.getText().equals(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() { return this.text; }
}