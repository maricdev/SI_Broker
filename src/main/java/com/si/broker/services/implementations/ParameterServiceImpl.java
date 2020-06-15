package com.si.broker.services.implementations;


import com.si.broker.api.v1.mappers.IParameterMapper;
import com.si.broker.api.v1.model.ParameterDTO;
import com.si.broker.model.Parameter;
import com.si.broker.repositories.IParameterRepository;
import com.si.broker.services.IParameterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParameterServiceImpl implements IParameterService {

    private final IParameterRepository parametarRepository;
    private final IParameterMapper parametarMapper;

    public ParameterServiceImpl(IParameterRepository parametarRepository, IParameterMapper parametarMapper) {
        this.parametarRepository = parametarRepository;
        this.parametarMapper = parametarMapper;
    }


    @Override
    public ParameterDTO getParametarById(Long id) {
        return parametarMapper.parametarToParametarDTO(parametarRepository.getOne(id));
    }

    @Override
    public List<ParameterDTO> getAllParametars() {
        return parametarRepository.findAll()
                .stream()
                .map(parametarMapper::parametarToParametarDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParameterDTO createParametar(ParameterDTO parameterDTO) {
        return saveAndReturnDTO(parametarMapper.parametarDTOToParametar(parameterDTO));
    }

    @Override
    public Boolean deleteParametarById(Long id) {
        parametarRepository.deleteById(id);
        return true;
    }

    @Override
    public ParameterDTO updateParametar(Long id, ParameterDTO parameterDTO) {
        Parameter inRepository = parametarRepository.getOne(id);

        Parameter parameterToSave = parametarMapper.parametarDTOToParametar(parameterDTO);
        if (inRepository == null)
            return null;

        if (parameterToSave.getName() == null)
            parameterToSave.setName(inRepository.getName());

        //probaj da izbrises
        if (parameterToSave.getType() == null)
            parameterToSave.setType(inRepository.getType());

        if (parameterToSave.getValueType() == null)
            parameterToSave.setValueType(inRepository.getValueType());

        if (parameterToSave.getName() == null)
            parameterToSave.setName(inRepository.getName());

        if (parameterToSave.getEndpoints_id() == null)
            parameterToSave.setEndpoints_id(inRepository.getEndpoints_id());

//


        parameterToSave.setId(id);

        return saveAndReturnDTO(parameterToSave);
    }


    private ParameterDTO saveAndReturnDTO(Parameter parameter) {
        parametarRepository.save(parameter);
        ParameterDTO toReturn = parametarMapper.parametarToParametarDTO(parameter);
        return toReturn;
    }
}
