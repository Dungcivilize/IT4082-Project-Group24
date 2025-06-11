package KTPM.Backend.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.userId NOT IN (" +
           "SELECT ao.user.userId FROM ApartmentOwnership ao WHERE ao.status = 'active')")
    List<User> findUsersNotOwningAnyActiveApartment();
}
