package htw.freiheit.user.web;

public record RegisterRequest(String username, String email, String password, String role) {
}
