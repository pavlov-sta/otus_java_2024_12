package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(
                "SELECT * FROM %s WHERE %s = ?",
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        var fields = entityClassMetaData.getFieldsWithoutId();
        var fieldName = fields.stream().map(Field::getName).collect(Collectors.joining(","));
        var fieldValues = fields.stream().map(field -> "?").collect(Collectors.joining(","));
        return String.format("INSERT INTO %s (%s) VALUES (%s)", entityClassMetaData.getName(), fieldName, fieldValues);
    }

    @Override
    public String getUpdateSql() {
        var fields = entityClassMetaData.getFieldsWithoutId();
        var fieldAssignments =
                fields.stream().map(field -> field.getName() + " = ?").collect(Collectors.joining(","));
        return String.format(
                "UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName(),
                fieldAssignments,
                entityClassMetaData.getIdField().getName());
    }
}