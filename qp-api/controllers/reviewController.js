const pool = require('../db');

const addReview = async (req, res) => {
    const { activityId, reviewerId, reviewedUserId, rating, comment } = req.body;

    if (rating < 1 || rating > 5) {
        return res.status(400).json({ message: 'Rating must be between 1 and 5.' });
    }

    try {
        const result = await pool.query(
            `INSERT INTO review (activity_id, reviewer_id, reviewed_user_id, rating, comment) 
             VALUES ($1, $2, $3, $4, $5) RETURNING *`,
            [activityId, reviewerId, reviewedUserId, rating, comment]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getReviewsForUser = async (req, res) => {
    const { userId } = req.params;
    try {
        const reviews = await pool.query(
            `SELECT * FROM review WHERE reviewed_user_id = $1`,
            [userId]
        );
        res.status(200).json(reviews.rows);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getReviewsForActivity = async (req, res) => {
    const { activityId } = req.params;
    try {
        const reviews = await pool.query(
            `SELECT * FROM review WHERE activity_id = $1`,
            [activityId]
        );
        res.status(200).json(reviews.rows);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getAverageRatingForActivity = async (req, res) => {
    const { activityId } = req.params;
    try {
        const result = await pool.query(
            `SELECT AVG(rating) AS average_rating FROM review WHERE activity_id = $1`,
            [activityId]
        );
        res.status(200).json({ averageRating: result.rows[0].avg });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getAverageRatingForUser = async (req, res) => {
    const { userId } = req.params;
    try {
        const result = await pool.query(
            `SELECT AVG(rating) AS average_rating FROM review WHERE reviewed_user_id = $1`,
            [userId]
        );
        res.status(200).json({ averageRating: result.rows[0].avg });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const updateReview = async (req, res) => {
    const { id } = req.params;
    const { rating, comment } = req.body;

    if (rating < 1 || rating > 5) {
        return res.status(400).json({ message: 'Rating must be between 1 and 5.' });
    }

    try {
        const result = await pool.query(
            `UPDATE review SET rating = $1, comment = $2 WHERE id = $3 RETURNING *`,
            [rating, comment, id]
        );
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Review not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const deleteReview = async (req, res) => {
    const { id } = req.params;
    try {
        const result = await pool.query(
            `DELETE FROM review WHERE id = $1 RETURNING *`,
            [id]
        );
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Review not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

module.exports = {
    addReview,
    getReviewsForUser,
    getReviewsForActivity,
    getAverageRatingForActivity,
    getAverageRatingForUser,
    updateReview,
    deleteReview,
};