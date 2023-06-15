package h06;

public enum Package {
    H06("h06"),
    PROBLEMS(H06, "problems"),
    WORLD(H06, "world"),
    ;
    private final String name;

    Package(String name) {
        this.name = name;
    }

    Package(Package p, String name) {
        this.name = p.name + "." + name;
    }

    public String getName() {
        return name;
    }
}
