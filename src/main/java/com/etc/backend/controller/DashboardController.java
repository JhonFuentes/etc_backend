package com.etc.backend.controller;

import com.etc.backend.dto.response.GeneralDashboardResponse;
import com.etc.backend.dto.response.StudentDashboardResponse;
import com.etc.backend.dto.response.TeacherDashboardResponse;
import com.etc.backend.security.UserPrincipal;
import com.etc.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/student")
    public ResponseEntity<?> studentDashboard(Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        StudentDashboardResponse resp = dashboardService.getStudentDashboard(user.getId());
        if (resp == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> teacherDashboard(Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        TeacherDashboardResponse resp = dashboardService.getTeacherDashboard(user.getId());
        if (resp == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/general")
    public ResponseEntity<GeneralDashboardResponse> generalDashboard() {
        return ResponseEntity.ok(dashboardService.getGeneralDashboard());
    }
}
