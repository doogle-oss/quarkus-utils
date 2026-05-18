package org.doogleoss.pg;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@GraphQLApi
public class DepartmentResource {

    @Query
    @Description("List all Department records")
    public List<Department> departments() {
        return Department.getAll();
    }

    @Query
    @Description("Find a Department record by id")
    public Department department(Long id) {
        Department entity = Department.getById(id);
        if (entity == null) {
            throw new NotFoundException("Department not found: " + id);
        }
        return entity;
    }

    @Mutation
    @Transactional
    @Description("Create a Department record")
    public Department createDepartment(String departmentName) {
        Department entity = new Department();
        entity.departmentName = departmentName;
        entity.persist();
        return entity;
    }

    @Mutation
    @Transactional
    @Description("Update a Department record")
    public Department updateDepartment(Long id, String departmentName) {
        Department entity = Department.getById(id);
        if (entity == null) {
            throw new NotFoundException("Department not found: " + id);
        }

        entity.departmentName = departmentName;
        return entity;
    }

    @Mutation
    @Transactional
    @Description("Delete a Department record")
    public boolean deleteDepartment(Long id) {
        return Department.removeById(id);
    }
}
