package at.fhv.master.laendleenergy.datacollector.integration;
import at.fhv.master.laendleenergy.datacollector.application.enums.Interval;
import at.fhv.master.laendleenergy.datacollector.controller.MeasurementController;
import at.fhv.master.laendleenergy.datacollector.model.AccumulatedMeasurements;
import at.fhv.master.laendleenergy.datacollector.model.AveragedMeasurement;
import at.fhv.master.laendleenergy.datacollector.model.Measurement;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.persistence.EntityManager;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
@TestHTTPEndpoint(MeasurementController.class)
public class MeasurementIntegrationTest {

    @InjectMock
    EntityManager entityManager;
    private final String validJwtToken = "eyJraWQiOiIvcHJpdmF0ZWtleS5wZW0iLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FyZDMzMy5jb20iLCJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsImlhdCI6MTcwNDcxMTgwMCwiZXhwIjozNjAwMTcwNDcxMTgwMCwiZ3JvdXBzIjpbIkFkbWluIl0sIm1lbWJlcklkIjoiMSIsImhvdXNlaG9sZElkIjoiaDEiLCJkZXZpY2VJZCI6IkQxIiwianRpIjoiYzlhNjJmYWEtMGIxZS00YzdiLTk3MDQtOTY0N2YwNGZmZDFjIn0.XgV-PnqA_LB9OFFE8-zr0UIMugTb6P4qPvymCoancALWvS4VJjF-tXjU02yms0YvSXC-GmpbyUDZtiPm26KApjawXaoNSa5gonsnTHl6s4bT8MkgUrNNs9Di9KmCHgoTohgr9B7pelM6eJCOf5tT-phkoSvaxxrYn099BYsUeA1DVVsApic1egEV1ItZYRops8XUR-KPydeimgYq6tpc2g-7L7RiNIYkssvVxxh25-EGn8lLkivBu3gA7_2siCZfVZbP8JWagT629OK9B_GpnOhz8_-p5KSjMRjDTJBcRTnzYQDGzOB-RmsB0NZaLPw5ulqR1yN3r5KEpm-GExAKRw";


    //reoccurring global data
    Measurement measurement = new Measurement("D1", LocalDateTime.now(), 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0);
    AveragedMeasurement averagedMeasurement = new AveragedMeasurement(LocalDateTime.now(),  LocalDateTime.now().plusHours(1), "D1",0, 0,
            0, 0, 0, 0, 0, 0);

    AccumulatedMeasurements accumulatedMeasurement = new AccumulatedMeasurements(LocalDateTime.now(),  LocalDateTime.now().plusHours(1), "D1",
            0, 0, 0);




    @Test
    void getTagNamesTest(){
        Query<String> queryMock = Mockito.mock(NativeQuery.class);
        Mockito.when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(anyString(), anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.getResultList()).thenReturn(List.of("deviceName1", "deviceName2"));

        given()
                .header("Authorization", "Bearer " + validJwtToken)
                .when().get("/tags/names/all")
                .then()
                .statusCode(200);
    }


    @Test
    void getMeasurementsBetweenDatesTest(){

        List<Measurement> measurements = List.of(measurement);

        String startDate = "2023-05-08T06:26:35";
        String endDate = "2023-05-08T06:26:40";


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);


        Query<Measurement> queryMock = Mockito.mock(Query.class);
        Mockito.when(entityManager.createQuery(anyString(), eq(Measurement.class))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("startTime"), eq(startDateTime))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("endTime"), eq(endDateTime))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("meterDeviceId"), eq("D1"))).thenReturn(queryMock);
        Mockito.when(queryMock.getResultList()).thenReturn(measurements);

        given()
                .header("Authorization", "Bearer " + validJwtToken)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .when().get("/")
                .then()
                .statusCode(200);
    }


    @Test
    void getAveragedMeasurementsBetweenDatesTest(){
        List<Object[]> measurements = Collections.emptyList();

        String startDate = "2023-05-08T06:26:35";
        String endDate = "2023-05-08T06:26:40";


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);


        NativeQuery<Object[]> queryMock = Mockito.mock(NativeQuery.class);
        Mockito.when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("startTime"), eq(startDateTime))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("endTime"), eq(endDateTime))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("meterDeviceId"), eq("D1"))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("interval"), eq("day"))).thenReturn(queryMock);
        Mockito.when(queryMock.getResultList()).thenReturn(measurements);

        given()
                .header("Authorization", "Bearer " + validJwtToken)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("interval", "day")
                .when().get("/averaged/")
                .then()
                .statusCode(200);

    }

    @Test
    void  getAccumulatedMeasurementsBetweenDateTest(){
        List<Object[]> measurements = Collections.emptyList();

        String startDate = "2023-05-08T06:26:35";
        String endDate = "2023-05-08T06:26:40";


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);


        NativeQuery<Object[]> queryMock = Mockito.mock(NativeQuery.class);
        Mockito.when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("startTime"), eq(startDateTime))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("endTime"), eq(endDateTime))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("meterDeviceId"), eq("D1"))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(eq("interval"), eq(Interval.DAY.toString()))).thenReturn(queryMock);
        Mockito.when(queryMock.getResultList()).thenReturn(measurements);

        given()
                .header("Authorization", "Bearer " + validJwtToken)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("interval", "day")
                .when().get("/accumulated")
                .then()
                .statusCode(200);

    }

    @Test
    void addTagToMeasurementsTest(){


    }

}
