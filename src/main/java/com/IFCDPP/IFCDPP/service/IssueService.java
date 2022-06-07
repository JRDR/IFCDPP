package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.IssueEntity;
import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.Issue;
import com.ifcdpp.ifcdpp.models.IssueUser;
import com.ifcdpp.ifcdpp.repo.IssueRepository;
import com.ifcdpp.ifcdpp.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public List<Issue> getAllIssues() {
        return issueRepository.findAll().stream().map(this::mapIssue).collect(Collectors.toList());
    }

    public void saveIssue(Issue issue, String email) {
        IssueEntity entity = new IssueEntity();
        entity.setTopic(issue.getTopic());
        entity.setDescription(issue.getDescription());

        User user = userRepository.findByEmail(email).orElseThrow(() -> new MessageException("User not found"));
        entity.setUser(user);

        issueRepository.save(entity);
    }

    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }

    private Issue mapIssue(IssueEntity entity) {
        return Issue.builder().id(entity.getId()).topic(entity.getTopic()).description(entity.getDescription())
                .user(mapUser(entity.getUser())).build();
    }

    private IssueUser mapUser(User entity) {
        return IssueUser.builder().id(entity.getId()).email(entity.getEmail()).login(entity.getLogin()).build();
    }

}
