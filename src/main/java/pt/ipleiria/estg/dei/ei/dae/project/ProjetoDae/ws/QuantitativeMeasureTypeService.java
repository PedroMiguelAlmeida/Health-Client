package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.QuantitativeMeasureTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.QuantitativeMeasureTypeBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Measurement;
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

@Path("quantitativeMeasureTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuantitativeMeasureTypeService {
    @EJB
    private QuantitativeMeasureTypeBean quantitativeMeasureTypeBean;

    @GET
    @Path("/")
    public List<QuantitativeMeasureTypeDTO> getAllQuantitativeMeasureTypesWS() {
        return this.toDTOs(this.quantitativeMeasureTypeBean.getAllQuantitativeMeasureTypes());
    }

    private QuantitativeMeasureTypeDTO toDTO(QuantitativeMeasureType quantitativeMeasureType) {
        return new QuantitativeMeasureTypeDTO(
                quantitativeMeasureType.getId(),
                quantitativeMeasureType.getName(),
                quantitativeMeasureType.isMultiple(),
                quantitativeMeasureType.getMin(),
                quantitativeMeasureType.getMax(),
                quantitativeMeasureType.isDecimal());
    }

    private List<QuantitativeMeasureTypeDTO> toDTOs(List<QuantitativeMeasureType> quantitativeMeasureTypes) {
        return (List)quantitativeMeasureTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response getQuantitativeMeasureTypeDetails(@PathParam("id") int id) throws MyEntityNotFoundException {
        QuantitativeMeasureType quantitativeMeasureType = quantitativeMeasureTypeBean.findQuantitativeMeasureType(id);
        return quantitativeMeasureType != null ? Response.ok(this.toDTO(quantitativeMeasureType)).build() : Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_STUDENT").build();
    }

    @POST
    @Path("/")
    public Response createNewQuantitativeMeasureType (QuantitativeMeasureTypeDTO quantitativeMeasureTypeDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        quantitativeMeasureTypeBean.create(
                quantitativeMeasureTypeDTO.getName(),
                quantitativeMeasureTypeDTO.isMultiple(),
                quantitativeMeasureTypeDTO.getMin(),
                quantitativeMeasureTypeDTO.getMax(),
                quantitativeMeasureTypeDTO.isDecimal());
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id")int id, QuantitativeMeasureTypeDTO quantitativeMeasureTypeDTO) throws MyEntityNotFoundException, MyConstraintViolationException {

        quantitativeMeasureTypeBean.updateQuantitativeMeasureType(
                id,
                quantitativeMeasureTypeDTO.getName(),
                quantitativeMeasureTypeDTO.isMultiple(),
                quantitativeMeasureTypeDTO.getMin(),
                quantitativeMeasureTypeDTO.getMax(),
                quantitativeMeasureTypeDTO.isDecimal()
        );

        return Response.ok().build();
    }

}
