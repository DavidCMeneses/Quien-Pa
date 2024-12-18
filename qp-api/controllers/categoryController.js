const pool = require('../db');

const getCategories = async (req, res) => {
    try {
        const categories = await pool.query('SELECT * FROM categories');
        res.json(categories.rows);
    } catch (error) {
        console.error(error.message);
    }
}

const getCategoryById = async (req, res) => {
    try {
        const { id } = req.params;
        const category = await pool.query('SELECT * FROM categories WHERE id = $1', [id]);
        if (category.rows.length === 0) {
            return res.status(404).json({ message: 'Category not found' });
        }
        res.json(category.rows[0]);
    } catch (error) {
        console.error(error.message);
    }
}

const getCategoriesByActivity = async (req, res) => {
    const { id } = req.params;
    try {
        const categories = await pool.query(
            'SELECT categories.id, categories.name FROM categories JOIN activity_category ON categories.id = activity_category.category_id WHERE activity_category.activity_id = $1',
            [id]
        );
        res.status(200).json(categories.rows);
    } catch (error) {
        console.error(error.message);
    }
}

const getCategoriesByUser = async (req, res) => {
    const { id } = req.params;
    try {
        const categories = await pool.query(
            'SELECT categories.id, categories.name FROM categories JOIN user_category ON categories.id = user_category.category_id WHERE user_category.user_id = $1',
            [id]
        );
        res.status(200).json(categories.rows);
    } catch (error) {
        console.error(error.message);
    }
}

const createCategory = async (req, res) => {
    const { name } = req.body;
    try {
        const result = await pool.query(
            'INSERT INTO categories (name) VALUES ($1) RETURNING *',
            [name]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const assignUserToCategory = async (req, res) => {
    const { categoryId, userId } = req.params;
    try {
        const result = await pool.query(
            'INSERT INTO user_category (category_id, user_id) VALUES ($1, $2) RETURNING *',
            [categoryId, userId]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const updateCategory = async (req, res) => {
    const { id } = req.params;
    const { name } = req.body;
    try {
        const result = await pool.query(
            'UPDATE categories SET name = $1 WHERE id = $2 RETURNING *',
            [name, id]
        );
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Category not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const deleteCategory = async (req, res) => {
    const { id } = req.params;
    try {
        const result = await pool.query('DELETE FROM categories WHERE id = $1 RETURNING *', [id]);
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Category not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

module.exports = {
    getCategories,
    getCategoryById,
    getCategoriesByActivity,
    getCategoriesByUser,
    createCategory,
    assignUserToCategory,
    updateCategory,
    deleteCategory,
};