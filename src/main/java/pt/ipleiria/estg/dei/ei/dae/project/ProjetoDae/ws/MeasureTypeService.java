package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.MeasureTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.MeasureTypeBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("measureTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasureTypeService {
    @EJB
    private MeasureTypeBean measureTypeBean;

    @GET
    @Path("/")
    public List<MeasureTypeDTO> getAllMeasureTypesWS() {
        return toDTOs(measureTypeBean.getAllMeasureTypes());
    }

    private MeasureTypeDTO toDTO(MeasureType measureType) {
        return new MeasureTypeDTO(measureType.getId(), measureType.getName(), measureType.isMultiple(), measureType.getType());
    }

    private List<MeasureTypeDTO> toDTOs(List<MeasureType> measureTypes) {
        return measureTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
