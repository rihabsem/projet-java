
import java.util.Objects;

public class JobDetails {
    private String experienceLevel;   // Experience level required for the job
    private String niveauEtude;       // Educational qualification required
    private String function;          // Job function or title
    private String activity;          // Job activity sector

    // Constructor
    public JobDetails(String experienceLevel, String function, String activity, String niveauEtude) {
        this.experienceLevel = experienceLevel;
        this.function = function;
        this.activity = activity;
        this.niveauEtude = niveauEtude;
    }

    // Getters and Setters
    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    // Override toString() for better output formatting
    @Override
    public String toString() {
        return "Experience Level: " + experienceLevel + "\n" +
                "Function: " + function + "\n" +
                "Activity: " + activity + "\n" +
                "Niveau d'Étude: " + niveauEtude;
    }

    // Optionally, override equals() and hashCode() if using this class in collections
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JobDetails that = (JobDetails) obj;
        return experienceLevel.equals(that.experienceLevel) &&
                niveauEtude.equals(that.niveauEtude) &&
                function.equals(that.function) &&
                activity.equals(that.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(experienceLevel, niveauEtude, function, activity);
}
}
