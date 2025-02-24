const express = require('express');
const router = express.Router();

const {
    getCategories,
    getCategoryById,
    getCategoriesByActivity,
    getCategoriesByUser,
    createCategory,
    assignUserToCategory,
    assignCategoryToActivity,
    updateCategory,
    deleteCategory,
} = require('../controllers/categoryController');

router.get('/', getCategories);
router.get('/:id', getCategoryById);
router.get('/activities/:id', getCategoriesByActivity);
router.get('/users/:id', getCategoriesByUser);
router.post('/', createCategory);
router.post('/:categoryId/users/:userId', assignUserToCategory); //TODO: Simplify this route
router.post('/:categoryId/activities', assignCategoryToActivity);
router.put('/:id', updateCategory);
router.delete('/:id', deleteCategory);

module.exports = router;