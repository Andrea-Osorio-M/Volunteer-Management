package co.edu.uptc.model;

/**
 * Represents a volunteer with basic personal details.
 */
public class Volunteer {
    private String name;
    private int age;
    private String email;

    /**
     * Default constructor.
     */
    public Volunteer() {
    }

    /**
     * Parameterized constructor.
     * @param name The full name of the volunteer.
     * @param age The age of the volunteer.
     * @param email The email of the volunteer.
     */
    public Volunteer(String name, int age, String email) {
        this.name = name;
        if (age >= 18) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("The volunteer must be at least 18 years old.");
        }
        this.email = email;
    }

    /**
     * Gets the name of the volunteer.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the volunteer.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the age of the volunteer.
     * @return The age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the volunteer, ensuring they are at least 18.
     * @param age The age to set.
     */
    public void setAge(int age) {
        if (age >= 18) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("The volunteer must be at least 18 years old.");
        }
    }

    /**
     * Gets the email of the volunteer.
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the volunteer.
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a string representation of the volunteer.
     * @return A formatted string with volunteer details.
     */
    @Override
    public String toString() {
        return "Volunteer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}