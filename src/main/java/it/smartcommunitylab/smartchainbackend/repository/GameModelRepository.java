package it.smartcommunitylab.smartchainbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.smartcommunitylab.smartchainbackend.model.GameModel;

@Repository
public interface GameModelRepository extends MongoRepository<GameModel, String> {

}
