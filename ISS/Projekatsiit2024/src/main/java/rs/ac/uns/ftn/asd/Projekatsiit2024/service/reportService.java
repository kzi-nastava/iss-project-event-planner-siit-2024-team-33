package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Report;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ReportRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class reportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AuthentifiedUserRepository userRepository;

    public Report createReport(String content, Integer authorId, Integer receiverId) {
        AuthentifiedUser author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found."));
        AuthentifiedUser receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found."));

        Report report = new Report();
        report.content = content;
        report.dateOfSending = Date.valueOf(LocalDate.now());
        report.author = author;
        report.receiver = receiver;

        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(int reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
    }

    public void suspendUser(Integer userId) {
        AuthentifiedUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setSuspensionEndDate(Date.valueOf(LocalDate.now().plusDays(3)));
        userRepository.save(user);
    }

    public long getSuspensionTimeRemaining(Integer userId) {
        AuthentifiedUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (user.getSuspensionEndDate() == null) return 0;

        long remainingTime = user.getSuspensionEndDate().getTime() - System.currentTimeMillis();
        return Math.max(0, remainingTime / (1000 * 60 * 60 * 24));
    }
}
