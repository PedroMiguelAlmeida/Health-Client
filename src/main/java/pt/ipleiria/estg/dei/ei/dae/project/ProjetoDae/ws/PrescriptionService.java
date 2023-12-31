package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.MeasurementDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Measurement;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
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

@Path("prescriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PrescriptionService {

    @EJB
    private PrescriptionBean prescriptionBean;

    @GET
    @Path("/")
    public List<PrescriptionDTO> getAllPrescriptionsWS() {
        return this.toDTOs(this.prescriptionBean.getAllPrescriptions());
    }

    private PrescriptionDTO toDTO(Prescription prescription) {
        return new PrescriptionDTO(prescription.getId(), prescription.getHealthProfessional().getUsername(), prescription.getPatient().getUsername(), prescription.getMeasurements(), prescription.getTreatment(), prescription.getDescription());
    }

    private List<PrescriptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private MeasurementDTO toDTO(Measurement measurement) {
        return new MeasurementDTO(measurement.getId(), measurement.getMeasureType().getId(), measurement.getMeasureType().getName(), measurement.getValue(), measurement.getInputSource(), measurement.getPatient().getUsername());
    }

    private List<MeasurementDTO> toDTOMeasurments(List<Measurement> measurements) {
        return measurements.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @GET
    @Path("{id}")
    public Response getPrescriptionDetails(@PathParam("id") int id) throws MyEntityNotFoundException {
        Prescription prescription = this.prescriptionBean.findPrescription(id);
        return prescription != null ? Response.ok(this.toDTO(prescription)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }
    @GET
    @Path("/professional/{username}")
    public Response getUsernamePrescriptions(@PathParam("username") String username) throws MyEntityNotFoundException {
        List<Prescription> prescriptions = this.prescriptionBean.findPrescriptions(username);
        if(prescriptions== null){
            throw new MyEntityNotFoundException("Prescriptions not found");
        }
        return Response.ok(this.toDTOs(prescriptions)).build();
    }
    @GET
    @Path("/patient/{username}")
    public Response getPatientPrescriptions(@PathParam("username") String username) throws MyEntityNotFoundException {
        List<Prescription> prescriptions = this.prescriptionBean.findPatientPrescriptions(username);
        if(prescriptions== null){
            throw new MyEntityNotFoundException("Prescriptions not found");
        }
        return Response.ok(this.toDTOs(prescriptions)).build();
    }
    @GET
    @Path("/{id}/measurments")
    public Response getPrescriptionMeasurements(@PathParam("id") int id) throws MyEntityNotFoundException {
        Prescription prescription = this.prescriptionBean.findPrescription(id);
        List<Measurement> measurements = prescription.getMeasurements();
        if(measurements == null){
            throw new MyEntityNotFoundException();
        }
        return Response.ok(this.toDTOMeasurments(measurements)).build();
        //return measurement != null ? Response.ok(this.toDTO(measurement)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @GET
    @Path("/active/{id}")
    public Response getActivePrescription(@PathParam("id") int id) throws MyEntityNotFoundException {
        System.err.println("Prescription SERVICE");
        List<Prescription> prescription = this.prescriptionBean.findActivePrescription(id);
        return prescription != null ? Response.ok(this.toDTOs(prescription)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id")int id, PrescriptionDTO prescriptionDTO) throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityExistsException {
        prescriptionBean.updatePrescription(
                id,
                prescriptionDTO.getHealthProfessional_username(),
                prescriptionDTO.getPatient_username(),
                prescriptionDTO.getMeasurements(),
                prescriptionDTO.getTreatment(),
                prescriptionDTO.getDescription()
        );

        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response createNewPrescription (PrescriptionDTO prescriptionDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        prescriptionBean.create(
                prescriptionDTO.getHealthProfessional_username(),
                prescriptionDTO.getPatient_username(),
                prescriptionDTO.getMeasurements(),
                prescriptionDTO.getTreatment(),
                prescriptionDTO.getDescription()
        );
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id")int id) throws MyEntityNotFoundException {
        Prescription prescription = prescriptionBean.findPrescription(id);
        if(prescription != null && prescription.isActive()){
            prescriptionBean.deletePrescription(id);
        }else if(!prescription.isActive()){
            return  Response.status(Response.Status.FORBIDDEN).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
