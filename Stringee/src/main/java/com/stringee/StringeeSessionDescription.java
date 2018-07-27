package com.stringee;

/**
 * Created by luannguyen on 9/27/16.
 */
public class StringeeSessionDescription  {

    public final StringeeSessionDescription.Type type;
    public final String description;

    public StringeeSessionDescription(StringeeSessionDescription.Type type, String description) {
        this.type = type;
        this.description = description;
    }

    public static enum Type {
        OFFER,
        PRANSWER,
        ANSWER;

        private Type() {
        }

        public String canonicalForm() {
            return this.name().toLowerCase();
        }

        public static StringeeSessionDescription.Type fromCanonicalForm(String canonical) {
            return (StringeeSessionDescription.Type)valueOf(StringeeSessionDescription.Type.class, canonical.toUpperCase());
        }
    }
}
