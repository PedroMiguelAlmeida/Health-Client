package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.PrescriptionBean;
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

    @GET
    @Path("{id}")
    public Response getPrescriptionDetails(@PathParam("id") int id) throws MyEntityNotFoundException {
        Prescription prescription = this.prescriptionBean.findPrescription(id);
        return prescription != null ? Response.ok(this.toDTO(prescription)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id")int id,Prescription updatedPrescription) throws MyEntityNotFoundException, MyConstraintViolationException {
        Prescription prescription = this.prescriptionBean.findPrescription(id);

        updatedPrescription.setId(id);
        prescriptionBean.updatePrescription(updatedPrescription);

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
}