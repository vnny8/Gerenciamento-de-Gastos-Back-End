package com.vnny8.gerenciamento_de_gastos.mapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class MapperObjects {

    private static ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject (O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination){
        List<D> destinationObjects = new ArrayList<>();
        for (O o : origin){
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    };
}
