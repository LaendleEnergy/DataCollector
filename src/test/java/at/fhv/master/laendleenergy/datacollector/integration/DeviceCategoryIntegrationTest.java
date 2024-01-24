package at.fhv.master.laendleenergy.datacollector.integration;


import at.fhv.master.laendleenergy.datacollector.controller.DeviceCategoryController;
import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
@TestHTTPEndpoint(DeviceCategoryController.class)
public class DeviceCategoryIntegrationTest {

    @InjectMock
    EntityManager entityManager;
    private final String validJwtToken = "eyJraWQiOiIvcHJpdmF0ZWtleS5wZW0iLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FyZDMzMy5jb20iLCJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsImlhdCI6MTcwNDcxMTgwMCwiZXhwIjozNjAwMTcwNDcxMTgwMCwiZ3JvdXBzIjpbIkFkbWluIl0sIm1lbWJlcklkIjoiMSIsImhvdXNlaG9sZElkIjoiaDEiLCJkZXZpY2VJZCI6IkQxIiwianRpIjoiYzlhNjJmYWEtMGIxZS00YzdiLTk3MDQtOTY0N2YwNGZmZDFjIn0.XgV-PnqA_LB9OFFE8-zr0UIMugTb6P4qPvymCoancALWvS4VJjF-tXjU02yms0YvSXC-GmpbyUDZtiPm26KApjawXaoNSa5gonsnTHl6s4bT8MkgUrNNs9Di9KmCHgoTohgr9B7pelM6eJCOf5tT-phkoSvaxxrYn099BYsUeA1DVVsApic1egEV1ItZYRops8XUR-KPydeimgYq6tpc2g-7L7RiNIYkssvVxxh25-EGn8lLkivBu3gA7_2siCZfVZbP8JWagT629OK9B_GpnOhz8_-p5KSjMRjDTJBcRTnzYQDGzOB-RmsB0NZaLPw5ulqR1yN3r5KEpm-GExAKRw";



    @Test
    void getAllDeviceCategoriesTest(){
        Query<DeviceCategory> queryMock = Mockito.mock(Query.class);

        Mockito.when(entityManager.createQuery(anyString(), eq(DeviceCategory.class))).thenReturn(queryMock);
        Mockito.when(queryMock.getResultList()).thenReturn(List.of(new DeviceCategory(), new DeviceCategory()));

        given()
                .header("Authorization", "Bearer " + validJwtToken)
                .when().get("/all")
                .then()
                .statusCode(200);

    }

    @Test
    void addDeviceCategoryTest(){
        Query<DeviceCategory> queryMock = Mockito.mock(Query.class);
        Mockito.when(entityManager.createQuery(anyString(), eq(DeviceCategory.class))).thenReturn(queryMock);
        Mockito.when(queryMock.setParameter(anyString(), anyString())).thenReturn(queryMock);

        String deviceDTOJSONString = "{\"name\":\"name\"}";

        given()
                .header("Authorization", "Bearer " + validJwtToken)
                .contentType(ContentType.JSON)
                .body(deviceDTOJSONString)
                .when().post("/add")
                .then()
                .statusCode(200);

    }


}
