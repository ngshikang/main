package seedu.address.login;

public class UserPass {

    private String username;
    private String password;

    public UserPass(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
