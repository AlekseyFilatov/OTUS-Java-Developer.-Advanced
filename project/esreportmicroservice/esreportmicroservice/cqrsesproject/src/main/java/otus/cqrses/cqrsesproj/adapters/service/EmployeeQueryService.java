package otus.cqrses.cqrsesproj.adapters.service;

import otus.cqrses.cqrsesproj.adapters.queries.GetEmployeeByIDQuery;
import otus.cqrses.cqrsesproj.application.dto.EmployeeResponseDTO;


public interface EmployeeQueryService {
    EmployeeResponseDTO handle(GetEmployeeByIDQuery query);

}

