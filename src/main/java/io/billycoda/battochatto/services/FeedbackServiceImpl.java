package io.billycoda.battochatto.services;

import io.billycoda.battochatto.models.Feedback;
import io.billycoda.battochatto.models.User;
import io.billycoda.battochatto.repositories.FeedbackRepository;
import io.billycoda.battochatto.repositories.UserRepository;
import io.billycoda.battochatto.validators.FeedbackValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    // Constructor
    public FeedbackServiceImpl() {
    }

    // Declarations
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private static final Logger logger = LogManager.getLogger(FeedbackServiceImpl.class.getName());
    private Feedback feedback;
    private List<Feedback> feedbackList;
    private ResponseEntity<Feedback> responseEntity;
    private ResponseEntity<String> stringResponseEntity;
    private ResponseEntity<List<Feedback>> listResponseEntity;

    // Methods

    /**
     * Finds all feedback in the DB
     *
     * @return Feedback list, status
     */
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        try {
            if (feedbackRepository.findAll().size() > 0) {
                feedbackList = feedbackRepository.findAll();
                return listResponseEntity = new ResponseEntity<>(feedbackList, HttpStatus.OK);
            } else {
                return listResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return listResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id
     * @return
     */
    public ResponseEntity<Feedback> getFeedbackById(String id) {
        try {
            if (feedbackRepository.findById(id).isPresent()) {
                feedback = feedbackRepository.findById(id).get();
                return responseEntity = new ResponseEntity<>(feedback, HttpStatus.OK);
            } else {
                return responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param userId
     * @return
     */
    public ResponseEntity<List<Feedback>> getFeedbackByUserId(String userId) {
        return null;
    }

    /**
     * @param feedback
     * @return
     */
    public ResponseEntity<String> postFeedback(Feedback feedback) {
        try {
            FeedbackValidator feedbackValidator = new FeedbackValidator(feedback);
            if (feedbackValidator.validData()) {
                if (userRepository.findById(feedback.getUserId()).isPresent()) {
                    feedbackRepository.save(feedback);
                    return stringResponseEntity = new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    return stringResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            return stringResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.toString());
            return stringResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id
     * @param updatedInfo
     * @return
     */
    public ResponseEntity<String> updateFeedback(String id, Feedback updatedInfo) {
        try { // work with user somehow
            FeedbackValidator feedbackValidator = new FeedbackValidator(updatedInfo);
            boolean valid = feedbackValidator.validData();
            if (valid) {
                if (feedbackRepository.findById(id).isPresent()) {
                    if (userRepository.findById(updatedInfo.getUserId()).isPresent()) {
                        feedbackRepository.save(updatedInfo);
                        return stringResponseEntity = new ResponseEntity<>(HttpStatus.OK);
                    }
                }
//                } else {
                return stringResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
            } else {
                return stringResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return stringResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id
     * @return
     */
    public ResponseEntity<String> deleteFeedback(String id) {
        if (feedbackRepository.findById(id).isPresent()) {
            feedback = feedbackRepository.findById(id).get();
            try {
                feedbackRepository.delete(feedback);
                return stringResponseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                logger.error(e.toString());
                return stringResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return stringResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
