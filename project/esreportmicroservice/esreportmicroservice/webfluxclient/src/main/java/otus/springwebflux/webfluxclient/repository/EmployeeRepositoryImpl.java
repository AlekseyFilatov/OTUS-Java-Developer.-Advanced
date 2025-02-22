package otus.springwebflux.webfluxclient.repository;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import otus.springwebflux.webfluxclient.model.Employee;


@Slf4j
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository, AutoCloseable{

    private ConcurrentMap<Long, Employee> repositoryMap = new ConcurrentHashMap<>();
    
    @Override
    public ConcurrentMap<Long, Employee> getRepo() {
            
            return repositoryMap;
        }
    
    @Override
    public Employee putStorage(long employeeId, Employee employee) {
            
            repositoryMap.put(employeeId, employee);
            return employee;
        }
    
    @Override
    public Employee get(long employeeId) {
            
           return repositoryMap.get(employeeId);
        }
    
    public Employee saveEmployeeAll(Employee emps) {
        log.info("SAVE!");
        
        return this.putStorage(emps.getEmployeeId(), emps);
    }

    public void close() throws Exception {
            repositoryMap.clear();
        }

}
