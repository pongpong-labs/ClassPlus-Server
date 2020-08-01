package pnu.classplus.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/*")
public class TestRestAPIs {

    @GetMapping("/st")
    @PreAuthorize("hasRole('STUDENT')")
    public String userAccess() {
        return ">>> User Contents!";
    }

    @GetMapping("/prof")
    @PreAuthorize("hasRole('PROFESSOR')")
    public String projectManagementAccess() {
        return ">>> Board Management Project";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return ">>> Admin Contents";
    }
}