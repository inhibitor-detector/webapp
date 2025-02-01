package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.service.DetectorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DetectorControllerTest {

    @InjectMocks
    DetectorController detectorController;
    @Mock
    DetectorService detectorService;

    @Test
    public void testGetDetectors() {

    }


}