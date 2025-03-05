package se.kumliens.ringring.security;

public record OAuth2User(String firstName, String lastName, String email, String picture){

    public String domain() {
        return email.substring(email.indexOf("@") + 1);
    }
}
