package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.MeasurementDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.MeasurementBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("measurements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementService {
    @EJB
    private MeasurementBean measurementBean;

    @GET
    @Path("/")
    public List<MeasurementDTO> getAllMeasurementsWS() {
        return this.toDTOs(this.measurementBean.getAllMeasurements());
    }

    private MeasurementDTO toDTO(Measurement measurement) {
        return new MeasurementDTO(measurement.getId(), measurement.getMeasureType().getId(), measurement.getMeasureType().getName(), measurement.getValue(), measurement.getInputSource(), measurement.getPatient().getUsername());
    }

    private List<MeasurementDTO> toDTOs(List<Measurement> measurements) {
        return measurements.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response getMeasurementDetails(@PathParam("id") int id) throws MyEntityNotFoundException {
        Measurement measurement = this.measurementBean.findMeasurement(id);
        return measurement != null ? Response.ok(this.toDTO(measurement)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @GET
    @Path("/patient/{username}")
    public Response getUsernamePrescriptions(@PathParam("username") String username) throws MyEntityNotFoundException {
        List<Measurement> measurement = this.measurementBean.findMeasurementByPatient(username);
        if(measurement== null){
            throw new MyEntityNotFoundException("Prescriptions not found");
        }
        return Response.ok(this.toDTOs(measurement)).build();

    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id")int id, MeasurementDTO updatedMeasurement) throws MyEntityNotFoundException, MyConstraintViolationException {
        measurementBean.updateMeasurement(
                id,
                updatedMeasurement.getMeasureTypeId(),
                updatedMeasurement.getValue(),
                updatedMeasurement.getInputSource(),
                updatedMeasurement.getUsername()
        );

        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response createNewMeasurement (MeasurementDTO measurementDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        measurementBean.create(
                measurementDTO.getMeasureTypeId(),
                measurementDTO.getValue(),
                measurementDTO.getInputSource(),
                measurementDTO.getUsername()
        );
        return Response.status(Response.Status.CREATED).build();
    }
}
