package h06;

/**
 * Defines the packages for H06.
 */
public enum Package {
    /**
     * The root package for H06.
     */
    H06("h06"),
    /**
     * The package for H06 problems.
     */
    PROBLEMS(H06, "problems"),

    /**
     * The package for H06 world.
     */
    WORLD(H06, "world"),
    ;

    /**
     * The name of the package.
     */
    private final String name;

    /**
     * Constructs a package with the given name.
     *
     * @param name the name of the package
     */
    Package(String name) {
        this.name = name;
    }

    /**
     * Constructs a package with the given name in the given parent package.
     *
     * @param p    the parent package
     * @param name the name of the package
     */
    Package(Package p, String name) {
        this.name = p.name + "." + name;
    }

    /**
     * Returns the name of the package.
     *
     * @return the name of the package
     */
    public String getName() {
        return name;
    }
}
