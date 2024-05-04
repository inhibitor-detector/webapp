package ar.edu.itba.tesis.interfaces.exceptions;

public class DetectorNotFoundException extends NotFoundException {

    public DetectorNotFoundException(Long id) {
        super(String.format("Detector with id %s", id));
    }
}
