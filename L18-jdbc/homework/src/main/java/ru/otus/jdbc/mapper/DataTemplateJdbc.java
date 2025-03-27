package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String selectByIdSql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, selectByIdSql, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createEntity(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String selectAllSql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor
                .executeSelect(connection, selectAllSql, Collections.emptyList(), rs -> {
                    var resultList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            resultList.add(createEntity(rs));
                        }
                        return resultList;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        String insertSql = entitySQLMetaData.getInsertSql();
        List<Object> params = getFieldValues(object, entityClassMetaData.getFieldsWithoutId());
        try {
            return dbExecutor.executeStatement(connection, insertSql, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        String updateSql = entitySQLMetaData.getUpdateSql();
        List<Object> params = getFieldValues(object, entityClassMetaData.getFieldsWithoutId());
        params.add(getFieldValue(object, entityClassMetaData.getIdField()));
        try {
            dbExecutor.executeStatement(connection, updateSql, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createEntity(ResultSet rs) {
        try {
            Constructor<T> constructor = entityClassMetaData.getConstructor();
            T entity = constructor.newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                VarHandle varHandle = getVarHandle(field);
                Object value = rs.getObject(field.getName());
                varHandle.set(entity, value);
            }
            return entity;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> getFieldValues(T object, List<Field> fieldsWithoutId) {
        List<Object> values = new ArrayList<>();
        for (Field field : fieldsWithoutId) {
            values.add(getFieldValue(object, field));
        }
        return values;
    }

    private Object getFieldValue(T object, Field field) {
        try {
            VarHandle varHandle = getVarHandle(field);
            return varHandle.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VarHandle getVarHandle(Field field) {
        try {
            MethodHandles.Lookup lookup =
                    MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup());
            return lookup.unreflectVarHandle(field);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
