package org.booking.core.service.employee;

import org.booking.core.domain.dto.EmployeeDto;
import org.booking.core.domain.entity.customer.User;
import org.booking.core.domain.entity.employee.Employee;
import org.booking.core.mapper.EmployeeMapper;
import org.booking.core.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeControllerBean implements EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeControllerBean(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDto create(EmployeeDto dto) {
        Employee businessService = employeeMapper.toEntity(dto);
        Employee save = employeeRepository.save(businessService);
        return employeeMapper.toDto(save);
    }

    @Override
    public EmployeeDto update(Long aLong, EmployeeDto dto) {
        Optional<Employee> optionalBusinessService = employeeRepository.findById(aLong);
        if (optionalBusinessService.isPresent()) {
            Employee existed = optionalBusinessService.get();
            Employee employee = employeeMapper.toEntity(dto);
            User employeeUser = employee.getUser();
            User user = existed.getUser();
            user.setEmail(employeeUser.getEmail());
            user.setName(employeeUser.getName());
            Employee save = employeeRepository.save(employee);
            return employeeMapper.toDto(save);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            employeeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public EmployeeDto getById(Long userId) {
        Optional<Employee> optionalBusinessService = employeeRepository.findById(userId);
        if (optionalBusinessService.isPresent()) {
            return employeeMapper.toDto(optionalBusinessService.get());
        }
        return null;
    }

    @Override
    public List<EmployeeDto> getAll() {
        List<Employee> all = employeeRepository.findAll();
        if (!all.isEmpty()) {
            List<EmployeeDto> services = new ArrayList<>();
            all.forEach(business -> {
                services.add(employeeMapper.toDto(business));
            });
            return services;
        }
        return null;
    }
}
