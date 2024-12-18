const express = require('express');
const router = express.Router();
const {
    addReview,
    getReviewsForUser,
    getReviewsForActivity,
    getAverageRatingForActivity,
    getAverageRatingForUser,
    updateReview,
    deleteReview,
} = require('../controllers/reviewController');

router.post('/', addReview);
router.get('/users/:userId', getReviewsForUser);
router.get('/activities/:activityId', getReviewsForActivity);
router.get('/activities/:activityId/average-rating', getAverageRatingForActivity);
router.get('/users/:userId/average-rating', getAverageRatingForUser);
router.put('/:id', updateReview);
router.delete('/:id', deleteReview);

module.exports = router;