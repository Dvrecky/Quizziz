package org.example.repository;

import org.example.entity.Result;

import java.util.*;

public class ResultRepository {

//    private final List<Result> resultList;
    private final Deque<Result> resultList;

    public ResultRepository() {
        this.resultList = new ArrayDeque<>();
    }

    public void addResult(Result newResult) {
        this.resultList.addFirst(newResult);
    }

    public List<Result> getAllResults() {
        return new ArrayList<>(resultList);
    }
}
