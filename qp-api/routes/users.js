const express = require('express');
const router = express.Router();

//TODO: Add extra user operations
const {
    getUsers,
    getUserById,
    createUser,
    updateUser,
    deleteUser,
} = require('../controllers/userController');

router.get('/users', getUsers);
router.get('/users/:id', getUserById);
router.post('/users', createUser);
router.put('/users/:id', updateUser);
router.delete('/users/:id', deleteUser);

module.exports = router;