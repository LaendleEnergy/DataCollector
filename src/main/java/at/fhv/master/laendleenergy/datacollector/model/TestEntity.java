package at.fhv.master.laendleenergy.datacollector.model;


import jakarta.persistence.*;

@Entity
@Table(name = "TestEntity")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String testId;

    public TestEntity() {
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}
