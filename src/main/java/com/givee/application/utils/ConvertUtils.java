package com.givee.application.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

@UtilityClass
public class ConvertUtils {
    public static <T, R> List<T> convertPageToList(ModelMapper modelMapper, Page<R> page, Class<T> tClass) {
        return convertLists(modelMapper, page.getContent(), tClass);
    }

    public static <T, R> List<T> convertLists(ModelMapper modelMapper, List<R> list, Class<T> tClass) {
        return list.stream().map(r -> modelMapper.map(r, tClass)).toList();
    }
}
