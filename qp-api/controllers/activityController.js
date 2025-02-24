const pool = require('../db');

const getActivities = async (req, res) => {
    try {
        const activities = await pool.query('SELECT * FROM activities');
        res.json(activities.rows);
    } catch (error) {
        console.error(error.message);
    }
};

const getActivityById = async (req, res) => {
    try {
        const { id } = req.params;
        const activity = await pool.query('SELECT * FROM activities WHERE id = $1', [id]);
        if (activity.rows.length === 0) {
            return res.status(404).json({ message: 'Activity not found' });
        }
        res.json(activity.rows[0]);
    } catch (error) {
        console.error(error.message);
    }
};

const createActivity = async (req, res) => {
    // User Id refers to the user who created the activity
    const { name, description, latitude, longitude, userId, date, place } = req.body;
    const createdDate = new Date();
    try {
        const result = await pool.query(
            'INSERT INTO activities (name, description, latitude, longitude, created_by, date, place, createdOn) VALUES ($1, $2, $3, $4, $5, $6, $7, $8) RETURNING *',
            [name, description, latitude, longitude, userId, date, place, createdDate]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const assignUserToActivity = async (req, res) => {
    const { activityId, userId } = req.params;
    // User_Activity table is a junction table that handles many-to-many relationship (participation) between users and activities
    try {
        const result = await pool.query(
            'INSERT INTO user_activity (activity_id, user_id) VALUES ($1, $2) RETURNING *',
            [activityId, userId]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const getUsersByActivity = async (req, res) => {
    const { id } = req.params;
    try {
        const users = await pool.query(
            'SELECT users.id, users.name, users.email, users.age, users.description FROM users JOIN user_activity ON users.id = user_activity.user_id WHERE user_activity.activity_id = $1',
            [id]
        );
        res.status(200).json(users.rows);
    } catch (error) {
        console.error(error.message);
    }
}

const updateActivity = async (req, res) => {
    const { id } = req.params;
    const { name, description, latitude, longitude, date, place } = req.body;
    try {
        const result = await pool.query(
            'UPDATE activities SET name = $1, description = $2, latitude = $3, longitude = $4, date = $5, place = $6 WHERE id = $7 RETURNING *',
            [name, description, latitude, longitude, date, place, id]
        );
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Activity not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const deleteActivity = async (req, res) => {
    const { id } = req.params;
    try {
        const result = await pool.query('DELETE FROM activities WHERE id = $1 RETURNING *', [id]);
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Activity not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

module.exports = {
    getActivities,
    getActivityById,
    createActivity,
    assignUserToActivity,
    getUsersByActivity,
    updateActivity,
    deleteActivity,
};
