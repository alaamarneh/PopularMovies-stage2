package com.ala.populermovies.repository;

public class RepoFactory {
    public static DataRepository getDataRepository() {
        return AppDataRepository.getInstance();
    }
}
