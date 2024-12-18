const express = require('express');
const router = express.Router();

const {
    getActivities,
    getActivityById,
    createActivity,
    assignUserToActivity,
    getUsersByActivity,
    updateActivity,
    deleteActivity,
} = require('../controllers/activityController');

router.get('/activities', getActivities);
router.get('/activities/:id', getActivityById);
router.post('/activities', createActivity);
router.post('/activities/:activityId/users/:userId', assignUserToActivity);
router.get('/activities/:id/users', getUsersByActivity);
router.put('/activities/:id', updateActivity);
router.delete('/activities/:id', deleteActivity);

module.exports = router;