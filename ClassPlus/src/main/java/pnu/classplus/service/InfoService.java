package pnu.classplus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.classplus.ApiResponse;
import pnu.classplus.domain.entity.UniversityEntity;
import pnu.classplus.domain.repository.UniversityRepository;

import java.util.Set;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class InfoService {

    private final UniversityRepository univRepo;

    public ResponseEntity getUniversityInfo() {
        Set<UniversityEntity> univSet = univRepo.findAll();

        ObjectMapper mapper = new ObjectMapper();
        String resultJSON;
        try {
            resultJSON = mapper.writeValueAsString(univSet).replace("\"[", "[").replace("]\"", "]")
                .replace("\\\"{", "{").replace("}\\\"", "}")
                .replace("\\\\\\\"", "\"");
        } catch (JsonProcessingException e) {
            return new ResponseEntity(new ApiResponse(11, "error"),
                HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new ApiResponse(11, resultJSON),
            HttpStatus.OK);
    }
}
