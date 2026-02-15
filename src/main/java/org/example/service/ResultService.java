package org.example.service;

import org.example.entity.Result;
import org.example.repository.ResultRepository;

import java.util.List;

public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void addResult(Result result) {
        this.resultRepository.addResult(result);
    }

    public List<Result> getAllResults() {
        return this.resultRepository.getAllResults();
    }
}
