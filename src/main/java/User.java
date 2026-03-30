public class User {
    private String name;
    private String email;
    private String userId;
    private String role;

    public User(String name, String email, String userId, String role) {
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.role = role;
    }

    public String getname() { return name; }
    public void setname(String name) { this.name = name; }
    public String getemail() { return email; }
    public void setemail(String email) { this.email = email; }
    public String getuserID() { return userId; }
    public void setuserId(String userId) { this.userId = userId; }
    public String getrole() { return role; }
    public void setrole(String role) { this.role = role; }
}
