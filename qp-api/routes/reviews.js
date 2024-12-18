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

router.post('/reviews', addReview);
router.get('/users/:userId/reviews', getReviewsForUser);
router.get('/activities/:activityId/reviews', getReviewsForActivity);
router.get('/activities/:activityId/average-rating', getAverageRatingForActivity);
router.get('/users/:userId/average-rating', getAverageRatingForUser);
router.put('/reviews/:id', updateReview);
router.delete('/reviews/:id', deleteReview);

module.exports = router;