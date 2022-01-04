package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.QualitativeMeasureTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.QualitativeMeasureTypeBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.QualitativeMeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("qualitativeMeasureTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QualitativeMeasureTypeService {
    @EJB
    private QualitativeMeasureTypeBean qualitativeMeasureTypeBean;

    @GET
    @Path("/")
    public List<QualitativeMeasureTypeDTO> getAllQualitativeMeasureTypesWS() {
        return this.toDTOs(this.qualitativeMeasureTypeBean.getAllQualitativeMeasureTypes());
    }

    private QualitativeMeasureTypeDTO toDTO(QualitativeMeasureType qualitativeMeasureType) {
        return new QualitativeMeasureTypeDTO(
                qualitativeMeasureType.getId(),
                qualitativeMeasureType.getName(),
                qualitativeMeasureType.isMultiple(),
                qualitativeMeasureType.getValues());
    }

    private List<QualitativeMeasureTypeDTO> toDTOs(List<QualitativeMeasureType> qualitativeMeasureTypes) {
        return (List)qualitativeMeasureTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response getQualitativeMeasureTypeDetails(@PathParam("id") int id) {
        QualitativeMeasureType qualitativeMeasureType = this.qualitativeMeasureTypeBean.findQualitativeMeasureType(id);
        return qualitativeMeasureType != null ? Response.ok(this.toDTO(qualitativeMeasureType)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @POST
    @Path("/")
    public Response createNewQualitativeMeasureType (QualitativeMeasureTypeDTO qualitativeMeasureTypeDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        qualitativeMeasureTypeBean.create(
                qualitativeMeasureTypeDTO.getName(),
                qualitativeMeasureTypeDTO.isMultiple(),
                qualitativeMeasureTypeDTO.getValues());
        return Response.status(Response.Status.CREATED).build();
    }
}
