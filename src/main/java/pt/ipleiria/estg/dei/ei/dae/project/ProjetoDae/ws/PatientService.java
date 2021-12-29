
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

@Path("patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientService {
    @EJB
    private PatientBean patientBean;

    @GET
    @Path("/")
    public List<PatientDTO> getAllPatientsWS() {
        return this.toDTOs(this.patientBean.getAllPatients());
    }

    private PatientDTO toDTO(Patient patient) {
        return new PatientDTO(patient.getUsername(), patient.getPassword(), patient.getName(), patient.getEmail(),patient.getRole(), patient.isActive(),patient.getHealthProfessionals());
    }


    private HealthProfessionalDTO toDTO(HealthProfessional professional) {
        return new HealthProfessionalDTO(
                professional.getUsername(),
                professional.getPassword(),
                professional.getName(),
                professional.getEmail(),
                professional.getVersion(),
                professional.getProfession(),
                professional.isChefe(),
                professional.getRole(),
                professional.isActive(),
                professional.getPatients()
        );
    }



    private List<PatientDTO> toDTOs(List<Patient> patients) {
        return (List)patients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<HealthProfessionalDTO> healthProfessionalToDTOs(List<HealthProfessional> professionals) {
        return professionals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{username}")
    public Response getPatientDetails(@PathParam("username") String username) throws MyEntityNotFoundException {
        Patient patient = this.patientBean.findPatient(username);
        return patient != null ? Response.ok(this.toDTO(patient)).build() : Response.status(Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @GET
    @Path("{username}/healthprofessionals")
    public Response getPatientHealthProfessionals(@PathParam("username")String username) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(username);
        if (patient !=null){
            List<HealthProfessionalDTO> dtos = healthProfessionalToDTOs(patient.getHealthProfessionals());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @PUT
    @Path("{username}")
    public Response update(@PathParam("username")String username,Patient patient) throws MyEntityNotFoundException {
        Patient updatePatient = this.patientBean.findPatient(username);

        updatePatient.setActive(patient.isActive());
        updatePatient.setMeasurementsList(patient.getMeasurementsList());
        updatePatient.setEmail(patient.getEmail());
        updatePatient.setName(patient.getName());
        updatePatient.setVersion(patient.getVersion()+1);
        patientBean.updatePatient(updatePatient);

        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response createNewPatient (PatientDTO patientDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
            patientBean.create(
                patientDTO.getUsername(),
                patientDTO.getPassword(),
                patientDTO.getName(),
                patientDTO.getEmail(),
                patientDTO.getRole(),
                    patientDTO.isActive()
            );
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{username}")
    public Response delete(@PathParam("username")String username) throws MyEntityNotFoundException {
        Patient deletePatient = this.patientBean.findPatient(username);

        patientBean.delete(deletePatient);

        return Response.ok().build();
    }

    private PatientDTO toDTONoHealthProfessionals(Patient patient){
        return new PatientDTO(patient.getUsername(), patient.getPassword(), patient.getName(), patient.getEmail(),patient.getRole(), patient.isActive(),patient.getHealthProfessionals());
    }

    private List<PatientDTO> toDTOsNoHealthProfessionals(List<Patient> patients){
        return patients.stream().map(this::toDTONoHealthProfessionals).collect(Collectors.toList());
    }
}
