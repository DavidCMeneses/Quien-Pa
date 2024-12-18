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

router.get('/', getActivities);
router.get('/:id', getActivityById);
router.post('/', createActivity);
router.post('/:activityId/users/:userId', assignUserToActivity); //TODO: Simplify this route
router.get('/:id/users', getUsersByActivity);
router.put('/:id', updateActivity);
router.delete('/:id', deleteActivity);

module.exports = router;