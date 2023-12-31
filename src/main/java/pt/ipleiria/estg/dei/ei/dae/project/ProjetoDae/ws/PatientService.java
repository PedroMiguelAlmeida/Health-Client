
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.validation.constraints.Email;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.TokenBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Token;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

@Path("patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientService {
    @EJB
    private PatientBean patientBean;
    @EJB
    private EmailBean emailBean;
    @EJB
    private TokenBean tokenBean;


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

    private EmailDTO toDTO(EmailDTO emailDTO){
        return new EmailDTO();
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
    @Path("/active/{username}")
    public Response getAativePatient(@PathParam("username") String username) throws MyEntityNotFoundException {
        System.err.println("Patient SERVICE");
        List<Patient> patient = this.patientBean.findActivePatient(username);
        return patient != null ? Response.ok(this.toDTOs(patient)).build() : Response.status(Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
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
    public Response update(@PathParam("username")String username, PatientDTO patientDTO) throws MyEntityNotFoundException {

        System.err.println("HELP ServiceAdmin Username "+ patientDTO.getUsername()+" name: "+patientDTO.getName()+"email: "+patientDTO.getEmail());
        patientBean.update(username,patientDTO.getName(),patientDTO.getEmail(),patientDTO.isActive());

        return Response.status(Response.Status.OK).build();
    }

//    @PUT
//    @Path("{username}/updatePassword")
//    public Response updatePassword(@PathParam("username")String username, UpdatePasswordDTO updatePasswordDTO) throws MyEntityNotFoundException {
//
//        System.out.println(updatePasswordDTO.getPassword());
//        System.out.println(updatePasswordDTO.getToken());
//        patientBean.updatePassword(username,updatePasswordDTO.getPassword(), updatePasswordDTO.getToken());
//        patientBean.deleteToken(username, updatePasswordDTO.getToken());
//        return  Response.status(Response.Status.OK).build();
//    }
//
//    @POST
//    @Path("{username}/changePassword")
//    public Response changePassword(@PathParam("username")String username) throws MyConstraintViolationException, MessagingException, MyEntityNotFoundException, MyEntityExistsException {
//
//        patientBean.sendEmailToChangePassword(username);
//        return Response.status(Response.Status.OK).build();
//    }


    @POST
    @Path("/")
    public Response createNewPatient (PatientDTO patientDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MessagingException {
        System.out.println();
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        patientBean.create(
                patientDTO.getUsername(),
                generatedString,
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
        Patient patient = patientBean.findPatient(username);
        if(patient != null && patient.isActive() == true){
            patientBean.delete(username);
        }else if( patient.isActive() == false){
            return  Response.status(Response.Status.FORBIDDEN).build();
        }else{
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email)
            throws MyEntityNotFoundException, MessagingException {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            throw new MyEntityNotFoundException("Patient with username '" + username
                    + "' not found in our records.");
        }
        emailBean.send(patient.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }

    //private PatientDTO toDTONoHealthProfessionals(Patient patient){
      //  return new PatientDTO(patient.getUsername(), patient.getPassword(), patient.getName(), patient.getEmail(),patient.getRole(), patient.isActive(),patient.getHealthProfessionals());
    //}

    //private List<PatientDTO> toDTOsNoHealthProfessionals(List<Patient> patients){
      //  return patients.stream().map(this::toDTONoHealthProfessionals).collect(Collectors.toList());
    //}

    @PUT
    @Path("{username}/addHealthProfessionalToList")
    public Response updateHealthProfessionalList(@PathParam("username")String username,HealthProfessional healthProfessional) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(username);
        if (patient == null){
            throw new MyEntityNotFoundException("Patient was not found");
        }
        patientBean.signHealthProfessionals(patient,healthProfessional, healthProfessional.getUsername());
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{username}/removeHealthProfessionalFromList")
    public Response removeHealthProfessionalList(@PathParam("username")String username,HealthProfessional healthProfessional) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(username);
        if (patient == null){
            throw new MyEntityNotFoundException("Patient was not found");
        }
        patientBean.unsignHealthProfessionals(patient,healthProfessional, healthProfessional.getUsername());
        return Response.status(Response.Status.CREATED).build();
    }


}
