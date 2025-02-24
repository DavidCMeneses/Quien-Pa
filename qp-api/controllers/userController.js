const pool = require('../db');
const bcrypt = require('bcrypt');

const getUsers = async (req, res) => {
    try {
        const users = await pool.query('SELECT * FROM users');
        res.json(users.rows);
    } catch (error) {
        console.error(error.message);
    }
}

const getUserById = async (req, res) => {
    try {
        const { id } = req.params;
        const user = await pool.query('SELECT * FROM users WHERE id = $1', [id]);
        if (user.rows.length === 0) {
            return res.status(404).json({ message: 'User not found' });
        }
        res.json(user.rows);
    } catch (error) {
        console.error(error.message);
    }
}

const createUser = async (req, res) => {
    const { name, email, password, age, description } = req.body;
    const hashedPassword = await bcrypt.hash(password, parseInt(process.env.SALT_ROUNDS, 10));
    try {

        const { rowCount } = await pool.query('SELECT 1 FROM users WHERE email = $1', [email]);

        if (rowCount > 0) {
            return res.status(409).json({ message: 'Email is already in use' });
        }
        const result = await pool.query(
            'INSERT INTO users (name, email, password, age, description) VALUES ($1, $2, $3, $4, $5) RETURNING *',
            [name, email, hashedPassword, age, description]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const updateUser = async (req, res) => {
    const { id } = req.params;
    const { name, email, age, description, imageUrl } = req.body;
    try {
        const result = await pool.query(
            'UPDATE users SET name = $1, email = $2, age = $3, description = $4, imageUrl = $5 WHERE id = $6 RETURNING *',
            [name, email, age, description, imageUrl, id]
        );
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'User not found' });
        }
        res.status(200).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const deleteUser = async (req, res) => {
    const { id } = req.params;
    try {
        const result = await pool.query('DELETE FROM users WHERE id = $1 RETURNING *', [id]);
        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'User not found' });
        }
        res.status(200).json({ message: 'User deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const loginUser = async (req, res) => {
    const { email, password } = req.body;
    try {
        const user = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
        if (user.rows.length === 0) {
            return res.status(404).json({ success: false, message: 'User not found' });
        }
        const isPasswordMatch = await bcrypt.compare(password, user.rows[0].password);
        if (!isPasswordMatch) {
            return res.status(401).json({ message: 'Invalid credentials' });
        }
        res.status(200).json({userId: user.rows[0].id , success: true, message: 'Login successful' });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

module.exports = {
    getUsers,
    getUserById,
    createUser,
    updateUser,
    deleteUser,
    loginUser,
};