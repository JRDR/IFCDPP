package com.ifcdpp.ifcdpp.controllers;

import com.ifcdpp.ifcdpp.models.Issue;
import com.ifcdpp.ifcdpp.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SupportController {

    private final IssueService issueService;

    @GetMapping("/support")
    public String getSupportPage(Model model) {
        model.addAttribute("title", "Поддержка");
        return "support";
    }

    @GetMapping("/support-admin")
    public String getAdminSupportPage(Model model) {
        model.addAttribute("issues", issueService.getAllIssues());
        model.addAttribute("title", "Поддержка");
        return "support-admin";
    }

    @PostMapping(path = "/support", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendIssue(Issue issue) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        issueService.saveIssue(issue, email);
        return "redirect:/";
    }

    @PostMapping("/support/delete/{id}")
    public String deleteIssue(@PathVariable("id") Long id) {
        issueService.deleteIssue(id);
        return "redirect:/support-admin";
    }
}
