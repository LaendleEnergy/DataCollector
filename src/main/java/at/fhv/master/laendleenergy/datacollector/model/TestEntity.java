package at.fhv.master.laendleenergy.datacollector.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestEntity {
    @Id
    String testId;
}
