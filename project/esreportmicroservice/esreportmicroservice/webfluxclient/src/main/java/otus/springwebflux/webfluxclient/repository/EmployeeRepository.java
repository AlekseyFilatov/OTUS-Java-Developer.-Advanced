package otus.springwebflux.webfluxclient.repository;

import java.util.concurrent.ConcurrentMap;
import otus.springwebflux.webfluxclient.model.Employee;


public interface EmployeeRepository {

   public ConcurrentMap<Long,Employee> getRepo();

   public Employee putStorage(long employeeId, Employee employee);

   public Employee get(long employeeId);

}