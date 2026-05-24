package com.example.HospitalManagement.repo;

import com.example.HospitalManagement.model.Paitent;
import com.example.HospitalManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaitentRepository extends JpaRepository<Paitent, Long> {


        // Search by uniqueId only
        Optional<Paitent> findByUserUniqueIdIgnoreCase(String uniqueId);

        // Search by firstName only
        List<Paitent> findByFirstNameIgnoreCaseContaining(String firstName);

        // Search by lastName only
        List<Paitent> findByLastNameIgnoreCaseContaining(String lastName);

        // Search by firstName + lastName
        List<Paitent> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(String firstName, String lastName);

        // Search by firstName + lastName + uniqueId
        List<Paitent> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContainingAndUserUniqueIdIgnoreCase(
                String firstName, String lastName, String uniqueId
        );

        // Optional: firstName + uniqueId
        List<Paitent> findByFirstNameIgnoreCaseContainingAndUserUniqueIdIgnoreCase(String firstName, String uniqueId);

        // Optional: lastName + uniqueId
        List<Paitent> findByLastNameIgnoreCaseContainingAndUserUniqueIdIgnoreCase(String lastName, String uniqueId);



Paitent findByUser(User user);


    Optional<Paitent> findByUserUniqueId(String uniqueId);
}
    /*
    Optional<Paitent> findByUser_UniqueId(String uniqueId);

    // Search by first and last name (partial + case insensitive)
    List<Paitent> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    List<Paitent> findByFirstName(String firstName);

    List<Paitent> findByLastName(String lastName);

    Paitent findByUser(User user);

    // Optional: Search by mobile number
    //List<Paitent> findByMobileNumber(String mobileNumber);

    //Optional<Paitent> findByUniqueId(String uniqueId);



}
*/



