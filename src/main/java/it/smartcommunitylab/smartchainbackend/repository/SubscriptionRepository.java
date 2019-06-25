package it.smartcommunitylab.smartchainbackend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.smartcommunitylab.smartchainbackend.model.Subscription;
import it.smartcommunitylab.smartchainbackend.model.Subscription.CompositeKey;

public interface SubscriptionRepository extends MongoRepository<Subscription, CompositeKey> {

    public List<Subscription> findByIdPlayerId(String playerId);
}
