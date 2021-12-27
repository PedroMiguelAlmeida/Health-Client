package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.QuantitativeMeasureTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.QuantitativeMeasureTypeBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.QuantitativeMeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("quantitativeMeasureType")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuantitativeMeasureTypeService {
    @EJB
    private QuantitativeMeasureTypeBean quantitativeMeasureTypeTypeBean;

    @GET
    @Path("/")
    public List<QuantitativeMeasureTypeDTO> getAllQuantitativeMeasureTypesWS() {
        return this.toDTOs(this.quantitativeMeasureTypeTypeBean.getAllQuantitativeMeasureTypes());
    }

    private QuantitativeMeasureTypeDTO toDTO(QuantitativeMeasureType quantitativeMeasureTypeType) {
        return new QuantitativeMeasureTypeDTO(
                quantitativeMeasureTypeType.getName(),
                quantitativeMeasureTypeType.isMultiple(),
                quantitativeMeasureTypeType.getMin(),
                quantitativeMeasureTypeType.getMax(),
                quantitativeMeasureTypeType.isDecimal());
    }

    private List<QuantitativeMeasureTypeDTO> toDTOs(List<QuantitativeMeasureType> quantitativeMeasureTypeTypes) {
        return (List)quantitativeMeasureTypeTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response getQuantitativeMeasureTypeDetails(@PathParam("id") int id) {
        QuantitativeMeasureType quantitativeMeasureTypeType = this.quantitativeMeasureTypeTypeBean.findQuantitativeMeasureType(id);
        return quantitativeMeasureTypeType != null ? Response.ok(this.toDTO(quantitativeMeasureTypeType)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @POST
    @Path("/")
    public Response createNewQuantitativeMeasureType (QuantitativeMeasureTypeDTO quantitativeMeasureTypeTypeDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        quantitativeMeasureTypeTypeBean.create(
                quantitativeMeasureTypeTypeDTO.getName(),
                quantitativeMeasureTypeTypeDTO.isMultiple(),
                quantitativeMeasureTypeTypeDTO.getMin(),
                quantitativeMeasureTypeTypeDTO.getMax(),
                quantitativeMeasureTypeTypeDTO.isDecimal());
        return Response.status(Response.Status.CREATED).build();
    }
}
