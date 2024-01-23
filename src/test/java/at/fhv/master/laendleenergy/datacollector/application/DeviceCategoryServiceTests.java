package at.fhv.master.laendleenergy.datacollector.application;
import at.fhv.master.laendleenergy.datacollector.application.services.DeviceCategoryService;
import at.fhv.master.laendleenergy.datacollector.controller.dto.DeviceCategoryDTO;
import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.exception.DeviceCategoryAlreadyExistsException;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceCategoryRepository;
import at.fhv.master.laendleenergy.datacollector.model.repositories.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@QuarkusTest
public class DeviceCategoryServiceTests {


    @InjectMock
    DeviceCategoryRepository deviceCategoryRepository;

    @Inject
    DeviceCategoryService deviceService;


    @BeforeEach
    void setUp()  {
        DeviceCategory deviceCategory1 = new DeviceCategory("Waschmaschine");
        DeviceCategory deviceCategory2 = new DeviceCategory("Toaster");
        Mockito.when(deviceCategoryRepository.getAllDeviceCategories()).thenReturn(List.of(deviceCategory1, deviceCategory2));
    }


    @Test
    public void getAllDeviceCategoriesTest(){
        deviceService.getAllDeviceCategories();
        Mockito.verify(deviceCategoryRepository, Mockito.times(1)).getAllDeviceCategories();
    }


    @Test
    public void addDeviceCategoryTest() throws DeviceCategoryAlreadyExistsException, JsonProcessingException {
        deviceService.addDeviceCategory(new DeviceCategoryDTO("Waschmaschine"));
        Mockito.verify(deviceCategoryRepository, times(1)).saveDeviceCategory(any());
    }


}