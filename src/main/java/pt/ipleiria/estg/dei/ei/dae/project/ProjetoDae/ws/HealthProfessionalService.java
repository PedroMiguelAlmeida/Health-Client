package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UpdatePasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.HealthProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Path("healthProfessionals")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class HealthProfessionalService {
    @EJB
    private HealthProfessionalBean healthProfessionalBean;

    @GET
    @Path("/")
    public List<HealthProfessionalDTO> getAllHealthProfessionalsWS() {
        return toDTOs(healthProfessionalBean.getAllHealthProfessionals());
    }
    // Converts an entity Student to a DTO Student class
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

    private PatientDTO toDTO(Patient patient) {
        return new PatientDTO(patient.getUsername(), patient.getPassword(), patient.getName(), patient.getEmail(),patient.getRole(), patient.isActive(),patient.getHealthProfessionals());
    }
    // converts an entire list of entities into a list of DTOs
    private List<HealthProfessionalDTO> toDTOs(List<HealthProfessional> professionals) {
        return professionals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<PatientDTO> patientToDTOs(List<Patient> patients) {
        return (List)patients.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username")String username) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = this.healthProfessionalBean.findHealthProfessional(username);
        return healthProfessional != null ? javax.ws.rs.core.Response.ok(this.toDTO(healthProfessional)).build() : javax.ws.rs.core.Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_ADMINISTRATOR").build();
    }

    @PUT
    @Path("{username}")
    public Response update(@PathParam("username")String username, HealthProfessionalDTO healthProfessionalDTO) throws MyEntityNotFoundException {
        healthProfessionalBean.update(username, healthProfessionalDTO.getName(),
                healthProfessionalDTO.getEmail(), healthProfessionalDTO.getProfession(), healthProfessionalDTO.isActive());

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/")
    public Response createNewPatient (HealthProfessionalDTO professionalDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MessagingException {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        healthProfessionalBean.create(
                professionalDTO.getUsername(),
                generatedString,
                professionalDTO.getName(),
                professionalDTO.getEmail(),
                professionalDTO.getVersion(),
                professionalDTO.getProfession(),
                professionalDTO.isChefe(),
                professionalDTO.getRole(),
                professionalDTO.isActive());
        return Response.status(Response.Status.CREATED).build();
    }

//    @PUT
//    @Path("{username}/updatePassword")
//    public Response updatePassword(@PathParam("username")String username, UpdatePasswordDTO updatePasswordDTO) throws MyEntityNotFoundException {
//
//        System.out.println(updatePasswordDTO.getPassword());
//        System.out.println(updatePasswordDTO.getToken());
//        healthProfessionalBean.updatePassword(username,updatePasswordDTO.getPassword(), updatePasswordDTO.getToken());
//        healthProfessionalBean.deleteToken(username, updatePasswordDTO.getToken());
//        return  Response.status(Response.Status.OK).build();
//    }
//
//    @POST
//    @Path("{username}/changePassword")
//    public Response changePassword(@PathParam("username")String username) throws MyConstraintViolationException, MessagingException, MyEntityNotFoundException, MyEntityExistsException {
//
//        healthProfessionalBean.sendEmailToChangePassword(username);
//        return Response.status(Response.Status.OK).build();
//    }

    @DELETE
    @Path("{username}")
    public Response delete(@PathParam("username")String username) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(username);
        if(healthProfessional != null && healthProfessional.isActive() == true){
            healthProfessionalBean.delete(username);
        }else if( healthProfessional.isActive() == false){
            return  Response.status(Response.Status.FORBIDDEN).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("{username}/patients")
    public Response getHealthProfessionalsPatient(@PathParam("username")String username) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(username);
        if (healthProfessional !=null){
            List<PatientDTO> dtos = patientToDTOs(healthProfessional.getPatients());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @PUT
    @Path("{username}/addPatientToList")
    public Response updatePatientList(@PathParam("username")String username,PatientDTO patientDTO) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(username);
        if (healthProfessional == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        healthProfessionalBean.signPatients(username, patientDTO.getUsername());
        return Response.status(Response.Status.CREATED).build();


    }

    @PUT
    @Path("{username}/removePatientsFromList")
    public Response removePatientsList(@PathParam("username")String username,PatientDTO patientDTO) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(username);
        if (healthProfessional == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        healthProfessionalBean.unsignPatients(username, patientDTO.getUsername());
        return Response.status(Response.Status.CREATED).build();
    }



}
