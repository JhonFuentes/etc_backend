package com.etc.backend.service;

import com.etc.backend.dto.response.GeneralDashboardResponse;
import com.etc.backend.dto.response.StudentDashboardResponse;
import com.etc.backend.dto.response.TeacherDashboardResponse;

public interface DashboardService {
    StudentDashboardResponse getStudentDashboard(Integer usuarioId);
    TeacherDashboardResponse getTeacherDashboard(Integer usuarioId);
    GeneralDashboardResponse getGeneralDashboard();
}
